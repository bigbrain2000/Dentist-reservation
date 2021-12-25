package exceptions;

public class UsernameDoesNotExistsException extends Exception {

    private String username;

    public UsernameDoesNotExistsException(String username) {
        super("An account with that username doesn't exists!");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
