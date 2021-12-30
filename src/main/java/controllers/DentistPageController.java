package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class DentistPageController extends PageAbstract{

    @FXML
    private Button viewProfileButton;

    @FXML
    private Button addDentists;

    @FXML
    private Button addServicesButton;

    @FXML
    private Button viewServicesButton;

    @FXML
    private Button viewAppointmentsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button minimizeField;

    @FXML
    private Button closeField;

    @FXML
    private void handleAddDentists(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("dentist_adding.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 600, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void handleAddServiceAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("dentist_add_services.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 620, 430);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void handleViewServicesAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("dentist_view_services.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 630, 537);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }
}
