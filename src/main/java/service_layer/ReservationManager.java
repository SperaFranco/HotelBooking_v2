package service_layer;
import data_access.ReservationDAO;
import domain_model.*;
import utilities.Subject;
import utilities.Research;
import utilities.IdGenerator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ReservationManager extends Subject {
    private final CalendarManager calendarManager;
    private final ReservationDAO reservationDAO;

    public ReservationManager(CalendarManager calendarManager) {
        this.calendarManager = calendarManager;
        this.reservationDAO = new ReservationDAO();
    }

    public Reservation createReservation(Guest user, Research researchInfo, Hotel hotel, String roomID, String description) {
        if (user == null)
            throw new RuntimeException("user is null");

        Reservation newReservation = null;
        //TODO devo veramente controllare se la camera risulta disponibile? --> controllo invece se ho abbastanza soldi
        double sum = 0.0;
        for(LocalDate date = researchInfo.getCheckIn(); !date.isAfter(researchInfo.getCheckOut()); date = date.plusDays(1)) {
            sum += calendarManager.getPrice(hotel.getId(), date.toString(), roomID);
        }

        if (user.getCard().doPayment(sum) && calendarManager.isRoomAvailable(hotel.getId(),researchInfo, roomID)) {
            newReservation = new Reservation(IdGenerator.generateReservationID(hotel.getId(), user.getName(), user.getSurname(), researchInfo.getCheckIn()), researchInfo, description, hotel.getId(), roomID, user.getId());
            addReservation(newReservation);
        }
        return newReservation;
    }

    private void addReservation(Reservation newReservation) {
        //TODO guardare come fare per mandare email di notifica prenotazione (qui o nel doReservation)
        try {
            reservationDAO.addReservation(newReservation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setChanged();
        notifyObservers(newReservation, "Add reservation");
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

    public void updateReservation(Reservation reservation, String newNote, LocalDate newCheckInDate, LocalDate newCheckOutDate) {
        if(newNote != null)
            setDescription(reservation.getId(), newNote);

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

    private void setDescription(String id, String newDescription) {
        try {
            reservationDAO.setNotes(id, newDescription);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
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

}
