package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Supplier;

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
    public JFXTextField txtSupplierID;

    @FXML
    public JFXTextField txtSupplierName;

    @FXML
    public JFXTextField txtSupplierContact;

    @FXML
    public JFXTextField txtSupplierCompany;

    @FXML
    public JFXTextField txtSupplierSearchByID;

    ObservableList<Supplier> supplierList;

    public void initialize() {

        colID.setCellValueFactory(new PropertyValueFactory<>("sID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("sName"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("sCompany"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("sContact"));

        loadSupplierData();
    }

    private void loadSupplierData() {
        supplierList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT * FROM Supplier";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                supplierList.add(new Supplier(
                        resultSet.getString("sID"),
                        resultSet.getString("sName"),
                        resultSet.getString("sCompany"),
                        resultSet.getString("sContact")
                ));
            }

            // Set the data in the TableView
            tblSupplier.setItems(supplierList);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load supplier data.").show();
        }
    }

    @FXML
    public void btnAddSupplierOnAction(ActionEvent actionEvent) throws SQLException {
        String SQL = "INSERT INTO Supplier (sName,sCompany,sContact) VALUES(?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        Supplier supplier = new Supplier(
                "",
                txtSupplierName.getText(),
                txtSupplierCompany.getText(),
                txtSupplierContact.getText()
        );

        PreparedStatement psTm = connection.prepareStatement(SQL);
        psTm.setString(1,supplier.getSName());
        psTm.setString(2,supplier.getSCompany());
        psTm.setString(3,supplier.getSContact());
        int rowsAffected = psTm.executeUpdate();

        if (rowsAffected > 0) {
            new Alert(Alert.AlertType.INFORMATION, "Supplier Added successfully!").show();
            loadSupplierData();

            txtSupplierName.clear();
            txtSupplierCompany.clear();
            txtSupplierContact.clear();

        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to add the supplier. Please try again.").show();
        }
    }

    @FXML
    public void btnUpdateSupplierOnAction(ActionEvent actionEvent) {

        String supplierID = txtSupplierSearchByID.getText();
        String updatedName = txtSupplierName.getText();
        String updatedCompany = txtSupplierCompany.getText();
        String updatedContact = txtSupplierContact.getText();

        if (supplierID == null || supplierID.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid Supplier ID to update.").show();
        }else {

            if (updatedName.trim().isEmpty() || updatedContact.trim().isEmpty() || updatedCompany.trim().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill in all fields before updating.").show();
            } else {

                try {
                    // Establish database connection
                    Connection connection = DBConnection.getInstance().getConnection();

                    // Prepare the update query
                    String updateQuery = "UPDATE Supplier SET sName = ?, sCompany = ?, sContact = ? WHERE sID = ?";
                    PreparedStatement ps = connection.prepareStatement(updateQuery);

                    // Set the parameters
                    ps.setString(1, updatedName);
                    ps.setString(2, updatedCompany);
                    ps.setString(3, updatedContact);
                    ps.setString(4, supplierID);

                    // Execute the update query
                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Supplier details updated successfully!").show();

                        loadSupplierData();

                    } else {
                        new Alert(Alert.AlertType.WARNING, "No supplier found with the provided ID").show();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "An error occurred while updating supplier details.").show();
                }
            }
        }
    }

    @FXML
    public void btnDeleteSupplierOnAction(ActionEvent actionEvent) {
        String supplierId = txtSupplierSearchByID.getText();

        if (supplierId == null || supplierId.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid supplier ID to delete.").show();
        }else {

            try {
                // Establish database connection
                Connection connection = DBConnection.getInstance().getConnection();

                // Prepare the update query
                String deleteQuery = "DELETE FROM Supplier WHERE sID = ?";
                PreparedStatement ps = connection.prepareStatement(deleteQuery);

                ps.setString(1, supplierId);

                // Execute the update query
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier details delete successfully!").show();

                    loadSupplierData();

                } else {
                    new Alert(Alert.AlertType.WARNING, "No supplier found with the provided ID.").show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred while deleting supplier details.").show();
            }
        }
    }

    @FXML
    public void btnSearchSupplierOnAction(ActionEvent actionEvent) {

        String supplierId = txtSupplierSearchByID.getText();

        if(supplierId == null || supplierId.trim().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please enter a valid supplier ID.").show();
        }else {

            try {

                Connection connection = DBConnection.getInstance().getConnection();

                String searchSQL = "SELECT * FROM Supplier WHERE sID = ?";
                PreparedStatement ps = connection.prepareStatement(searchSQL);
                ps.setString(1, supplierId);

                // Execute the query
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    // If the employee exists, set the text fields with their details
                    txtSupplierName.setText(resultSet.getString("sName"));
                    txtSupplierCompany.setText(resultSet.getString("sCompany"));
                    txtSupplierContact.setText(resultSet.getString("sContact"));
                } else {
                    // If no employee found, show an alert
                    new Alert(Alert.AlertType.WARNING, "No supplier found with the provided ID.").show();

                    // Clear the text fields if no result
                    txtSupplierName.clear();
                    txtSupplierCompany.clear();
                    txtSupplierContact.clear();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred while searching for the supplier.").show();
            }
        }
    }
}
