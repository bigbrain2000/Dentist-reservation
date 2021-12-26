package services;

import exceptions.FieldNotCompletedException;
import exceptions.UsernameAlreadyExistsException;
import model.Dentist;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import java.util.Objects;
import static services.FileSystemService.getPathToFile;

public class DentistService {

    private static ObjectRepository<Dentist> dentistRepository;
    private static Nitrite database;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("dentist_database.db").toFile())
                .openOrCreate("test2", "test2");

        dentistRepository = database.getRepository(Dentist.class);
    }

    public static void addDentist(String firstName, String secondName, int age) throws FieldNotCompletedException, UsernameAlreadyExistsException {
        checkDentistAlreadyExist(firstName);
        checkAllFieldsAreCompleted(firstName, secondName, age);
        dentistRepository.insert(new Dentist(firstName, secondName, age));
    }


    public static void checkAllFieldsAreCompleted(String firstName, String secondName, int age) throws FieldNotCompletedException {
        if (firstName.trim().isEmpty() || secondName.trim().isEmpty() || String.valueOf(age).trim().isEmpty())
            throw new FieldNotCompletedException();
    }

    public static void checkDentistAlreadyExist(String firstName) throws UsernameAlreadyExistsException {
        for (Dentist dentist : dentistRepository.find()) {
            if (Objects.equals(firstName, dentist.getFirstName()))
                throw new UsernameAlreadyExistsException(firstName);
        }
    }

    public static ObjectRepository<Dentist> getDentistRepository() {
        return dentistRepository;
    }

    public static Nitrite getDatabase() {
        return database;
    }
}
