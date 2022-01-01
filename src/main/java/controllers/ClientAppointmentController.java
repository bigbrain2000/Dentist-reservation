package controllers;

import exceptions.AppointmentUsernameAlreadyExistsException;
import exceptions.FieldNotCompletedException;
import exceptions.IncorrectDateException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.User;
import services.AppointmentService;
import services.UserService;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

public class ClientAppointmentController extends  ClientPageAbstract{

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private DatePicker dateField;

    @FXML
    private Button backButton;

    @FXML
    private Button createAppointmentButton;

    @FXML
    private CheckBox medicalRecordCheckField;

    @FXML
    private Text appointmentMessage;

    @FXML
    private ChoiceBox<String> dentistNameChoiceBox;

    //user data from authentication
    private String username = LoginController.getUsername();
    private String firstName = LoginController.getUserFirstName();
    private String secondName = LoginController.getUserSecondName();

    private ArrayList<String> dentistFirstNameList =  new ArrayList<>();

    public void initialize() {
        setPredefinedFields();
        dentistNameChoiceBox.getItems().addAll(getDentistsFirstNameList());  //add in the ChoiceBox first name of all Dentists
    }

    private ArrayList<String>  getDentistsFirstNameList() {
        for(User user : UserService.getUsers().find()) {
            if (Objects.equals("Dentist", user.getRole())) {
                dentistFirstNameList.add(user.getFirstName());
            }
        }

        return dentistFirstNameList;
    }

    @FXML
    private void createAppointment(ActionEvent event) {
        try{
            insertAppointmentIntoDB();
            appointmentMessage.setText("Appointment successfully added!");
            clearFields();
        }catch (AppointmentUsernameAlreadyExistsException | FieldNotCompletedException| IncorrectDateException e) {
            appointmentMessage.setText(e.getMessage());
        }
    }

    private void insertAppointmentIntoDB() throws FieldNotCompletedException, AppointmentUsernameAlreadyExistsException, IncorrectDateException {
        checkIfFieldsAreCompleted();

        AppointmentService.addAppointment(username, firstNameField.getText(), secondNameField.getText(),
                dentistNameChoiceBox.getValue(), Date.valueOf(dateField.getValue()), medicalRecordCheckField.isSelected());
    }

    private void checkIfFieldsAreCompleted() throws FieldNotCompletedException {
        if(dateField.getValue() == null || dentistNameChoiceBox.getValue() == null || !medicalRecordCheckField.isSelected())
            throw new FieldNotCompletedException();
    }

    private void setPredefinedFields() {
        firstNameField.setText(firstName);
        secondNameField.setText(secondName);

        firstNameField.setEditable(false);
        secondNameField.setEditable(false);
    }

    private void clearFields() {
        firstNameField.setText(null);
        secondNameField.setText(null);
        dentistNameChoiceBox.setValue(null);
        medicalRecordCheckField.setSelected(false);
    }
}
