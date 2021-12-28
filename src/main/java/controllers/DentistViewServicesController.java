package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Service;
import services.DentistService;

import java.util.ArrayList;

public class DentistViewServicesController extends  DentistPageAbstract {

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

    private ObservableList<Service> offer = FXCollections.observableArrayList();
    private ArrayList<Service> serviceList = new ArrayList<>();

    public void initialize() {
        tableServiceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableServicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableService.setItems(getOffers());
    }

    private ObservableList<Service> getOffers()  {
        for (Service offer : DentistService.getDentistRepository().find())
            serviceList.add(offer);

        offer.addAll(serviceList);
        return offer;
    }

    @FXML
    private void deleteSelectedService(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete SERVICE");
        alert.setHeaderText("Are you sure you want to delete the selected service?");

        if(alert.showAndWait().get() == ButtonType.OK)
            removeSelectedService();
        else
            alert.close();
    }

    private void removeSelectedService() {
        ObservableList<Service> selectedService , allServices;

        allServices = tableService.getItems();
        selectedService = tableService.getSelectionModel().getSelectedItems();

        for(Service service : selectedService)
            DentistService.getDentistRepository().remove(service);

        selectedService.forEach(allServices::remove);
    }
}
