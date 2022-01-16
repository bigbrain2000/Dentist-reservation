package controllers.user;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class UserAppointmentViewAbstract {

    public void initializeTableView(@NotNull TableColumn<Appointment, String> firstNameColumn, @NotNull TableColumn<Appointment, String> secondNameColumn, @NotNull TableColumn<Appointment, String> serviceNameColumn,
                                    @NotNull TableColumn<Appointment, Float> servicePriceColumn, @NotNull TableColumn<Appointment, Date> dateColumn, @NotNull TableColumn<Appointment, String> dentistNameColumn) {

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
    }
}
