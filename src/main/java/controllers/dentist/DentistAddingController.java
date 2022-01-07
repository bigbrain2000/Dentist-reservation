package controllers.dentist;

import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WeakPasswordException;
import exceptions.username.UsernameAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import services.UserService;
import java.net.URL;
import java.util.ResourceBundle;

public class DentistAddingController extends DentistPageAbstract implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

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
    private Button createAccountButton;

    @FXML
    private Button backButton;

    @FXML
    private Text registrationMessage;

    @FXML
    private Label passwordNote;

    @FXML
    private ImageView informationalLogo;

    private int counter;

    private void increaseCounter(){ counter++;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPredefinedRoleAsDentist();
        createTooltipForPassword();
        setPasswordNoteVisibility(false);
    }

    private void setPredefinedRoleAsDentist() {
        roleField.setText("Dentist");
        roleField.setEditable(false);
    }

    private void createTooltipForPassword() {
        Tooltip tooltip = new Tooltip("Password details");
        tooltip.setPrefHeight(50);
        tooltip.setPrefWidth(130);
        tooltip.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
        setTooltip(informationalLogo, tooltip);
    }

    private static void setTooltip(Node node, Tooltip tooltip) {
        Tooltip.install(node, tooltip);
    }

    private void setPasswordNoteVisibility(boolean b) {
        passwordNote.setVisible(b);
    }

    @FXML
    private void showPasswordNote() {
        informationalLogo.setOnMouseClicked(e -> {
            increaseCounter();
            setPasswordNoteVisibility(counter % 2 != 0);
        });
    }

    @FXML
    private void createNewDentist() {
        try {
            InsertDentistDataIntoDB();
            clearFields();
            increaseCounter();
            registrationMessage.setText("Account created successfully!");

        } catch (FieldNotCompletedException | UsernameAlreadyExistsException e) {
            setPasswordNoteVisibility(true);
            increaseCounter();
            registrationMessage.setText(e.getMessage());

        } catch (WeakPasswordException e) {
            setPasswordNoteVisibility(true);
            increaseCounter();
            registrationMessage.setText(e.getMessage());
            passwordField.clear();
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        secondNameField.clear();
        phoneNumberField.clear();
        addressField.clear();
    }

    private void InsertDentistDataIntoDB() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        UserService.addUser(usernameField.getText(), passwordField.getText(),
                firstNameField.getText(), secondNameField.getText(), phoneNumberField.getText(),
                addressField.getText(), "Dentist");
    }
}