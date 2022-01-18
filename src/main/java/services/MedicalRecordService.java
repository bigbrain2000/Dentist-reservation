package services;

import exceptions.username.MedicalRecordUsernameAlreadyExistsException;
import model.MedicalRecord;
import model.User;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.List;
import java.util.Objects;

import static services.FileSystemService.getPathToFile;

public class MedicalRecordService {

    private static ObjectRepository<MedicalRecord> medicalRecordRepository;
    private static Nitrite database;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("medical_record_database.db").toFile())
                .openOrCreate("test3", "test3");

        medicalRecordRepository = database.getRepository(MedicalRecord.class);
    }

    public static void deleteMedicalRecordFromDB(String usernameMedicalRecord) {
        for (MedicalRecord medicalRecord : medicalRecordRepository.find())
            if (Objects.equals(usernameMedicalRecord, medicalRecord.getUsername()))
                medicalRecordRepository.remove(medicalRecord);
    }

    public static void addMedicalRecord(String username, String answerFirstQuestion, String answerSecondQuestion, String answerThirdQuestion,
                                        String answerFourthQuestion, String vaccinated) throws MedicalRecordUsernameAlreadyExistsException {
        checkMedicalRecordUsernameAlreadyExists(username);
        medicalRecordRepository.insert(new MedicalRecord(username, answerFirstQuestion, answerSecondQuestion, answerThirdQuestion, answerFourthQuestion, vaccinated));
    }

    public static void checkMedicalRecordUsernameAlreadyExists(String username) throws MedicalRecordUsernameAlreadyExistsException {
        for (MedicalRecord medicalRecord : medicalRecordRepository.find()) {
            if (Objects.equals(username, medicalRecord.getUsername()))
                throw new MedicalRecordUsernameAlreadyExistsException(username);
        }
    }

    public static boolean isMedicalRecordCompleted(String username) {
        for (MedicalRecord medicalRecord : medicalRecordRepository.find()) {
            if (Objects.equals(username, medicalRecord.getUsername()))
                return true;
        }

        return false;
    }

    public static ObjectRepository<MedicalRecord> getMedicalRecordRepository() {
        return medicalRecordRepository;
    }

    public static List<MedicalRecord> getMedicalRecordList() {
        return medicalRecordRepository.find().toList();
    }

    public static Nitrite getDatabase() {
        return database;
    }
}
