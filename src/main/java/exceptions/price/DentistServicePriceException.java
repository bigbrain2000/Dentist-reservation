package exceptions;

public class DentistServicePriceException extends Exception {
    public DentistServicePriceException() {
        super("Incorrect price !");
    }
}
