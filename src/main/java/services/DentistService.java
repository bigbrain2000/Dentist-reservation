package services;

import exceptions.username.DentistServiceNameAlreadyExistsException;
import exceptions.fields.FieldNotCompletedException;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Service;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Objects;
import static services.FileSystemService.getPathToFile;

public class DentistService {

    private static ObjectRepository<Service> dentistRepository;
    private static Nitrite database;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("dentist_services_database.db").toFile())
                .openOrCreate("test2", "test2");

        dentistRepository = database.getRepository(Service.class);
    }

    public static void addDentistService(String name, float price) throws FieldNotCompletedException, DentistServiceNameAlreadyExistsException {
        checkDentistServiceNameAlreadyExist(name);
        checkAllFieldsAreCompleted(name, price);
        dentistRepository.insert(new Service(name, price));
    }


    public static void checkAllFieldsAreCompleted(@NotNull String name, float price) throws FieldNotCompletedException {
        if (name.trim().isEmpty() || String.valueOf(price).trim().isEmpty())
            throw new FieldNotCompletedException();
    }

    public static void checkDentistServiceNameAlreadyExist(String name) throws DentistServiceNameAlreadyExistsException {
        for (Service dentistService : dentistRepository.find()) {
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

    @NotNull
    public static ArrayList<String> getDentistServiceNameList() {
        ArrayList<String> dentistServiceList = new ArrayList<>();

        for(Service service : dentistRepository.find()) {
            dentistServiceList.add(service.getName());
        }

        return dentistServiceList;
    }

    public static void getDentistServicePriceBasedOnName(@NotNull ChoiceBox<String> dentistServiceName, @NotNull TextField dentistServicePrice) {
        for(Service service : dentistRepository.find())
            if(Objects.equals(service.getName(), dentistServiceName.getValue()))
                dentistServicePrice.setText(String.valueOf(service.getPrice()));
    }

    public static ObjectRepository<Service> getDentistRepository() {
        return dentistRepository;
    }

    public static Nitrite getDatabase() {
        return database;
    }
}
