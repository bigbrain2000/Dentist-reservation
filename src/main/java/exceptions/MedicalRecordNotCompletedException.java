package exceptions;

public class MedicalRecordNotCompletedException extends Exception{

    public MedicalRecordNotCompletedException(){
        super("Please complete the medical record first!");
    }
}
