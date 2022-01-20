package controllersTest.dentist;

import exceptions.date.IncorrectDateException;
import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WeakPasswordException;
import exceptions.username.AppointmentUsernameAlreadyExistsException;
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
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import services.AppointmentService;
import services.FileSystemService;
import services.MedicalRecordService;
import services.UserService;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(ApplicationExtension.class)
public class DentistViewMedicalRecordControllerTest {

    private final Date APPOINTMENT_DATE = new java.util.Date();

    @AfterAll
    static void cleanUpStages() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @BeforeEach
    public void setUpDB() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-medical_record_database";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        AppointmentService.initDatabase();
        MedicalRecordService.initDatabase();
    }

    @AfterEach
    public void tearDown() {
        UserService.getDatabase().close();
        AppointmentService.getDatabase().close();
        MedicalRecordService.getDatabase().close();
    }

    @Start
    public void start(@NotNull Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("loginFXML/user_login.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("A dentist can view the medical record for each patient")
    public void testViewMedicalRecordForEachPatient(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException, MedicalRecordUsernameAlreadyExistsException, IncorrectDateException, AppointmentUsernameAlreadyExistsException {

        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(2);

        MedicalRecordService.addMedicalRecord("PC20000", "No", "No", "No", "No", "Yes");
        assertThat(MedicalRecordService.getMedicalRecordList().size()).isEqualTo(1);


        AppointmentService.addAppointment("PC20000", "Popescu", "Andrei", "Extraction", 44.2f, APPOINTMENT_DATE, "DOODS2022", true);
        assertThat(AppointmentService.getAppointmentList().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#viewAppointmentsButton");
        robot.clickOn("Popescu");
        robot.clickOn("#viewMedicalRecordButton");
        robot.clickOn("#backButton");
    }

    @Test
    @DisplayName("A dentist can not view the medical record for patient because he does not select any appointment")
    public void testViewMedicalForEmptyPatientSelected(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException, MedicalRecordUsernameAlreadyExistsException, IncorrectDateException, AppointmentUsernameAlreadyExistsException {

        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(2);

        MedicalRecordService.addMedicalRecord("PC20000", "No", "No", "No", "No", "Yes");
        assertThat(MedicalRecordService.getMedicalRecordList().size()).isEqualTo(1);


        AppointmentService.addAppointment("PC20000", "Popescu", "Andrei", "Extraction", 44.2f, APPOINTMENT_DATE, "DOODS2022", true);
        assertThat(AppointmentService.getAppointmentList().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#viewAppointmentsButton");
        robot.clickOn("");
        robot.clickOn("#viewMedicalRecordButton");

        Assertions.assertThat(robot.lookup("#viewMedicalRecordMessage").queryText()).hasText("Please select an appointment!");
        robot.clickOn("#backButton");
    }
}
