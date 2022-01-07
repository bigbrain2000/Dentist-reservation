package controllers.client;

import controllers.login.LoginController;
import exceptions.fields.FieldNotCompletedException;
import exceptions.username.MedicalRecordUsernameAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientMedicalRecordController extends ClientPageAbstract implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBoxFirstQuestion;

    @FXML
    private ChoiceBox<String> choiceBoxSecondQuestion;

    @FXML
    private ChoiceBox<String> choiceBoxThirdQuestion;

    @FXML
    private ChoiceBox<String> choiceBoxFourthQuestion;

    @FXML
    private ComboBox<String> vaccinatedComboBox;

    @FXML
    private Button submitRecordButton;

    @FXML
    private Button backButton;

    @FXML
    private Text medicalRecordMessage;

    private final String[] CORONA = {"Vaccinated", "Unvaccinated"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vaccinatedComboBox.getItems().addAll(CORONA);

        choiceBoxFirstQuestion.getItems().addAll("Yes", "No");
        choiceBoxSecondQuestion.getItems().addAll("Yes", "No");
        choiceBoxThirdQuestion.getItems().addAll("Yes", "No");
        choiceBoxFourthQuestion.getItems().addAll("Yes", "No");

        deleteMedicalRecordBasedOnOptions();
    }

    private void deleteMedicalRecordBasedOnOptions() {
        if(LoginController.setValueBasedOnCompletedMedicalRecord() == 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete medical record");
            alert.setHeaderText("You already have a medical record!\nPress OK if you want to delete this and make a new one." +
                    "\nPress CANCEL if you don`t want to change the current medical record.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    LoginController.deleteMedicalRecordFromDB();
                    medicalRecordMessage.setText("Create a new medical record.");
                    clearFields();
                } else {
                    alert.close();
                    LoginController.showDataForExistingMedicalRecord(choiceBoxFirstQuestion, choiceBoxSecondQuestion, choiceBoxThirdQuestion, choiceBoxFourthQuestion, vaccinatedComboBox);
                }
            });
        }
    }

    private void clearFields() {
        choiceBoxFirstQuestion.setValue(null);
        choiceBoxSecondQuestion.setValue(null);
        choiceBoxThirdQuestion.setValue(null);
        choiceBoxFourthQuestion.setValue(null);
        vaccinatedComboBox.setValue(null);
    }

    @FXML
    private void createNewMedicalRecord() {
        try{
            insertMedicalRecordIntoDB();
            clearFields();
            medicalRecordMessage.setText("Medical record successfully added!");
        } catch (FieldNotCompletedException | MedicalRecordUsernameAlreadyExistsException e) {
            medicalRecordMessage.setText(e.getMessage());
        }
    }

    private void insertMedicalRecordIntoDB() throws FieldNotCompletedException, MedicalRecordUsernameAlreadyExistsException {
        checkIfFieldsAreCompleted();

        LoginController.insertMedicalRecordInDB(choiceBoxFirstQuestion.getValue(), choiceBoxSecondQuestion.getValue(), choiceBoxThirdQuestion.getValue(),
                choiceBoxFourthQuestion.getValue(), vaccinatedComboBox.getValue());
    }

    private void checkIfFieldsAreCompleted() throws FieldNotCompletedException {
        if(choiceBoxFirstQuestion.getValue() == null || choiceBoxSecondQuestion.getValue() == null || choiceBoxThirdQuestion.getValue() == null
                || choiceBoxFourthQuestion.getValue() == null || vaccinatedComboBox.getValue() == null)
            throw  new FieldNotCompletedException();
    }
}
