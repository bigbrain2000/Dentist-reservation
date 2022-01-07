package controllers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class ClientPageController {

    @FXML
    private Button viewProfileButton;

    @FXML
    private Button addMedicalRecordButton;

    @FXML
    private Button viewServicesButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button viewAppointmentsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button minimizeField;

    @FXML
    private Button closeField;

    @FXML
    private void handleViewProfileAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("client/client_profile.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 600, 420);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void handleLoginAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("login/user_login.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 654, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void handleAddMedicalRecordAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("client/client_medical_record.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 600, 400);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void handleAddAppointmentAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("client/client_appointment.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 600, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void handleViewAppointmentAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("client/client_view_appointment.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 802, 433);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void minimizeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    private void closeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }
}
