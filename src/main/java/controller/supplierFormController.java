package controller;

import com.jfoenix.controls.JFXTextField;
import dto.SupplierDto;
import dto.tm.SupplierTm;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import repository.DaoFactory;
import repository.service.SupplierDao;
import java.sql.*;

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
    public JFXTextField txtSupplierName;

    @FXML
    public JFXTextField txtSupplierContact;

    @FXML
    public JFXTextField txtSupplierCompany;

    @FXML
    public JFXTextField txtSupplierSearchByID;

    SupplierDao supplierDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SUPPLIER);

    public void initialize() {

        colID.setCellValueFactory(new PropertyValueFactory<>("sID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("sName"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("sCompany"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("sContact"));

        loadTable();
    }

    public void loadTable(){
        try {
            ObservableList<SupplierTm> all = supplierDao.findAll();
            tblSupplier.setItems(all);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    public void btnAddSupplierOnAction(ActionEvent actionEvent) throws SQLException {

        if (!validateInputs()) {
            return;
        }

        SupplierDto supplierDto = new SupplierDto(
                txtSupplierName.getText(),
                txtSupplierCompany.getText(),
                txtSupplierContact.getText()
        );

        try {
            boolean isAdded = supplierDao.save(supplierDto);
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier added successfully!").show();
                clearForm();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void btnUpdateSupplierOnAction(ActionEvent actionEvent) {
        SupplierDto supplierDto = new SupplierDto(
                txtSupplierName.getText(),
                txtSupplierCompany.getText(),
                txtSupplierContact.getText()
        );
        try {
            boolean isUpdate = supplierDao.update(supplierDto, Integer.valueOf(txtSupplierSearchByID.getText()));
            if(isUpdate){
                new Alert(Alert.AlertType.INFORMATION,"Supplier Update Successfully !").show();
                loadTable();
            }else {
                new Alert(Alert.AlertType.ERROR,"Something went wrong !").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void btnDeleteSupplierOnAction(ActionEvent actionEvent) {
        try {
            boolean delete = supplierDao.delete(Integer.valueOf(txtSupplierSearchByID.getText()));
            if(delete){
                new Alert(Alert.AlertType.INFORMATION,"Supplier DELETE Successfully !").show();
                loadTable();
                clearForm();
            }else {
                new Alert(Alert.AlertType.ERROR,"Something went wrong !").show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void btnSearchSupplierOnAction(ActionEvent actionEvent) {
        try {
            Integer supplierId = Integer.valueOf(txtSupplierSearchByID.getText());
            SupplierDto supplierDto = supplierDao.find(supplierId);

            if (supplierDto != null) {
                txtSupplierName.setText(supplierDto.getSName());
                txtSupplierCompany.setText(supplierDto.getSCompany());
                txtSupplierContact.setText(supplierDto.getSContact());
            } else {
                new Alert(Alert.AlertType.WARNING, "No employee found with ID: " + supplierId).show();
                clearForm();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid ID format. Please enter a valid number.").show();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error fetching supplier : " + e.getMessage()).show();
        }
    }

    private boolean validateInputs() {
        if (txtSupplierName.getText().isEmpty() ||
                txtSupplierCompany.getText().isEmpty() ||
                txtSupplierContact.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required.").show();
            return false;
        }

        if (!txtSupplierContact.getText().matches("\\d{10}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid contact number.").show();
            return false;
        }

        return true;
    }

    private void clearForm() {
        txtSupplierName.clear();
        txtSupplierCompany.clear();
        txtSupplierContact.clear();
    }
}
