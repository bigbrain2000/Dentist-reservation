package exceptions;

public class MedicalRecordUsernameAlreadyExistException extends Exception{

    public MedicalRecordUsernameAlreadyExistException(String username){
        super(String.format("Medical record for the user %s already exist!", username));
    }
}
