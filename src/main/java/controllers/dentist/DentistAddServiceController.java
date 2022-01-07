package controllers.dentist;

import exceptions.username.DentistServiceNameAlreadyExistsException;
import exceptions.price.DentistServicePriceException;
import exceptions.fields.FieldNotCompletedException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import services.DentistService;
import java.io.IOException;

public class DentistAddServiceController extends DentistPageAbstract {

    @FXML
    private Button backButton;

    @FXML
    private Button viewServicesButton;

    @FXML
    private TextField serviceNameField;

    @FXML
    private TextField priceField;

    @FXML
    private Button createServiceButton;

    @FXML
    private Text registrationMessage;

    @FXML
    private void createService() {
        try {
            insertDentistServiceDataIntoDBIfCorrectPriceFieldFormat();
            registrationMessage.setText("Service successfully added !");
            clearFields();
        } catch (FieldNotCompletedException | DentistServiceNameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        } catch (DentistServicePriceException e) {
            registrationMessage.setText(e.getMessage());
            priceField.clear();
        }
    }

    private void insertDentistServiceDataIntoDBIfCorrectPriceFieldFormat() throws FieldNotCompletedException, DentistServiceNameAlreadyExistsException, DentistServicePriceException {
        if(DentistService.checkIfPriceIsAFloat(priceField.getText()))
            DentistService.addDentistService(serviceNameField.getText(), Float.parseFloat(priceField.getText()));
        else
            throw new DentistServicePriceException();
    }

    private void clearFields() {
        serviceNameField.clear();
        priceField.clear();
    }

    @FXML
    private void handleViewServicesAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("dentist/dentist_view_services.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 635, 537.0);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }
}
