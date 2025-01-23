package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class orderFormController {

    @FXML
    public TableView tblOrder;

    @FXML
    public TableColumn colProductID;

    @FXML
    public TableColumn colCustomerName;

    @FXML
    public TableColumn colProductName;

    @FXML
    public TableColumn colQty;

    @FXML
    public TableColumn colUnitPrice;

    @FXML
    public TableColumn colDiscount;

    @FXML
    public TableColumn colTotalPrice;

    @FXML
    public JFXTextField txtCustomerName;

    @FXML
    public JFXTextField txtCustomerContact;

    @FXML
    public JFXTextField txtCustomerEmail;

    @FXML
    public JFXTextField txtProductName;

    @FXML
    public JFXTextField txtProductDiscount;

    @FXML
    public JFXTextField txtProductQty;

    @FXML
    public JFXTextField txtBuyingQty;

    @FXML
    public JFXComboBox cmbProductID;

    @FXML
    public JFXComboBox cmbEmployeeID;

    @FXML
    public JFXTextField txtProductPrice;

    @FXML
    public void btnAddToCartOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnRemoveProductOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
    }
}
