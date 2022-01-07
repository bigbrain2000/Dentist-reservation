package controllers.client;

import controllers.login.LoginController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Appointment;
import services.AppointmentService;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ClientViewAppointmentController extends ClientPageAbstract implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateColumn.setCellFactory(column -> new TableCell<>() {
            private final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");

            @Override
            protected void updateItem(java.util.Date item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    setText(FORMAT.format(item));
                }
            }
        });

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        secondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        serviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        servicePriceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        dentistNameColumn.setCellValueFactory(new PropertyValueFactory<>("dentistName"));

        appointmentTableView.setItems(LoginController.getAppointments());
    }



    @FXML
    private void deleteAppointment() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete SERVICE");
        alert.setHeaderText("Are you sure you want to delete the selected appointment?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK)
                removeSelectedService();
            else
                alert.close();
        });
    }

    private void removeSelectedService() {
        ObservableList<Appointment> selectedService = appointmentTableView.getSelectionModel().getSelectedItems();

        for(Appointment appointment : selectedService)
            AppointmentService.getAppointmentRepository().remove(appointment);

        appointmentTableView.getItems().removeAll(appointmentTableView.getSelectionModel().getSelectedItems());  //select the wanted service
    }
}
