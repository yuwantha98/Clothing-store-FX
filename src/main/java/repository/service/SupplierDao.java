package repository.service;

import dto.SupplierDto;
import dto.tm.SupplierTm;
import javafx.collections.ObservableList;
import repository.CrudDao;

import java.sql.SQLException;


public interface SupplierDao extends CrudDao<SupplierDto,Integer> {
    ObservableList<SupplierTm> findAll() throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllSupplierNames() throws SQLException, ClassNotFoundException;

    Integer findSupplierIDByName(String supplierName) throws SQLException, ClassNotFoundException;
    String findSupplierNameByID(Integer supplierID) throws SQLException, ClassNotFoundException;

}
