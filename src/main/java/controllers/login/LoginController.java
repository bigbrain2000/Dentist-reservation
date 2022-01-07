package controllers.login;

import exceptions.*;
import exceptions.password.WrongPasswordException;
import exceptions.username.AppointmentUsernameAlreadyExistsException;
import exceptions.username.MedicalRecordUsernameAlreadyExistsException;
import exceptions.username.UsernameDoesNotExistsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import model.MedicalRecord;
import model.User;
import org.jetbrains.annotations.NotNull;
import services.AppointmentService;
import services.MedicalRecordService;
import services.UserService;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

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

    private static String username;

    @FXML
    private void handleLoginAction(javafx.event.ActionEvent event) {
        username = usernameField.getText();
        String password = passwordField.getText();

        try {
            int aux = UserService.loginUser(username, password);
            FXMLLoader Loader = new FXMLLoader();
            if (aux == 1) {
                Loader.setLocation(getClass().getClassLoader().getResource("client/client_page.fxml"));
                clearFields();
            } else if (aux == 2) {
                Loader.setLocation(getClass().getClassLoader().getResource("dentist/dentist_page.fxml"));
                clearFields();
            }
            Parent viewUserLogin = Loader.load();
            Scene loginScene = new Scene(viewUserLogin, 655, 500);
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

    private void clearFields(){
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void goBackToRegistration(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("register/user_registration.fxml"));
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


    //getters for data client

    public static String getUsername() {
        return username;
    }

    public static String getUserFirstName() {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(username, user.getUsername()))
                return user.getFirstName();

        return "";
    }

    private static String getUserSecondName() {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(username, user.getUsername()))
                return user.getSecondName();

        return "";
    }

    private static String getUserAddress() {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(username, user.getUsername()))
                return user.getAddress();

        return "";
    }

    private static String getUserPhoneNumber() {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(username, user.getUsername()))
                return user.getPhoneNumber();

        return "";
    }

    private static String getUserRole() {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(username, user.getUsername()))
                return user.getRole();

        return "";
    }


    //delete User method used in ClientProfileController and DentistProfileController
    public static void deleteUserFromDB() {
        UserService.deleteUser(username);
    }

    //setter fields for ClientProfileController and DentistProfileController
    public static void setTextFieldsForProfileBasedOnUsername(@NotNull TextField usernameField, @NotNull TextField firstNameField, @NotNull TextField secondNameField,
                                                              @NotNull TextField addressField, @NotNull TextField phoneNumberField, @NotNull TextField roleField) {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(username, user.getUsername())){
                LoginController.setTextFieldsForProfile(usernameField, firstNameField, secondNameField, addressField, phoneNumberField, roleField);

                setTextFieldsForProfileNotEditable(usernameField, firstNameField, secondNameField, addressField, phoneNumberField, roleField);
            }
    }

    public static void setTextFieldsForProfile(@NotNull TextField usernameField, @NotNull TextField firstNameField, @NotNull TextField secondNameField,
                                               @NotNull TextField addressField, @NotNull TextField phoneNumberField, @NotNull TextField roleField) {

        usernameField.setText(getUsername());
        firstNameField.setText(getUserFirstName());
        secondNameField.setText(getUserSecondName());
        addressField.setText(getUserAddress());
        phoneNumberField.setText(getUserPhoneNumber());
        roleField.setText(getUserRole());
    }

    public static void setTextFieldsForProfileNotEditable(@NotNull TextField usernameField, @NotNull TextField firstNameField, @NotNull TextField secondNameField,
                                                          @NotNull TextField addressField, @NotNull TextField phoneNumberField, @NotNull TextField roleField) {
        usernameField.setEditable(false);
        firstNameField.setEditable(false);
        secondNameField.setEditable(false);
        addressField.setEditable(false);
        phoneNumberField.setEditable(false);
        roleField.setEditable(false);
    }

    //insert Appointment, method used in ClientAppointmentController
    public static void insertAppointmentInDB(@NotNull TextField firstNameField, @NotNull TextField secondNameField,  @NotNull ChoiceBox<String> serviceNameChoiceBox, @NotNull TextField servicePrice,
                                             @NotNull DatePicker dateField, @NotNull ChoiceBox<String> dentistNameChoiceBox, @NotNull CheckBox medicalRecordCheckField) throws IncorrectDateException, AppointmentUsernameAlreadyExistsException {

        AppointmentService.addAppointment(username, firstNameField.getText(), secondNameField.getText(),serviceNameChoiceBox.getValue(), Float.parseFloat(servicePrice.getText()),
                Date.valueOf(dateField.getValue()),  dentistNameChoiceBox.getValue(),medicalRecordCheckField.isSelected());
    }

    //setter fields, method for ClientProfileController and DentistProfileController
    public static void setFieldsForAppointmentBasedOnUser(@NotNull TextField firstNameField, @NotNull TextField secondNameField) {
        firstNameField.setText(getUserFirstName());
        secondNameField.setText(getUserSecondName());

        firstNameField.setEditable(false);
        secondNameField.setEditable(false);
    }

    //insert Medical Record, method used in ClientMedicalRecordController
    public static void insertMedicalRecordInDB(String choiceBoxFirstQuestion, String choiceBoxSecondQuestion, String choiceBoxThirdQuestion,
                                               String choiceBoxFourthQuestion, String vaccinatedComboBox) throws MedicalRecordUsernameAlreadyExistsException {

        MedicalRecordService.addMedicalRecord(username, choiceBoxFirstQuestion, choiceBoxSecondQuestion,
                choiceBoxThirdQuestion, choiceBoxFourthQuestion, vaccinatedComboBox);
    }

    //delete Medical Record, method used in ClientMedicalRecordController
    public static void deleteMedicalRecordFromDB() {MedicalRecordService.deleteMedicalRecordFromDB(username);}

    //based on username data, it will be shown all his data in the fields
    public static void showDataForExistingMedicalRecord(@NotNull ChoiceBox<String> choiceBoxFirstQuestion, @NotNull ChoiceBox<String>  choiceBoxSecondQuestion,
                                                        @NotNull ChoiceBox<String>  choiceBoxThirdQuestion, @NotNull ChoiceBox<String>  choiceBoxFourthQuestion, @NotNull ComboBox<String>  vaccinatedComboBox) {

        for (MedicalRecord medicalRecord : MedicalRecordService.getMedicalRecordRepository().find())
            if (Objects.equals(username, medicalRecord.getUsername()))
                setFieldsForExistingMedicalRecord(medicalRecord, choiceBoxFirstQuestion, choiceBoxSecondQuestion, choiceBoxThirdQuestion, choiceBoxFourthQuestion, vaccinatedComboBox);
    }


    private static void setFieldsForExistingMedicalRecord(@NotNull MedicalRecord medicalRecord, @NotNull ChoiceBox<String>  choiceBoxFirstQuestion, @NotNull ChoiceBox<String>  choiceBoxSecondQuestion,
                                                          @NotNull ChoiceBox<String>  choiceBoxThirdQuestion, @NotNull ChoiceBox<String>  choiceBoxFourthQuestion, @NotNull ComboBox<String>  vaccinatedComboBox) {

        choiceBoxFirstQuestion.setValue(medicalRecord.getAnswerFirstQuestion());
        choiceBoxSecondQuestion.setValue(medicalRecord.getAnswerSecondQuestion());
        choiceBoxThirdQuestion.setValue(medicalRecord.getAnswerThirdQuestion());
        choiceBoxFourthQuestion.setValue(medicalRecord.getAnswerFourthQuestion());
        vaccinatedComboBox.setValue(medicalRecord.getVaccinated());
    }

    public static int setValueBasedOnCompletedMedicalRecord() {
        if(MedicalRecordService.isMedicalRecordCompleted(username))
            return 1;
        else
            return 2;
    }

    @NotNull
    public static ObservableList<Appointment> getAppointments()  {

        ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
        ObservableList<Appointment> appointmentObservableArrayList = FXCollections.observableArrayList();

        for (Appointment appointment : AppointmentService.getAppointmentRepository().find())
            if(Objects.equals(appointment.getUsername(), username))
                appointmentArrayList.add(appointment);

        appointmentObservableArrayList.addAll(appointmentArrayList);
        return appointmentObservableArrayList;
    }
}
