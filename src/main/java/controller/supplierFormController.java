package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class supplierFormController {

    @FXML
    public TableView tblSupplier;

    @FXML
    public TableColumn colID;

    @FXML
    public TableColumn colName;

    @FXML
    public TableColumn colContact;

    @FXML
    public TableColumn colCompany;

    @FXML
    public JFXTextField txtSupplierID;

    @FXML
    public JFXTextField txtSupplierName;

    @FXML
    public JFXTextField txtSupplierContact;

    @FXML
    public JFXTextField txtSupplierCompany;

    @FXML
    public JFXTextField txtSupplietrSearchByID;

    @FXML
    public void btnAddSupplierOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnUpdateSupplierOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnDeleteSupplierOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnSearchSupplierOnAction(ActionEvent actionEvent) {
    }
}
