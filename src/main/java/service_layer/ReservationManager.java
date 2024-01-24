package service_layer;
import data_access.ReservationDAO;
import domain_model.*;
import utilities.MailNotifier;
import utilities.Subject;
import utilities.Research;
import utilities.IdGenerator;

import javax.mail.internet.AddressException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ReservationManager extends Subject {

    private static AccountManager accountManager;
    private static CalendarManager calendarManager;
    private static ReservationManager reservationManager;

    private final ReservationDAO reservationDAO;

    private ReservationManager(AccountManager accountManager, CalendarManager calendarManager) {
        ReservationManager.accountManager = accountManager;
        ReservationManager.calendarManager = calendarManager;
        this.reservationDAO = new ReservationDAO();
    }
    public static ReservationManager createReservationManager(AccountManager accountManager, CalendarManager calendarManager){
        if(reservationManager == null)
            reservationManager = new ReservationManager(accountManager, calendarManager);
        return reservationManager;
    }

    public Reservation createReservation(Guest user, Research researchInfo, Hotel hotel, String roomID, String notes, boolean sendEmail) {
        if (user == null)
            throw new RuntimeException("user is null");

        Reservation newReservation = null;
        double sum = 0.0;
        for(LocalDate date = researchInfo.getCheckIn(); date.isBefore(researchInfo.getCheckOut()); date = date.plusDays(1)) {
            sum += calendarManager.getPrice(hotel.getId(), date.toString(), roomID);
        }

        if (accountManager.doPayment(user, sum) && calendarManager.isRoomAvailable(hotel.getId(),researchInfo, roomID)) {
            newReservation = new Reservation(IdGenerator.generateReservationID(hotel.getId(), user.getName(), user.getSurname(), researchInfo.getCheckIn()), researchInfo, notes, hotel.getId(), roomID, user.getId());
            addReservation(newReservation);

            if(sendEmail) {
                //TODO rimane da decidere chi manda le mail
                HotelDirector sender = accountManager.findHotelDirector(hotel.getId());

                try {
                    //Invio la mail al cliente
                    MailNotifier.sendEmail(sender, user, "Confirmation of Reservation", newReservation);
                    //Invio la mail al gestore
                    MailNotifier.sendEmail(sender, sender, "New Reservation", newReservation);
                } catch (AddressException e) {
                    System.out.println("Email not send correctly!");
                }
            }
        }
        return newReservation;
    }
    public void deleteReservation(Reservation reservation) {
        try {
            reservationDAO.removeReservation(reservation.getId());
        } catch (SQLException e) {
            System.out.println("Reservation not found!");
            throw new RuntimeException(e);
        }
        setChanged();
        notifyObservers(reservation, "Delete reservation");
    }
    public void updateReservation(Reservation reservation, String newNotes, LocalDate newCheckInDate, LocalDate newCheckOutDate) {
        if(newNotes != null)
            setDescription(reservation.getId(), newNotes);

        if(newCheckInDate != null && newCheckOutDate != null){
            setChanged();
            notifyObservers(reservation, "Delete reservation");
            Research research = new Research(null, newCheckInDate, newCheckOutDate, reservation.getNumOfGuests());
            if(calendarManager.isRoomAvailable(reservation.getHotel(), research, reservation.getRoomReserved())) {
                setCheckIn(reservation, newCheckInDate);
                setCheckOut(reservation, newCheckOutDate);
            }
            else
                throw new RuntimeException("the dates requested are not available");
            setChanged();
            notifyObservers(reservation, "Add reservation");
        }

    }
    public ArrayList<Reservation> getReservations(Guest guest) {
        if (guest == null)
            throw new RuntimeException("guest is a null reference");
        try {
            return reservationDAO.getReservationByGuest(guest.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Reservation> getAllReservations(Hotel hotel) {
        if (hotel == null)
            throw new RuntimeException("hotel is a null reference");
        try {
            return reservationDAO.getReservationByHotel(hotel.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //helper method
    private void addReservation(Reservation newReservation) {
        try {
            reservationDAO.addReservation(newReservation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setChanged();
        notifyObservers(newReservation, "Add reservation");
    }
    private void setCheckIn(Reservation reservation, LocalDate checkIn) {
        try {
            reservationDAO.setCheckIn(reservation.getId(), checkIn.toString());
            reservation.setCheckIn(checkIn);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setCheckOut(Reservation reservation, LocalDate checkOut) {
        try {
            reservationDAO.setCheckOut(reservation.getId(), checkOut.toString());
            reservation.setCheckOut(checkOut);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setDescription(String id, String newDescription) {
        try {
            reservationDAO.setNotes(id, newDescription);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
