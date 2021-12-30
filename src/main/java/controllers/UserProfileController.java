package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.User;
import org.jetbrains.annotations.NotNull;
import services.UserService;
import java.io.IOException;
import java.util.Objects;


public class UserProfileController extends DentistPageAbstract{

    @FXML
    private TextField usernameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField roleField;

    @FXML
    private StackPane stackPane;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button backButton;

    //user data from authentication
    private  String username = LoginController.getUsername();

    public void initialize() {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(username, user.getUsername())){
                setTextFields(user);
                setTextFieldsNotEditable();
            }
    }

    private void setTextFieldsNotEditable() {
        usernameField.setEditable(false);
        firstNameField.setEditable(false);
        secondNameField.setEditable(false);
        addressField.setEditable(false);
        phoneNumberField.setEditable(false);
        roleField.setEditable(false);
    }

    private void setTextFields(@NotNull User user) {
        usernameField.setText(user.getUsername());
        firstNameField.setText(user.getFirstName());
        secondNameField.setText(user.getSecondName());
        addressField.setText(user.getAddress());
        phoneNumberField.setText(user.getPhoneNumber());
        roleField.setText(user.getRole());
    }

    private void loadRegisterFXML(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("user_registration.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 650, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void deleteAccount(javafx.event.ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setHeaderText("Are you sure you want to delete this account? ");

        if(alert.showAndWait().get() == ButtonType.OK) {
            deleteUserFromDB(username);
            loadRegisterFXML(event);
        }
        else
            alert.close();
    }

    private void deleteUserFromDB(String username) {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(username, user.getUsername()))
                UserService.getUsers().remove(user);
    }
}