package services;

import exceptions.DentistServiceNameAlreadyExistsException;
import exceptions.FieldNotCompletedException;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import java.util.Objects;
import static services.FileSystemService.getPathToFile;

public class DentistService {

    private static ObjectRepository<model.DentistService> dentistRepository;
    private static Nitrite database;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("dentist_services_database.db").toFile())
                .openOrCreate("test2", "test2");

        dentistRepository = database.getRepository(model.DentistService.class);
    }

    public static void addDentistService(String name, float price) throws FieldNotCompletedException, DentistServiceNameAlreadyExistsException {
        checkDentistServiceNameAlreadyExist(name);
        checkAllFieldsAreCompleted(name, price);
        dentistRepository.insert(new model.DentistService(name, price));
    }


    public static void checkAllFieldsAreCompleted(String name, float price) throws FieldNotCompletedException {
        if (name.trim().isEmpty() || String.valueOf(price).trim().isEmpty())
            throw new FieldNotCompletedException();
    }

    public static void checkDentistServiceNameAlreadyExist(String name) throws DentistServiceNameAlreadyExistsException {
        for (model.DentistService dentistService : dentistRepository.find()) {
            if (Objects.equals(name, dentistService.getName()))
                throw new DentistServiceNameAlreadyExistsException(name);
        }
    }

    public static boolean checkIfPriceIsAFloat(String price) {
        try {
            Float.parseFloat(price);
            return true;

        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static ObjectRepository<model.DentistService> getDentistRepository() {
        return dentistRepository;
    }

    public static Nitrite getDatabase() {
        return database;
    }
}
