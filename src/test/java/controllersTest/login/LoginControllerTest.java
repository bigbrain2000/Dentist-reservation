package controllersTest.login;


import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WeakPasswordException;
import exceptions.username.UsernameAlreadyExistsException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import services.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerTest {

    private final String USERNAME = "DOODS2022";
    private final String PASSWORD = "Helicopter123";
    private final String FIRST_NAME = "Roncea";
    private final String SECOND_NAME = "Marius-Alexandru";
    private final String PHONE_NUMBER = "1234567890";
    private final String ADDRESS = "Caracal";
    private final String ROLE_PATIENT = "Patient";
    private final String EMPTY_FIELD = "";

    @AfterAll
    static void cleanUpStages() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @BeforeEach
    public void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-users_database";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
    }

    @AfterEach
    public void tearDown() {
        UserService.getDatabase().close();
    }

    @Start
    public void start(@NotNull Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("loginFXML/user_login.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("FxRobot switch to register window")
    public void testLoginSwitchToRegisterWindow(@NotNull FxRobot robot) {
        robot.clickOn("#RegisterButton");
        robot.clickOn("#closeField");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the login is successfully for dentist")
    public void testLoginIsSuccessfulForDentist(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        String ROLE_DENTIST = "Dentist";
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE_DENTIST);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#visiblePasswordImage");
        robot.clickOn("#LoginButton");
        robot.clickOn("#logoutButton");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the login is successfully for patient")
    public void testLoginIsSuccessfulForPatient(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE_PATIENT);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#visiblePasswordImage");
        robot.clickOn("#LoginButton");
        robot.clickOn("#logoutButton");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the login fails because username field is not completed")
    public void testLoginUsernameFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE_PATIENT);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#LoginButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("An account with that username doesn't exists!");
    }


    @Test
    @DisplayName("FxRobot enters user`s data and the login fails because password field is not completed")
    public void testLoginPasswordFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE_PATIENT);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(EMPTY_FIELD);

        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#LoginButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Wrong password!");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the login fails because password field is not completed")
    public void testLoginUsernameDoesNotExists(@NotNull FxRobot robot) {
        assertThat(UserService.getUsers().size()).isEqualTo(0);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        assertThat(UserService.getUsers().size()).isEqualTo(0);

        robot.clickOn("#LoginButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("An account with that username doesn't exists!");
    }
}
