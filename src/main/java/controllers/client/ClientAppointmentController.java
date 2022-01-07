package controllers.client;

import controllers.login.LoginController;
import exceptions.MedicalRecordNotCompletedException;
import exceptions.username.AppointmentUsernameAlreadyExistsException;
import exceptions.FieldNotCompletedException;
import exceptions.IncorrectDateException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import services.DentistService;
import services.UserService;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientAppointmentController extends  ClientPageAbstract implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginController.setFieldsForAppointmentBasedOnUser(firstNameField, secondNameField);

        dentistNameChoiceBox.getItems().addAll(UserService.getDentistsFirstNameList());  //add in the ChoiceBox first name of all Dentists
        dentistServicesChoiceBox.getItems().addAll(DentistService.getDentistServiceNameList());

        dentistServicesChoiceBox.setOnAction(e -> {
            if(dentistServicesChoiceBox.getValue() != null)
               DentistService.getDentistServicePriceBasedOnName(dentistServicesChoiceBox, dentistServicePriceField);
        });
    }

    @FXML
    private void createAppointment(ActionEvent event) {
        try{
            insertAppointmentIntoDB();
            appointmentMessage.setText("Appointment successfully added!");
            clearFields();

        }catch (AppointmentUsernameAlreadyExistsException | FieldNotCompletedException | IncorrectDateException | MedicalRecordNotCompletedException e) {
            appointmentMessage.setText(e.getMessage());
        }
    }

    private void insertAppointmentIntoDB() throws FieldNotCompletedException, AppointmentUsernameAlreadyExistsException, IncorrectDateException, MedicalRecordNotCompletedException {
        checkIfFieldsAreCompleted();

        //is completed
        if(LoginController.setValueBasedOnCompletedMedicalRecord() == 1)
           LoginController.insertAppointmentInDB(firstNameField, secondNameField, dentistServicesChoiceBox, dentistServicePriceField, dateField, dentistNameChoiceBox, medicalRecordCheckField);

        //is not completed
        if(LoginController.setValueBasedOnCompletedMedicalRecord() == 2)
            throw new MedicalRecordNotCompletedException();
    }

    private void checkIfFieldsAreCompleted() throws FieldNotCompletedException {
        if(dentistServicesChoiceBox.getValue() == null || dateField.getValue() == null || dentistNameChoiceBox.getValue() == null || !medicalRecordCheckField.isSelected())
            throw new FieldNotCompletedException();
    }

    private void clearFields() {
        dentistServicesChoiceBox.setValue(null);
        dentistServicePriceField.setText(null);
        dentistNameChoiceBox.setValue(null);
        dateField.setValue(null);
        medicalRecordCheckField.setSelected(false);
    }
}
