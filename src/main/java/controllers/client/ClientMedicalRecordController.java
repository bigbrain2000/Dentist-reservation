package controllers.client;

import controllers.login.LoginController;
import exceptions.fields.FieldNotCompletedException;
import exceptions.username.MedicalRecordUsernameAlreadyExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.MedicalRecord;
import model.User;
import org.jetbrains.annotations.NotNull;
import services.MedicalRecordService;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientMedicalRecordController implements Initializable, ClientPageInterface {

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

    private static final User loggedUser = LoginController.getLoggedUser();

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
        if(MedicalRecordService.isMedicalRecordCompleted(loggedUser.getUsername())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete medical record");
            alert.setHeaderText("You already have a medical record!\nPress OK if you want to delete this and make a new one." +
                    "\nPress CANCEL if you don`t want to change the current medical record.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    MedicalRecordService.deleteMedicalRecordFromDB(loggedUser.getUsername());
                    medicalRecordMessage.setText("Create a new medical record.");
                    clearFields();
                } else {
                    alert.close();
                    showDataForExistingMedicalRecord(choiceBoxFirstQuestion, choiceBoxSecondQuestion, choiceBoxThirdQuestion, choiceBoxFourthQuestion, vaccinatedComboBox);
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

    private void showDataForExistingMedicalRecord(@NotNull ChoiceBox<String> choiceBoxFirstQuestion, @NotNull ChoiceBox<String>  choiceBoxSecondQuestion,
                                                        @NotNull ChoiceBox<String>  choiceBoxThirdQuestion, @NotNull ChoiceBox<String>  choiceBoxFourthQuestion, @NotNull ComboBox<String>  vaccinatedComboBox) {

        for (MedicalRecord medicalRecord : MedicalRecordService.getMedicalRecordRepository().find())
            if (Objects.equals(loggedUser.getUsername(), medicalRecord.getUsername()))
                setFieldsForExistingMedicalRecord(medicalRecord, choiceBoxFirstQuestion, choiceBoxSecondQuestion, choiceBoxThirdQuestion, choiceBoxFourthQuestion, vaccinatedComboBox);
    }

    private void setFieldsForExistingMedicalRecord(@NotNull MedicalRecord medicalRecord, @NotNull ChoiceBox<String>  choiceBoxFirstQuestion, @NotNull ChoiceBox<String>  choiceBoxSecondQuestion,
                                                          @NotNull ChoiceBox<String>  choiceBoxThirdQuestion, @NotNull ChoiceBox<String>  choiceBoxFourthQuestion, @NotNull ComboBox<String>  vaccinatedComboBox) {

        choiceBoxFirstQuestion.setValue(medicalRecord.getAnswerFirstQuestion());
        choiceBoxSecondQuestion.setValue(medicalRecord.getAnswerSecondQuestion());
        choiceBoxThirdQuestion.setValue(medicalRecord.getAnswerThirdQuestion());
        choiceBoxFourthQuestion.setValue(medicalRecord.getAnswerFourthQuestion());
        vaccinatedComboBox.setValue(medicalRecord.getVaccinated());
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

        MedicalRecordService.addMedicalRecord(loggedUser.getUsername(), choiceBoxFirstQuestion.getValue(), choiceBoxSecondQuestion.getValue(), choiceBoxThirdQuestion.getValue(),
                choiceBoxFourthQuestion.getValue(), vaccinatedComboBox.getValue());
    }

    private void checkIfFieldsAreCompleted() throws FieldNotCompletedException {
        if(choiceBoxFirstQuestion.getValue() == null || choiceBoxSecondQuestion.getValue() == null || choiceBoxThirdQuestion.getValue() == null
                || choiceBoxFourthQuestion.getValue() == null || vaccinatedComboBox.getValue() == null)
            throw  new FieldNotCompletedException();
    }

    @Override
    public void goBackToClientPage(@NotNull ActionEvent event) throws IOException {
        ClientPageInterface.super.goBackToClientPage(event);
    }
}
