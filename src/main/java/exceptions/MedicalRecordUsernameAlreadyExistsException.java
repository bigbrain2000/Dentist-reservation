package exceptions;

public class MedicalRecordUsernameAlreadyExistsException extends Exception{

    public MedicalRecordUsernameAlreadyExistsException(String username){
        super(String.format("Medical record for the user %s already exist!", username));
    }
}
