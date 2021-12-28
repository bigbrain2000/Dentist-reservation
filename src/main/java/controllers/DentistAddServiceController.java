package controllers;

import exceptions.DentistServiceNameAlreadyExistsException;
import exceptions.DentistServicePriceException;
import exceptions.FieldNotCompletedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import services.DentistService;

public class DentistAddServiceController extends DentistPageAbstract implements FieldsOptionsInterface{

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
            registrationMessage.setText("Service succesfully added !");
            clearFields();
        } catch (FieldNotCompletedException | DentistServiceNameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        } catch (DentistServicePriceException e) {
            registrationMessage.setText(e.getMessage());
            priceField.clear();
        }
    }

    @FXML
    private void viewServices(ActionEvent event) {
    //pupa
        //bboobbaa
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
}
