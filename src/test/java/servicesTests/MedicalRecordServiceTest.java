package servicesTests;

import exceptions.username.MedicalRecordUsernameAlreadyExistsException;
import model.MedicalRecord;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import services.FileSystemService;
import services.MedicalRecordService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordServiceTest {

    private final String USERNAME = "DOODS2022";
    private final String ANSWER_FIRST_QUESTION = "No";
    private final String ANSWER_SECOND_QUESTION = "No";
    private final String ANSWER_THIRD_QUESTION = "No";
    private final String ANSWER_FOURTH_QUESTION = "No";
    private final String VACCINATED = "Yes";

    @BeforeEach
    @DisplayName("Check if MedicalRecordRepository path is correct created and database is initialized")
    public void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-medical_record_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        MedicalRecordService.initDatabase();
    }

    @AfterEach
    @DisplayName("Check if MedicalRecord database is closed after every test")
    public void tearDown() {
        MedicalRecordService.getDatabase().close();
    }

    @Test
    @DisplayName("Check if MedicalRecord database is initialized and there are no medical records")
    public void testMedicalRecordRepositoryIsInitializedAndNoMedicalRecordIsPersisted() {
        assertThat(MedicalRecordService.getMedicalRecordList()).isNotNull();
        assertThat(MedicalRecordService.getMedicalRecordList()).isEmpty();
    }

    @Test
    @DisplayName("Check if medical record is successfully removed from MedicalRecordRepository")
    public void testUserIsDeletedFromDB() throws MedicalRecordUsernameAlreadyExistsException {
        testMedicalRecordIsAddedToDB();
        MedicalRecordService.deleteMedicalRecordFromDB(USERNAME);

        assertTrue(MedicalRecordService.getMedicalRecordList().isEmpty());
    }

    @Test
    @DisplayName("Check if medical record is successfully added to MedicalRecord database")
    public void testMedicalRecordIsAddedToDB() throws MedicalRecordUsernameAlreadyExistsException {

        MedicalRecordService.addMedicalRecord(USERNAME, ANSWER_FIRST_QUESTION, ANSWER_SECOND_QUESTION, ANSWER_THIRD_QUESTION, ANSWER_FOURTH_QUESTION, VACCINATED);

        assertThat(MedicalRecordService.getMedicalRecordList()).isNotEmpty();
        assertThat(MedicalRecordService.getMedicalRecordList()).size().isEqualTo(1);
        MedicalRecord medicalRecordService = MedicalRecordService.getMedicalRecordList().get(0);
        assertThat(medicalRecordService).isNotNull();

        assertAll("Should check all fields of a medical record",
                () -> assertEquals(USERNAME, medicalRecordService.getUsername()),
                () -> assertEquals(ANSWER_FIRST_QUESTION, medicalRecordService.getAnswerFirstQuestion()),
                () -> assertEquals(ANSWER_SECOND_QUESTION, medicalRecordService.getAnswerSecondQuestion()),
                () -> assertEquals(ANSWER_THIRD_QUESTION, medicalRecordService.getAnswerThirdQuestion()),
                () -> assertEquals(ANSWER_FOURTH_QUESTION, medicalRecordService.getAnswerFourthQuestion()),
                () -> assertEquals(VACCINATED, medicalRecordService.getVaccinated()));
    }

    @Test
    @DisplayName("Multiple medical records can not be created by the same user")
    public void testUsernameAlreadyExists() {
        assertThrows(MedicalRecordUsernameAlreadyExistsException.class, () -> {
            testMedicalRecordIsAddedToDB();
            MedicalRecordService.checkMedicalRecordUsernameAlreadyExists(USERNAME);
        });
    }

    @Test
    @DisplayName("Check if the user completed a medical record")
    public void testMedicalRecordCompleted() throws MedicalRecordUsernameAlreadyExistsException {
        testMedicalRecordIsAddedToDB();
        assertTrue(MedicalRecordService.isMedicalRecordCompleted(USERNAME));

        MedicalRecordService.deleteMedicalRecordFromDB(USERNAME);
        assertFalse(MedicalRecordService.isMedicalRecordCompleted(USERNAME));
    }
}