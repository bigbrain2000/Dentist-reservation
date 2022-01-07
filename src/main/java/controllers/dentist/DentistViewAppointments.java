package controllers.dentist;

import controllers.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class DentistViewAppointments extends DentistPageAbstract implements Initializable {

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

        appointmentTableView.setItems(LoginController.getAppointmentsForDentist());
    }
}
