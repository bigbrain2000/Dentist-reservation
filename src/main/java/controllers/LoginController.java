package controllers;

import exceptions.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import services.UserService;
import java.io.IOException;

public class LoginController {

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
    public void handleLoginAction(javafx.event.ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            int aux = UserService.loginUser(username, password);
            FXMLLoader Loader = new FXMLLoader();

            if (aux == 1) {
                Loader.setLocation(getClass().getClassLoader().getResource("Patient.fxml"));
                clearFields();
            } else if (aux == 2) {
                Loader.setLocation(getClass().getClassLoader().getResource("dentist_Page.fxml"));
                clearFields();
            }

            Parent viewUserLogin = Loader.load();
            Scene loginScene = new Scene(viewUserLogin, 670, 500);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();

        } catch (FieldNotCompletedException e) {
            getRegistrationMessageForFieldNotCompletedException(e);
        } catch (UsernameDoesNotExistsException e) {
            getRegistrationMessageForUsernameDoesNotExistsException(e);
        } catch (WrongPasswordException e) {
            getRegistrationMessageForWrongPasswordException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearFields(){
        usernameField.clear();
        passwordField.clear();
    }

    private void getRegistrationMessageForWrongPasswordException(@NotNull WrongPasswordException e) {
        registrationMessage.setText(e.getMessage());
        passwordField.clear();
    }

    private void getRegistrationMessageForFieldNotCompletedException(@NotNull FieldNotCompletedException e) {
        registrationMessage.setText(e.getMessage());
    }

    private void getRegistrationMessageForUsernameDoesNotExistsException(@NotNull UsernameDoesNotExistsException e) {
        registrationMessage.setText(e.getMessage());
    }

    @FXML
    public void goBackToLogin(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("user_registration.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 655, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    public void minimizeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    public void closeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }
}
