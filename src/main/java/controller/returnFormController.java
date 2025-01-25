package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import repository.DaoFactory;
import repository.service.ProductDao;

import java.sql.SQLException;

public class returnFormController {

    @FXML
    public JFXTextField txtReturnQty;

    @FXML
    public JFXTextField txtProductID;

    @FXML
    public JFXTextField txtOrderID;

    @FXML
    public void btnReturnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        ProductDao productDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.PRODUCT);

        Integer orderId = Integer.parseInt(txtOrderID.getText());
        Integer productId = Integer.parseInt(txtProductID.getText());
        Integer qty = Integer.parseInt(txtReturnQty.getText());

        Integer Qty = productDao.getProductQuantity( productId);

        System.out.println(Qty);

        Integer updateQty = qty +Qty;

        System.out.println(updateQty);

        boolean isUpdatedQty = productDao.updateProductQuantity(productId, updateQty);

        if(isUpdatedQty){
            new Alert(Alert.AlertType.INFORMATION, "Product Quantity updated successfully!").show();
            txtReturnQty.clear();
            txtProductID.clear();
            txtOrderID.clear();
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to update quantity.").show();
        }
    }
}
