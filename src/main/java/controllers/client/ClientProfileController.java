package controllers.client;

import controllers.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientProfileController extends ClientPageAbstract implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField roleField;

    @FXML
    private StackPane stackPane;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginController.setTextFieldsForProfileBasedOnUsername(usernameField, firstNameField, secondNameField, addressField, phoneNumberField, roleField);
    }

    private void loadRegisterFXML(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("register/user_registration.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 651, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void deleteAccount(javafx.event.ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setHeaderText("Are you sure you want to delete this account?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK ) {
                LoginController.deleteUserFromDB();

                try {
                    loadRegisterFXML(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
                alert.close();
        });
    }
}
