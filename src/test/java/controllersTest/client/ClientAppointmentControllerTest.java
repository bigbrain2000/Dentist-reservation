package controllersTest.client;

import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WeakPasswordException;
import exceptions.username.DentistServiceNameAlreadyExistsException;
import exceptions.username.MedicalRecordUsernameAlreadyExistsException;
import exceptions.username.UsernameAlreadyExistsException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import services.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class ClientAppointmentControllerTest {

    @AfterAll
    static void cleanUpStages() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @BeforeEach
    public void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-appointment_service_database";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        DentistService.initDatabase();
        MedicalRecordService.initDatabase();
        AppointmentService.initDatabase();
    }

    @AfterEach
    public void tearDown() {
        UserService.getDatabase().close();
        DentistService.getDatabase().close();
        MedicalRecordService.getDatabase().close();
        AppointmentService.getDatabase().close();
    }

    @Start
    public void start(@NotNull Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("loginFXML/user_login.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("A patient can not create an appointment because date field is empty")
    public void testDateFieldEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException, DentistServiceNameAlreadyExistsException, MedicalRecordUsernameAlreadyExistsException {

        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(2);

        DentistService.addDentistService("Extraction", 123);
        assertThat(DentistService.getServiceList().size()).isEqualTo(1);

        MedicalRecordService.addMedicalRecord("PC20000", "No", "No", "No", "Yes", "Vaccinated");
        assertThat(MedicalRecordService.getMedicalRecordList().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addAppointmentButton");
        robot.clickOn("#dentistServicesChoiceBox");
        robot.clickOn("Extraction");
        robot.clickOn("#dateField");
        robot.clickOn("#dentistNameChoiceBox");
        robot.clickOn("Roncea");
        robot.clickOn("#medicalRecordCheckField");
        robot.clickOn("#createAppointmentButton");
        assertThat(AppointmentService.getAppointmentList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("A patient can not create an appointment because service name field is empty")
    public void testServiceNameFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException, DentistServiceNameAlreadyExistsException, MedicalRecordUsernameAlreadyExistsException {

        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(2);

        DentistService.addDentistService("Extraction", 123);
        assertThat(DentistService.getServiceList().size()).isEqualTo(1);

        MedicalRecordService.addMedicalRecord("PC20000", "No", "No", "No", "Yes", "Vaccinated");
        assertThat(MedicalRecordService.getMedicalRecordList().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addAppointmentButton");
        robot.clickOn("#dentistServicesChoiceBox");
        robot.clickOn("#dateField");
        robot.clickOn("#dentistNameChoiceBox");
        robot.clickOn("Roncea");
        robot.clickOn("#medicalRecordCheckField");
        robot.clickOn("#createAppointmentButton");
        assertThat(AppointmentService.getAppointmentList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("A patient can not create an appointment because dentist name field is empty")
    public void testDentistNameFieldEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException, DentistServiceNameAlreadyExistsException, MedicalRecordUsernameAlreadyExistsException {

        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(2);

        DentistService.addDentistService("Extraction", 123);
        assertThat(DentistService.getServiceList().size()).isEqualTo(1);

        MedicalRecordService.addMedicalRecord("PC20000", "No", "No", "No", "Yes", "Vaccinated");
        assertThat(MedicalRecordService.getMedicalRecordList().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addAppointmentButton");
        robot.clickOn("#dentistServicesChoiceBox");
        robot.clickOn("Extraction");
        robot.clickOn("#dateField");
        robot.clickOn("#dentistNameChoiceBox");
        robot.clickOn("#medicalRecordCheckField");
        robot.clickOn("#createAppointmentButton");
        assertThat(AppointmentService.getAppointmentList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("A patient can not create an appointment because medical record is not completed")
    public void testMedicalRecordIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException, DentistServiceNameAlreadyExistsException, MedicalRecordUsernameAlreadyExistsException {

        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(2);

        DentistService.addDentistService("Extraction", 123);
        assertThat(DentistService.getServiceList().size()).isEqualTo(1);

        MedicalRecordService.addMedicalRecord("PC20000", "No", "No", "No", "Yes", "Vaccinated");
        assertThat(MedicalRecordService.getMedicalRecordList().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addAppointmentButton");
        robot.clickOn("#dentistServicesChoiceBox");
        robot.clickOn("Extraction");
        robot.clickOn("#dateField");
        robot.clickOn("#dentistNameChoiceBox");
        robot.clickOn("Roncea");
        robot.clickOn("#createAppointmentButton");
        assertThat(AppointmentService.getAppointmentList().size()).isEqualTo(0);
    }
}
