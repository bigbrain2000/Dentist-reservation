package controllers;

import exceptions.FieldNotCompletedException;
import exceptions.UsernameAlreadyExistsException;
import exceptions.WeakPasswordException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import services.UserService;
import java.io.IOException;

public class RegistrationController implements LoginPageInterface, PageOptionsInterface, FieldsOptionsInterface{
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

    private final String[] ROLETYPES = {"Patient", "Dentist"};
    public void initialize() {
        role.getItems().addAll(ROLETYPES);
        role.setValue("Patient");
    }

    @FXML
    public void handleRegisterAction()  {
        try {
            insertUserDataIntoDB();
            registrationMessage.setText("Account created successfully!");
            clearFields();

        } catch (UsernameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
            usernameField.clear();
        }catch (FieldNotCompletedException e) {
            registrationMessage.setText(e.getMessage());
        }catch (WeakPasswordException e) {
            registrationMessage.setText(e.getMessage());
            passwordField.clear();
        }
    }

    private void insertUserDataIntoDB() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser(usernameField.getText(), passwordField.getText(),
                firstNameField.getText(), secondNameField.getText(), phoneNumberField.getText(),
                addressField.getText(), role.getValue());
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        secondNameField.clear();
        phoneNumberField.clear();
        addressField.clear();
    }

    @FXML
    public void goBackToLogin(javafx.event.ActionEvent back) throws IOException {
        loadLoginFXML(back);
    }

    @FXML
    public void loadLoginFXML(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("user_login.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 656, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    public void minimizeWindow(@NotNull javafx.event.ActionEvent min) {
        Stage window = (Stage) ((Node) min.getSource()).getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    public void closeWindow(@NotNull javafx.event.ActionEvent close) {
        Stage window = (Stage) ((Node) close.getSource()).getScene().getWindow();
        window.close();
    }
}
