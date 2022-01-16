package controllers.client;

import controllers.login.LoginController;
import controllers.user.UserAppointmentViewAbstract;
import exceptions.username.UserNotSelectedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.Appointment;
import model.MedicalRecord;
import model.User;
import org.jetbrains.annotations.NotNull;
import services.AppointmentService;
import services.MedicalRecordService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientViewAppointmentController extends UserAppointmentViewAbstract implements Initializable, ClientPageInterface {

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
    private Button backButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private Text appointmentMessage;

    private static final User loggedUser =  LoginController.getLoggedUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableView(firstNameColumn, secondNameColumn, serviceNameColumn, servicePriceColumn, dateColumn, dentistNameColumn);

        appointmentTableView.setItems(getAppointmentForClient());
    }

    @NotNull
    private ObservableList<Appointment> getAppointmentForClient()  {

        ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
        ObservableList<Appointment> appointmentObservableArrayList = FXCollections.observableArrayList();

        for (Appointment appointment : AppointmentService.getAppointmentRepository().find())
            if(Objects.equals(appointment.getUsername(), loggedUser.getUsername()))
                appointmentArrayList.add(appointment);

        appointmentObservableArrayList.addAll(appointmentArrayList);
        return appointmentObservableArrayList;
    }

    @FXML
    private void deleteAppointment() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete SERVICE");
        alert.setHeaderText("Are you sure you want to delete the selected appointment?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    removeSelectedService();

                } catch (UserNotSelectedException e) {
                    appointmentMessage.setVisible(true);
                    appointmentMessage.setText(e.getMessage());
                }
            }
            else
                appointmentMessage.setVisible(false);
                alert.close();
        });
    }

    private void removeSelectedService() throws UserNotSelectedException {

        if(Objects.equals(appointmentTableView.getSelectionModel().getSelectedItem(), null))
            throw new UserNotSelectedException();
        else {
            ObservableList<Appointment> selectedService = appointmentTableView.getSelectionModel().getSelectedItems();

            for(Appointment appointment : selectedService)
                AppointmentService.getAppointmentRepository().remove(appointment);

            appointmentTableView.getItems().removeAll(appointmentTableView.getSelectionModel().getSelectedItems());  //select the wanted service
        }
    }

    @Override
    public void goBackToClientPage(@NotNull ActionEvent event) throws IOException {
        ClientPageInterface.super.goBackToClientPage(event);
    }
}
