package controllersTest.client;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import services.FileSystemService;
import services.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class ClientProfileControllerTest {

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
    @DisplayName("A patient views his profile")
    public void testProfileIsVisible(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#viewProfileButton");
        robot.clickOn("#backButton");
        robot.clickOn("#logoutButton");
    }

    @Test
    @DisplayName("A patient deletes his account")
    public void testDeleteAccount(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#viewProfileButton");
        robot.clickOn("#deleteAccountButton");
        robot.clickOn("OK");

        assertThat(UserService.getUsers().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("A patient cancel the deletion of his account")
    public void testCancelDeleteAccount(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser("PC20000", "Romania123", "Popescu", "Andrei", "0987654321", "Timisoara", "Patient");
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write("PC20000");
        robot.clickOn("#passwordField");
        robot.write("Romania123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#viewProfileButton");
        robot.clickOn("#deleteAccountButton");
        robot.clickOn("Cancel");

        assertThat(UserService.getUsers().size()).isEqualTo(1);
    }
}
