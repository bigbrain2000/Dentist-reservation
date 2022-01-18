package controllers.client;

import controllers.login.LoginController;
import exceptions.username.MedicalRecordNotCompletedException;
import exceptions.username.AppointmentUsernameAlreadyExistsException;
import exceptions.fields.FieldNotCompletedException;
import exceptions.date.IncorrectDateException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.Service;
import model.User;
import org.jetbrains.annotations.NotNull;
import services.AppointmentService;
import services.DentistService;
import services.MedicalRecordService;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientAppointmentController implements Initializable, ClientPageInterface {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private ChoiceBox<String> dentistNameChoiceBox;

    @FXML
    private TextField dentistServicePriceField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ChoiceBox<String> dentistServicesChoiceBox;

    @FXML
    private CheckBox medicalRecordCheckField;

    @FXML
    private Text appointmentMessage;

    @FXML
    private Button backButton;

    @FXML
    private Button createAppointmentButton;

    private static final User loggedUser = LoginController.getLoggedUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFieldsForAppointmentBasedOnUserName(firstNameField, secondNameField);

        dentistNameChoiceBox.getItems().addAll(UserService.getDentistsFirstNameList());  //add first name of all Dentists in the ChoiceBox
        dentistServicesChoiceBox.getItems().addAll(DentistService.getDentistServiceNameList()); //add all service names in the ChoiceBox

        dentistServicesChoiceBox.setOnAction(e -> {
            if (dentistServicesChoiceBox.getValue() != null)
                getDentistServicePriceBasedOnServiceName(dentistServicesChoiceBox, dentistServicePriceField);
        });
    }

    public static void getDentistServicePriceBasedOnServiceName(@NotNull ChoiceBox<String> dentistServiceName, @NotNull TextField dentistServicePrice) {
        for (Service service : DentistService.getDentistRepository().find())
            if (Objects.equals(service.getName(), dentistServiceName.getValue()))
                dentistServicePrice.setText(String.valueOf(service.getPrice()));
    }

    private void setFieldsForAppointmentBasedOnUserName(@NotNull TextField firstNameField, @NotNull TextField secondNameField) {
        firstNameField.setText(UserService.getUserFirstName(loggedUser.getUsername()));
        secondNameField.setText(UserService.getUserSecondName(loggedUser.getUsername()));

        firstNameField.setEditable(false);
        secondNameField.setEditable(false);
    }

    @FXML
    private void createAppointment(ActionEvent event) {
        try {
            insertAppointmentIntoDBIfMedicalRecord();
            appointmentMessage.setText("Appointment successfully added!");
            clearFields();

        } catch (AppointmentUsernameAlreadyExistsException | FieldNotCompletedException | IncorrectDateException | MedicalRecordNotCompletedException e) {
            appointmentMessage.setText(e.getMessage());
        }
    }

    private void insertAppointmentIntoDBIfMedicalRecord() throws FieldNotCompletedException, AppointmentUsernameAlreadyExistsException, IncorrectDateException, MedicalRecordNotCompletedException {
        checkIfFieldsAreCompleted();

        //is completed
        if (MedicalRecordService.isMedicalRecordCompleted(loggedUser.getUsername()))
            insertAppointmentInDB(firstNameField, secondNameField, dentistServicesChoiceBox, dentistServicePriceField, dateField, dentistNameChoiceBox, medicalRecordCheckField);

        //is not completed
        if (!MedicalRecordService.isMedicalRecordCompleted(loggedUser.getUsername()))
            throw new MedicalRecordNotCompletedException();
    }

    private void checkIfFieldsAreCompleted() throws FieldNotCompletedException {
        if (dentistServicesChoiceBox.getValue() == null || dateField.getValue() == null || dentistNameChoiceBox.getValue() == null || !medicalRecordCheckField.isSelected())
            throw new FieldNotCompletedException();
    }

    public static void insertAppointmentInDB(@NotNull TextField firstNameField, @NotNull TextField secondNameField, @NotNull ChoiceBox<String> serviceNameChoiceBox, @NotNull TextField servicePrice,
                                             @NotNull DatePicker dateField, @NotNull ChoiceBox<String> dentistNameChoiceBox, @NotNull CheckBox medicalRecordCheckField) throws IncorrectDateException, AppointmentUsernameAlreadyExistsException {

        AppointmentService.addAppointment(loggedUser.getUsername(), firstNameField.getText(), secondNameField.getText(), serviceNameChoiceBox.getValue(), Float.parseFloat(servicePrice.getText()),
                Date.valueOf(dateField.getValue()), dentistNameChoiceBox.getValue(), medicalRecordCheckField.isSelected());
    }

    private void clearFields() {
        dentistServicesChoiceBox.setValue(null);
        dentistServicePriceField.setText(null);
        dentistNameChoiceBox.setValue(null);
        dateField.setValue(null);
        medicalRecordCheckField.setSelected(false);
    }

    @Override
    public void goBackToClientPage(@NotNull ActionEvent event) throws IOException {
        ClientPageInterface.super.goBackToClientPage(event);
    }
}
