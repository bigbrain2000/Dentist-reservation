package controllers.dentist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Service;
import org.jetbrains.annotations.NotNull;
import services.DentistService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DentistViewServicesController implements Initializable, DentistPageInterface {

    @FXML
    private Button deleteServiceButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Service> tableService;

    @FXML
    private TableColumn<Service, String> tableServiceName;

    @FXML
    private TableColumn<Service, String> tableServicePrice;

    @FXML
    private Text deleteServiceMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableServiceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableServicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableService.setItems(getOfferObservableArrayList());
    }

    @NotNull
    private ObservableList<Service> getOfferObservableArrayList() {
        ObservableList<Service> offerObservableArrayList = FXCollections.observableArrayList();
        ArrayList<Service> serviceList = new ArrayList<>();

        for (Service offer : DentistService.getDentistRepository().find())
            serviceList.add(offer);

        offerObservableArrayList.addAll(serviceList);
        return offerObservableArrayList;
    }

    @FXML
    private void deleteSelectedService() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete SERVICE");
        alert.setHeaderText("Are you sure you want to delete the selected service?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK)
                removeSelectedService();
            else
                alert.close();
        });
    }

    private void removeSelectedService() {
        ObservableList<Service> selectedService = tableService.getSelectionModel().getSelectedItems();

        for (Service service : selectedService)
            DentistService.getDentistRepository().remove(service);

        tableService.getItems().removeAll(tableService.getSelectionModel().getSelectedItems());  //select the wanted service
    }

    @Override
    public void goBackToDentistPage(@NotNull ActionEvent event) throws IOException {
        DentistPageInterface.super.goBackToDentistPage(event);
    }
}