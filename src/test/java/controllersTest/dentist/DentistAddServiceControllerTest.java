package controllersTest.dentist;

import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WeakPasswordException;
import exceptions.username.UsernameAlreadyExistsException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Service;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import services.DentistService;
import services.FileSystemService;
import services.UserService;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Objects;

@ExtendWith(ApplicationExtension.class)
public class DentistAddServiceControllerTest {

    private final String SERVICE_NAME = "Extraction";
    private final float SERVICE_PRICE = 20.20f;
    private final String EMPTY_FIELD = "";

    @AfterAll
    static void cleanUpStages() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @BeforeEach
    public void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-dentist_service_database";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        DentistService.initDatabase();
    }

    @AfterEach
    public void tearDown() {
        UserService.getDatabase().close();
        DentistService.getDatabase().close();
    }

    @Start
    public void start(@NotNull Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("loginFXML/user_login.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("A dentist adds a new service")
    public void testAddServices(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {

        Service service = new Service();
        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");

        assertThat(UserService.getUsers().size()).isEqualTo(1);
        assertThat(DentistService.getServiceList().size()).isEqualTo(0);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addServicesButton");
        robot.clickOn("#serviceNameField");
        robot.write(SERVICE_NAME);
        robot.clickOn("#priceField");
        robot.write(String.valueOf(SERVICE_PRICE));
        robot.clickOn("#createServiceButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Service successfully added !");
        assertAll("Service fields should be empty after a successful adding",
                () -> assertNull(service.getName()),
                () -> assertThat(String.valueOf(service.getPrice())).isEqualTo("0.0"));

        robot.clickOn("#backButton");
    }

    @Test
    @DisplayName("A dentist tries to add a new service but fails because service name field is empty")
    public void testServiceNameFieldIsEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {

        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");

        assertThat(UserService.getUsers().size()).isEqualTo(1);
        assertThat(DentistService.getServiceList().size()).isEqualTo(0);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addServicesButton");
        robot.clickOn("#serviceNameField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#priceField");
        robot.write(String.valueOf(SERVICE_PRICE));
        robot.clickOn("#createServiceButton");


        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please complete all fields!");
        assertThat(DentistService.getServiceList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("A dentist tries to add a new service but fails because service price field is empty")
    public void testServicePriceFieldIsWrongOrEmpty(@NotNull FxRobot robot) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {

        UserService.addUser("DOODS2022", "Helicopter123", "Roncea", "Marius-Alexandru", "1234567890", "Caracal", "Dentist");

        assertThat(UserService.getUsers().size()).isEqualTo(1);
        assertThat(DentistService.getServiceList().size()).isEqualTo(0);

        robot.clickOn("#usernameField");
        robot.write("DOODS2022");
        robot.clickOn("#passwordField");
        robot.write("Helicopter123");
        robot.clickOn("#LoginButton");

        robot.clickOn("#addServicesButton");
        robot.clickOn("#serviceNameField");
        robot.write(SERVICE_NAME);
        robot.clickOn("#priceField");
        robot.write(EMPTY_FIELD);
        robot.clickOn("#createServiceButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Incorrect price !");
        assertThat(DentistService.getServiceList().size()).isEqualTo(0);
    }

}
