package repository.service;

import javafx.collections.ObservableList;
import repository.CrudDao;
import dto.ProductDto;
import dto.tm.ProductTm;

import java.sql.SQLException;

public interface ProductDao extends CrudDao<ProductDto,Integer> {
    ObservableList<ProductTm> findAll() throws SQLException, ClassNotFoundException;

    ObservableList<Integer> getAllProductIDs() throws SQLException, ClassNotFoundException;

    ProductDto find(Integer id) throws SQLException, ClassNotFoundException;

    Integer getProductQuantity(Integer productID) throws SQLException, ClassNotFoundException;

    boolean updateProductQuantity(Integer productID, int newQuantity)throws SQLException, ClassNotFoundException;

}
