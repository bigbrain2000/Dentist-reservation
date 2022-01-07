package exceptions.username;

public class AppointmentUsernameAlreadyExistsException extends Exception{
    public AppointmentUsernameAlreadyExistsException(String username) {
        super(String.format("Appointment for the user %s already exist!", username));
    }
}
