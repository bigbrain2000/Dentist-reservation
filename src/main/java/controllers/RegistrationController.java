package controllers;

import exceptions.FieldNotCompletedException;
import exceptions.UsernameAlreadyExistsException;
import exceptions.WeakPasswordException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UserService;
import java.io.IOException;

public class RegistrationController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button minimizeField;

    @FXML
    private Button closeField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private ChoiceBox<String> role;

    @FXML
    private Button RegisterButton;

    @FXML
    private Button BackButton;

    @FXML
    private Text registrationMessage;

    private String[] roleTypes = {"Patient", "Dentist"};
    public void initialize() {
        role.getItems().addAll(roleTypes);
        role.setValue("Patient");
    }

    @FXML
    public void handleRegisterAction(javafx.event.ActionEvent login)  {
        try {
            InsertUserDataIntoDB();
            registrationMessage.setText("Account created successfully!");
            ClearAllFields();

        } catch (UsernameAlreadyExistsException e) {
            getRegistrationMessageForUsernameAlreadyExistsException(e);
        }catch (FieldNotCompletedException e) {
            getRegistrationMessageForFieldNotCompletedException(e);
        }catch (WeakPasswordException e) {
            getRegistrationMessageForWeakPasswordException(e);
        }
    }

    private void InsertUserDataIntoDB() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser(usernameField.getText(), passwordField.getText(),
                firstNameField.getText(), secondNameField.getText(), phoneNumberField.getText(),
                addressField.getText(), role.getValue());
    }

    private void ClearAllFields() {
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        secondNameField.clear();
        phoneNumberField.clear();
        addressField.clear();
    }

    private void getRegistrationMessageForUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        registrationMessage.setText(e.getMessage());
        passwordField.clear();
    }

    private void getRegistrationMessageForWeakPasswordException(WeakPasswordException e) {
        registrationMessage.setText(e.getMessage());
        passwordField.clear();
    }

    private void getRegistrationMessageForFieldNotCompletedException(FieldNotCompletedException e) {
        registrationMessage.setText(e.getMessage());
    }

    public void goBackToLogin(javafx.event.ActionEvent back) throws IOException {
        LoadFXML(back);
    }

    private void LoadFXML(ActionEvent back) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("user_login.fxml"));
        Parent viewUserlogin = Loader.load();
        Scene loginScene = new Scene(viewUserlogin, 650, 450);
        Stage window = (Stage) ((Node) back.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    public void minimizeWindow(javafx.event.ActionEvent min) {
        Stage window = (Stage) ((Node) min.getSource()).getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    public void closeWindow(javafx.event.ActionEvent close) {
        Stage window = (Stage) ((Node) close.getSource()).getScene().getWindow();
        window.close();
    }
}
