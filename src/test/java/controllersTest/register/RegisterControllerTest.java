package controllersTest.register;

import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WeakPasswordException;
import exceptions.username.UsernameAlreadyExistsException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class RegisterControllerTest {

    private final String USERNAME = "DOODS2022";
    private final String PASSWORD = "Helicopter123";
    private final String FIRST_NAME = "Roncea";
    private final String SECOND_NAME = "Marius-Alexandru";
    private final String PHONE_NUMBER = "1234567890";
    private final String ADDRESS = "Caracal";
    private final String ROLE = "Dentist";
    private final String EMPTY_FIELD = "";

    @AfterAll
    static void afterAll() throws TimeoutException {
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("registerFXML/user_registration.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration is successfully")
    public void testRegistrationIsSuccessful(@NotNull FxRobot robot) {
        User user = new User();

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField ");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Account created successfully!");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        assertAll("Register fields should be empty after an account was created",
                () -> assertThat(user.getUsername()).isEqualTo(EMPTY_FIELD),
                () -> assertThat(user.getPassword()).isEqualTo(EMPTY_FIELD),
                () -> assertThat(user.getAddress()).isEqualTo(EMPTY_FIELD),
                () -> assertThat(user.getFirstName()).isEqualTo(EMPTY_FIELD),
                () -> assertThat(user.getSecondName()).isEqualTo(EMPTY_FIELD),
                () -> assertThat(user.getPhoneNumber()).isEqualTo(EMPTY_FIELD)
        );

        robot.clickOn("#BackButton");
        robot.clickOn("#RegisterButton");
        robot.clickOn("#closeField");
    }


    @Test
    @DisplayName("FxRobot enters an existing username and the registration fails")
    public void testRegistrationIsNotSuccessful(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText(
                String.format("An account with the username %s already exists!", USERNAME)
        );

        robot.clickOn("#minimizeField");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because username field is not completed")
    public void testRegistrationUsernameFieldIsEmpty(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because password field is not completed")
    public void testRegistrationPasswordFieldIsEmpty(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because address field is not completed")
    public void testRegistrationAddressFieldIsEmpty(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#addressField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because first name field is not completed")
    public void testRegistrationFirstNameFieldIsEmpty(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because second name field is not completed")
    public void testRegistrationSecondNameFieldIsEmpty(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because phone field is not completed")
    public void testRegistrationPhoneFieldIsEmpty(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because password does not contains at least 8 characters")
    public void testRegistrationPasswordDoesNotContainsAtLeast8Characters(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write("Heli1");
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Password does not contain at least 8 characters !");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because password does not contain at least 1 digit")
    public void testRegistrationPasswordDoesNotContainsAtLeast1Digit(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write("Helicopter");
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Password does not contain at least one digit !");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the registration fails because password does not contain at least 1 upper case")
    public void testRegistrationPasswordDoesNotContainsAtLeast1UpperCase(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write("helicopter123");
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#role");
        robot.clickOn(ROLE);

        robot.clickOn("#RegisterButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Password does not contain at least one upper case !");
    }
}
