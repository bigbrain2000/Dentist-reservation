package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class DentistPageController {

    @FXML
    private Button addDentists;

    @FXML
    private Button viewProfileButton;

    @FXML
    private Button addServicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button addAppointmentsButton;

    @FXML
    private Button minimizeField;

    @FXML
    private Button closeField;

    @FXML
    public void minimizeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    public void closeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

}
