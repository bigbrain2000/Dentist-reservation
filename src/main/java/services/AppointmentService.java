package services;

import exceptions.username.AppointmentUsernameAlreadyExistsException;
import exceptions.date.IncorrectDateException;
import model.Appointment;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import static services.FileSystemService.getPathToFile;

public class AppointmentService {
    private static ObjectRepository<Appointment> appointmentRepository;
    private static Nitrite database;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("appointment_service_database.db").toFile())
                .openOrCreate("test4", "test4");

        appointmentRepository = database.getRepository(Appointment.class);
    }

    public static void addAppointment(String username, String firstName, String secondName, String serviceName, float servicePrice, Date appointmentDate,
                                      String dentistName, boolean checkMedicalRecord) throws AppointmentUsernameAlreadyExistsException, IncorrectDateException {
        checkUsernameAlreadyExists(username);
        checkValidDate(appointmentDate);

        appointmentRepository.insert(new Appointment(username, firstName, secondName, dentistName,serviceName, servicePrice, appointmentDate, checkMedicalRecord));
    }

    private static void checkUsernameAlreadyExists(String username) throws AppointmentUsernameAlreadyExistsException{
        for(Appointment appointment : appointmentRepository.find())
            if(Objects.equals(username, appointment.getUsername()))
                throw  new AppointmentUsernameAlreadyExistsException(username);
    }

    private static void checkValidDate(@NotNull Date date) throws IncorrectDateException {
        LocalDate currentLocalDateTime = java.time.LocalDate.now();
        Date currentDate = convertToDateViaSqlDate(currentLocalDateTime);

        if(!date.after(currentDate)) {
            throw  new IncorrectDateException();
        }
    }

    @NotNull
    @Contract("_ -> new")
    private static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static ObjectRepository<Appointment> getAppointmentRepository() {
        return appointmentRepository;
    }

    public static Nitrite getDatabase() {
        return database;
    }
}
