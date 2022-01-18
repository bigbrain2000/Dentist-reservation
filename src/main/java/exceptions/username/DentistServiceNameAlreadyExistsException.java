package exceptions.username;

public class DentistServiceNameAlreadyExistsException extends Exception {
    private String name;

    public DentistServiceNameAlreadyExistsException(String name) {
        super("A service with that name already exists!");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
