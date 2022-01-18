package exceptions.username;

public class UserNotSelectedException extends Exception {

    public UserNotSelectedException() {
        super("Please select an appointment!");
    }
}
