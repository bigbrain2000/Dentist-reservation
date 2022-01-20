package controllersTest.dentist;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import services.FileSystemService;
import services.UserService;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Objects;


@ExtendWith(ApplicationExtension.class)
public class DentistAddingControllerTest {

    private final String USERNAME = "PC20000";
    private final String PASSWORD = "Romania123";
    private final String FIRST_NAME = "Popescu";
    private final String SECOND_NAME = "Andrei";
    private final String PHONE_NUMBER = "0987654321";
    private final String ADDRESS = "Timisoara";
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
    @DisplayName("A dentist adds a new dentist")
    public void testAddDentists(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        User user = new User();
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Account created successfully!");
        assertThat(UserService.getUsers().size()).isEqualTo(2);


        assertAll("Register fields should be empty after an account was created",
                () -> org.assertj.core.api.Assertions.assertThat(user.getUsername()).isEqualTo(null),
                () -> org.assertj.core.api.Assertions.assertThat(user.getPassword()).isEqualTo(null),
                () -> org.assertj.core.api.Assertions.assertThat(user.getAddress()).isEqualTo(null),
                () -> org.assertj.core.api.Assertions.assertThat(user.getFirstName()).isEqualTo(null),
                () -> org.assertj.core.api.Assertions.assertThat(user.getSecondName()).isEqualTo(null),
                () -> org.assertj.core.api.Assertions.assertThat(user.getPhoneNumber()).isEqualTo(null)
        );

        robot.clickOn("#backButton");
        robot.clickOn("#logoutButton");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#LoginButton");
    }

    @Test
    @DisplayName("A dentist adds a new dentist but can not because that username is already taken")
    public void testUsernameAlreadyExists(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("An account with the username DOODS2022 already exists!");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because username field is empty")
    public void testUsernameFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because password field is empty")
    public void testPasswordFieldDoesNotContainAnUpperCase(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write("romania123");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Password does not contain at least one upper case !");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because password field is empty")
    public void testPasswordFieldDoesNotContainAtLeastOneDigit(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write("RomaniaIarna");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Password does not contain at least one digit !");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because password field is empty")
    public void testPasswordFieldDoesNotContainAtLeast8Characters(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write("Roma1");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Password does not contain at least 8 characters !");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because password field is empty")
    public void testPasswordFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because first name field is empty")
    public void testFirstNameFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because second name field is empty")
    public void testSecondNameFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because phone number field is empty")
    public void testPhoneNumberFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#addressField");
        robot.write(ADDRESS);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("A dentist tries to add a new dentist but can not because address field is empty")
    public void testAddressFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addDentists");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");
        robot.clickOn("#informationalLogo");

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#informationalLogo");
        robot.clickOn("#firstNameField");
        robot.write(FIRST_NAME);
        robot.clickOn("#secondNameField");
        robot.write(SECOND_NAME);
        robot.clickOn("#phoneNumberField");
        robot.write(PHONE_NUMBER);
        robot.clickOn("#addressField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#createAccountButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }
}