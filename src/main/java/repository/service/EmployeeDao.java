package repository.service;

import repository.CrudDao;
import dto.EmployeeDto;
import dto.tm.EmployeeTm;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface EmployeeDao extends CrudDao<EmployeeDto,Integer> {
    ObservableList<EmployeeTm> findAll() throws SQLException, ClassNotFoundException;
    ObservableList<Integer> getAllEmployeeIDs() throws SQLException, ClassNotFoundException;
    boolean existsByEmail(String email);
}
