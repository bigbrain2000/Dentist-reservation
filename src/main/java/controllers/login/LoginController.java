package controllers.login;

import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WrongPasswordException;
import exceptions.username.UsernameDoesNotExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button minimizeField;

    @FXML
    private Button closeField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button LoginButton;

    @FXML
    private Button RegisterButton;

    @FXML
    private Text registrationMessage;

    @FXML
    private ImageView visiblePasswordImage;

    private static final User user = new User();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tooltip tooltip = createTooltipForPassword(visiblePasswordImage);

        if (!Objects.equals(user.getPassword(), ""))
            visiblePasswordImage.setOnMouseClicked(e -> tooltip.setText(passwordField.getText()));
    }

    @NotNull
    private Tooltip createTooltipForPassword(ImageView image) {
        Tooltip tooltip = new Tooltip();
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setAutoHide(false);
        tooltip.setPrefHeight(50);
        tooltip.setPrefWidth(130);
        tooltip.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
        setTooltip(image, tooltip);

        return tooltip;
    }

    private static void setTooltip(Node node, Tooltip tooltip) {
        Tooltip.install(node, tooltip);
    }

    @FXML
    private void handleLoginAction(javafx.event.ActionEvent event) {
        try {
            setUserDataFromDB();

            int aux = UserService.loginUser(user.getUsername(), user.getPassword());
            FXMLLoader Loader = new FXMLLoader();
            if (aux == 1) {
                Loader.setLocation(getClass().getClassLoader().getResource("clientFXML/client_page.fxml"));
                clearFields();
            } else if (aux == 2) {
                Loader.setLocation(getClass().getClassLoader().getResource("dentistFXML/dentist_page.fxml"));
                clearFields();
            }
            Parent viewUserLogin = Loader.load();
            Scene loginScene = new Scene(viewUserLogin, 1098, 512);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();

        } catch (FieldNotCompletedException | UsernameDoesNotExistsException e) {
            registrationMessage.setText(e.getMessage());
        } catch (WrongPasswordException e) {
            registrationMessage.setText(e.getMessage());
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUserDataFromDB() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(UserService.getUserFirstName(username));
        user.setSecondName(UserService.getUserSecondName(username));
        user.setPhoneNumber(UserService.getUserPhoneNumber(username));
        user.setAddress(UserService.getUserAddress(username));
        user.setRole(UserService.getUserRole(username));
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void goBackToRegistration(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("registerFXML/user_registration.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 655, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void minimizeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    private void closeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    @NotNull
    @Contract(" -> new")
    public static User getLoggedUser() {
        return new User(user.getUsername(), user.getPassword(), user.getFirstName(), user.getSecondName(), user.getPhoneNumber(), user.getAddress(), user.getRole());
    }
}
