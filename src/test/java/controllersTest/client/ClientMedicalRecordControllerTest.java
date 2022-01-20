package controllersTest.client;

import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WeakPasswordException;
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
import services.FileSystemService;
import services.MedicalRecordService;
import services.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class ClientMedicalRecordControllerTest {

    @AfterAll
    static void cleanUpStages() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @BeforeEach
    public void setUpDB() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-medical_record_database";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        MedicalRecordService.initDatabase();
    }

    @AfterEach
    public void tearDown() {
        UserService.getDatabase().close();
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
    @DisplayName("A patient can not create his medical record because at least one field is empty")
    public void testMedicalRecordFieldsAreEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {

        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addMedicalRecordButton");

        robot.clickOn("#choiceBoxFirstQuestion");
        robot.clickOn("No");
        robot.clickOn("#choiceBoxSecondQuestion");
        robot.clickOn("Yes");
        robot.clickOn("#choiceBoxThirdQuestion");
        robot.clickOn("No");
        robot.clickOn("#choiceBoxFourthQuestion");
        robot.clickOn("Yes");
        robot.clickOn("#vaccinatedComboBox");
        robot.clickOn("Vaccinated");

        robot.clickOn("#submitRecordButton");
        Assertions.assertThat(robot.lookup("#medicalRecordMessage").queryText()).hasText("Please complete all fields!");
        robot.clickOn("#backButton");
    }

    @Test
    @DisplayName("A patient view his medical record if he has one or make a new one")
    public void testMedicalRecordAlreadyDefined(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException, MedicalRecordUsernameAlreadyExistsException {

        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        MedicalRecordService.addMedicalRecord("PC20000", "Yes", "Yes", "No", "No", "Vaccinated");
        assertThat(MedicalRecordService.getMedicalRecordList().size()).isEqualTo(1);

        robot.clickOn("#addMedicalRecordButton");
        robot.clickOn("Cancel");
        robot.clickOn("#backButton");
    }

}
