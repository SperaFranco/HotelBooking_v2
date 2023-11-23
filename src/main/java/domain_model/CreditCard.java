package domain_model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreditCard {
    //Region Fields
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate; //Data nel formato MM/YY
    private int CVV; //Card verification number
    private double balance = 1000.0; //imposto per default il saldo degli utenti a 1000
    //end Region


    public CreditCard(String cardHolderName, String cardNumber, String expiryDate, int CVV) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.CVV = CVV;

        //TODO potrei controllore se la carta è valida tramite isValid altrimenti lanciare eccezione
        if(!isValid(cardNumber, expiryDate, CVV))
            throw new RuntimeException("Not valid card!");
    }

    //Region Getters and setters
    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getCVV() {
        return CVV;
    }

    public void setCVV(int CVV) {
        this.CVV = CVV;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //end Region

    private boolean isValid(String cardNumber, String expiryDate, int CVV){
        //Al momento la gestione è molto semplificata
        // è possibile comunque guardare anche alcune api sui metodi di pagamento
        //Controllo il numero di carta
        if(cardNumber == null || cardNumber.length() != 16)
            return false;

        //Controllo l'expiryDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        LocalDate expirationDate = LocalDate.parse(expiryDate, formatter);
        if(expirationDate.isAfter(LocalDate.now()))
            return false;

        //controllo il cvv
        String cvvString = String.valueOf(CVV);
        if(cvvString.length() != 3 || CVV < 0)
            return false;

        return true;
    }

    public void doPayment(double amount) {
        //magari va fatto un controllo al saldo e se sufficiente esegui il pagamento
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Payment successful. Remaining balance: " + balance);
        }else {
            throw new RuntimeException("Insufficient balance for the payment.");
        }
    }
}
