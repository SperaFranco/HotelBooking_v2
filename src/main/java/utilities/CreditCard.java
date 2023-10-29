package utilities;

public class CreditCard {
    //Region Fields
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate; //Data nel formato MM/YY
    private int CVV; //Card verification number
    private double balance = 1000.0; //saldo imposto per default il saldo degli utenti a 1000
    //end Region


    public CreditCard(String cardHolderName, String cardNumber, String expiryDate, int CVV) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.CVV = CVV;

        //TODO potrei controllore se la carta è valida tramite isValid altrimenti lanciare eccezione
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
        //TODO da implementare --> guardare metodi per verifica carte di credito
        return false;
    }

    public void doPayment() {
        //TODO il metodo va messo qui o nei guest??
        //magari va fatto un controllo al saldo e se sufficiente esegui il pagamento
    }
}
