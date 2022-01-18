package controllers.dentist;

import controllers.user.UserAppointmentViewAbstract;
import exceptions.username.UserNotSelectedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import model.MedicalRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import services.AppointmentService;
import services.MedicalRecordService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class DentistViewAppointmentsController extends UserAppointmentViewAbstract implements Initializable, DentistPageInterface {

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, String> firstNameColumn;

    @FXML
    private TableColumn<Appointment, String> secondNameColumn;

    @FXML
    private TableColumn<Appointment, String> serviceNameColumn;

    @FXML
    private TableColumn<Appointment, Float> servicePriceColumn;

    @FXML
    private TableColumn<Appointment, Date> dateColumn;

    @FXML
    private TableColumn<Appointment, String> dentistNameColumn;

    @FXML
    private Text viewMedicalRecordMessage;

    @FXML
    private Button backButton;

    @FXML
    private Button viewMedicalRecordButton;

    private static final ArrayList<MedicalRecord> medicalRecordArrayList = new ArrayList<>();

    public static ArrayList<MedicalRecord> getMedicalRecordArrayList() {
        return medicalRecordArrayList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableView(firstNameColumn, secondNameColumn, serviceNameColumn, servicePriceColumn, dateColumn, dentistNameColumn);

        appointmentTableView.setItems(getAppointmentsForDentist());
    }

    @NotNull
    private ObservableList<Appointment> getAppointmentsForDentist() {

        ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
        ObservableList<Appointment> appointmentObservableArrayList = FXCollections.observableArrayList();

        for (Appointment appointment : AppointmentService.getAppointmentRepository().find())
            appointmentArrayList.add(appointment);

        appointmentObservableArrayList.addAll(appointmentArrayList);
        return appointmentObservableArrayList;
    }

    @FXML
    private void viewMedicalRecord(@NotNull ActionEvent event) throws IOException {
        try {
            medicalRecordArrayList.add(this.getMedicalRecordBasedOnSelectedAppointment());

            FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getClassLoader().getResource("dentistFXML/dentist_view_medical_record.fxml"));
            Parent viewUserLogin = Loader.load();
            Scene loginScene = new Scene(viewUserLogin, 802, 433);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();

        } catch (UserNotSelectedException e) {
            viewMedicalRecordMessage.setText(e.getMessage());
        }
    }

    @Nullable
    private MedicalRecord getMedicalRecordBasedOnSelectedAppointment() throws UserNotSelectedException {

        if (Objects.equals(appointmentTableView.getSelectionModel().getSelectedItem(), null))
            throw new UserNotSelectedException();
        else {
            String usernameSelected = appointmentTableView.getSelectionModel().getSelectedItem().getUsername();

            for (MedicalRecord medicalRecord : MedicalRecordService.getMedicalRecordRepository().find())
                if (Objects.equals(usernameSelected, medicalRecord.getUsername()))
                    return medicalRecord;
        }

        return null;
    }

    @Override
    public void goBackToDentistPage(@NotNull ActionEvent event) throws IOException {
        DentistPageInterface.super.goBackToDentistPage(event);
    }
}
