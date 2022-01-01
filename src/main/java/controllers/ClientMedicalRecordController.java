package controllers;

import exceptions.FieldNotCompletedException;
import exceptions.MedicalRecordUsernameAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.MedicalRecord;
import org.jetbrains.annotations.NotNull;
import services.MedicalRecordService;
import java.util.Objects;

public class ClientMedicalRecordController extends ClientPageAbstract{

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

    //user data from authentication
    private String username = LoginController.getUsername();

    private final String[] CORONA = {"Vaccinated", "Unvaccinated"};
    public void initialize() {
        vaccinatedComboBox.getItems().addAll(CORONA);

        choiceBoxFirstQuestion.getItems().addAll("Yes", "No");
        choiceBoxSecondQuestion.getItems().addAll("Yes", "No");
        choiceBoxThirdQuestion.getItems().addAll("Yes", "No");
        choiceBoxFourthQuestion.getItems().addAll("Yes", "No");

        deleteMedicalRecordBasedOnOptions();
    }

    private void deleteMedicalRecordBasedOnOptions() {
        if(MedicalRecordService.isMedicalRecordCompleted(username)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete medical record");
            alert.setHeaderText("You already have a medical record!\nPress OK if you want to delete this and make a new one." +
                    "\nPress CANCEL if you don`t want to change the current medical record.");

            if(alert.showAndWait().get() == ButtonType.OK) {
                deleteMedicalRecordFromDB(username);
                medicalRecordMessage.setText("Create a new medical record.");
                clearFields();
            }
            else {
                alert.close();
                showDataForExistingMedicalRecord();
            }
        }
    }

    private void deleteMedicalRecordFromDB(String usernameMedicalRecord) {
        for (MedicalRecord medicalRecord : MedicalRecordService.getMedicalRecordRepository().find())
            if (Objects.equals(usernameMedicalRecord, medicalRecord.getUsername()))
                MedicalRecordService.getMedicalRecordRepository().remove(medicalRecord);
    }

    private void clearFields() {
        choiceBoxFirstQuestion.setValue(null);
        choiceBoxSecondQuestion.setValue(null);
        choiceBoxThirdQuestion.setValue(null);
        choiceBoxFourthQuestion.setValue(null);
        vaccinatedComboBox.setValue(null);
    }

    private void showDataForExistingMedicalRecord() {
        for (MedicalRecord medicalRecord : MedicalRecordService.getMedicalRecordRepository().find())
            if (Objects.equals(username, medicalRecord.getUsername()))
                setFieldsForExistingMedicalRecord(medicalRecord);
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

        MedicalRecordService.addMedicalRecord(username, choiceBoxFirstQuestion.getValue(), choiceBoxSecondQuestion.getValue(),
                choiceBoxThirdQuestion.getValue(), choiceBoxFourthQuestion.getValue(), vaccinatedComboBox.getValue());
    }

    private void checkIfFieldsAreCompleted() throws FieldNotCompletedException {
        if(choiceBoxFirstQuestion.getValue() == null || choiceBoxSecondQuestion.getValue() == null || choiceBoxThirdQuestion.getValue() == null
                || choiceBoxFourthQuestion.getValue() == null || vaccinatedComboBox.getValue() == null)
            throw  new FieldNotCompletedException();
    }

    private void setFieldsForExistingMedicalRecord(@NotNull MedicalRecord medicalRecord) {
        choiceBoxFirstQuestion.setValue(medicalRecord.getAnswerFirstQuestion());
        choiceBoxSecondQuestion.setValue(medicalRecord.getAnswerSecondQuestion());
        choiceBoxThirdQuestion.setValue(medicalRecord.getAnswerThirdQuestion());
        choiceBoxFourthQuestion.setValue(medicalRecord.getAnswerThirdQuestion());
        vaccinatedComboBox.setValue(medicalRecord.getVaccinated());
    }
}
