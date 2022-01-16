package controllers.dentist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.MedicalRecord;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DentistViewMedicalRecordController implements Initializable {

    @FXML
    private TextField answerFirstQuestionField;

    @FXML
    private TextField answerSecondQuestionField;

    @FXML
    private TextField answerThirdQuestionField;

    @FXML
    private TextField answerFourthQuestionField;

    @FXML
    private TextField answerFifthQuestionField;

    @FXML
    private TextField usernameField;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setFieldNotEditable();

        for(MedicalRecord medicalRecord : DentistViewAppointmentsController.getMedicalRecordArrayList()){
            usernameField.setText(medicalRecord.getUsername() + "'s medical record");
            usernameField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

            answerFirstQuestionField.setText(medicalRecord.getAnswerFirstQuestion());
            answerSecondQuestionField.setText(medicalRecord.getAnswerSecondQuestion());
            answerThirdQuestionField.setText(medicalRecord.getAnswerThirdQuestion());
            answerFourthQuestionField.setText(medicalRecord.getAnswerFourthQuestion());
            answerFifthQuestionField.setText(medicalRecord.getVaccinated());
        }
    }

    private void setFieldNotEditable() {
        usernameField.setEditable(false);
        answerFirstQuestionField.setEditable(false);
        answerSecondQuestionField.setEditable(false);
        answerThirdQuestionField.setEditable(false);
        answerFourthQuestionField.setEditable(false);
        answerFifthQuestionField.setEditable(false);
    }

    @FXML
    private void goBackToaAppointmentsPage(@NotNull ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("dentist/dentist_view_appointments.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 802, 432);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }
}
