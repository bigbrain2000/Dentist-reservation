package servicesTests;

import exceptions.date.IncorrectDateException;
import exceptions.username.AppointmentUsernameAlreadyExistsException;
import model.Appointment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import services.AppointmentService;
import services.FileSystemService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest {

    private final String USERNAME = "DOODS2022";
    private final String FIRST_NAME = "Roncea";
    private final String SECOND_NAME = "Marius-Alexandru";
    private final String DENTIST_NAME = "Ionescu";
    private final String SERVICE_NAME = "Extraction";
    private final float SERVICE_PRICE = 40.3f;
    private final Date APPOINTMENT_DATE = new java.util.Date();
    private final boolean CHECK_MEDICAL_RECORD = true;

    @BeforeEach
    @DisplayName("Check if AppointmentServiceRepository path is correct created and database is initialized")
    public void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-appointment_service_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        AppointmentService.initDatabase();
    }

    @AfterEach
    @DisplayName("Check if AppointmentService database is closed after every test")
    public void tearDown() {
        AppointmentService.getDatabase().close();
    }

    @Test
    @DisplayName("Check if AppointmentService database is initialized and there are no services available")
    public void testDentistServiceRepositoryIsInitializedAndNoServiceIsPersisted() {
        assertThat(AppointmentService.getAppointmentList()).isNotNull();
        assertThat(AppointmentService.getAppointmentList()).isEmpty();
    }

    @Test
    @DisplayName("Check if appointment is successfully added to AppointmentService")
    public void testAppointmentIsAddedToDB() throws IncorrectDateException, AppointmentUsernameAlreadyExistsException {

        AppointmentService.addAppointment(USERNAME, FIRST_NAME, SECOND_NAME, SERVICE_NAME, SERVICE_PRICE, APPOINTMENT_DATE, DENTIST_NAME, CHECK_MEDICAL_RECORD);

        assertThat(AppointmentService.getAppointmentList()).isNotEmpty();
        assertThat(AppointmentService.getAppointmentList()).size().isEqualTo(1);
        Appointment appointment = AppointmentService.getAppointmentList().get(0);
        assertThat(appointment).isNotNull();

        assertAll("Should check all fields of a dentist service",
                () -> assertEquals(USERNAME, appointment.getUsername()),
                () -> assertEquals(FIRST_NAME, appointment.getFirstName()),
                () -> assertEquals(SECOND_NAME, appointment.getSecondName()),
                () -> assertEquals(SERVICE_NAME, appointment.getServiceName()),
                () -> assertEquals(SERVICE_PRICE, appointment.getServicePrice()),
                () -> assertEquals(APPOINTMENT_DATE, appointment.getAppointmentDate()),
                () -> assertEquals(DENTIST_NAME, appointment.getDentistName()),
                () -> assertEquals(CHECK_MEDICAL_RECORD, appointment.isCheckMedicalRecord())
        );
    }

    @Test
    @DisplayName("Multiple appointments can not be made by the same user")
    public void testUsernameAlreadyExists() {
        assertThrows(AppointmentUsernameAlreadyExistsException.class, () -> {
            testAppointmentIsAddedToDB();
            AppointmentService.checkUsernameAlreadyExists(USERNAME);
        });
    }

    @Test
    @DisplayName("Check if the date is valid")
    public void testValidDate() {
        assertThrows(IncorrectDateException.class, () -> {
            AppointmentService.addAppointment(USERNAME, FIRST_NAME, SECOND_NAME, SERVICE_NAME, SERVICE_PRICE, Date.from(LocalDate.now().minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()), DENTIST_NAME, CHECK_MEDICAL_RECORD);
            AppointmentService.checkValidDate(APPOINTMENT_DATE);
        });
    }

}
