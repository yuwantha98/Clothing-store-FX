package repository.service.impl;

import repository.service.EmployeeDao;
import dto.tm.EmployeeTm;
import dto.EmployeeDto;
import entity.Employee;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.HibernateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDaoImpl implements EmployeeDao {

    @Override
    public boolean save(EmployeeDto employee) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Employee empEntity = new ModelMapper().map(employee, Employee.class);
            session.persist(empEntity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(EmployeeDto dto, Integer id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Employee set  eContact=?,eEmail=?,eName=? WHERE eID=?",
                dto.getEContact(),
                dto.getEEmail(),
                dto.getEName(),
                id
        );
    }

    @Override
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee WHERE eID = ?", id);
    }

    @Override
    public Integer findLastId() throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public EmployeeDto find(Integer id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Employee WHERE eID = ?", id);
        if (rst.next()) {
            return new EmployeeDto(
                    rst.getString("eName"),
                    rst.getString("eContact"),
                    rst.getString("eEmail"),
                    rst.getString("ePassword")
            );
        }
        return null;
    }

    @Override
    public ObservableList<EmployeeTm> findAll() throws SQLException, ClassNotFoundException {
        ObservableList<EmployeeTm> list = FXCollections.observableArrayList();
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        List<Employee> employeeList = session.createQuery("from Employee", Employee.class).getResultList();

        session.getTransaction().commit();
        session.close();

        ModelMapper modelMapper = new ModelMapper();
        employeeList.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());
        employeeList.forEach(employee -> {
            list.add(modelMapper.map(employee,EmployeeTm.class));
        });
        return list;
    }

    @Override
    public ObservableList<Integer> getAllEmployeeIDs() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            List<Integer> employeeIds = session.createQuery("SELECT e.eID FROM Employee e", Integer.class).list();
            return FXCollections.observableArrayList(employeeIds);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Long count = session.createQuery(
                            "SELECT COUNT(e) FROM Employee e WHERE e.eEmail = :email", Long.class)
                    .setParameter("email", email)
                    .uniqueResult();
            session.getTransaction().commit();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updatePasswordByEmail(String email, String encryptedPassword) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Employee set  ePassword=? WHERE eEmail=?",
                encryptedPassword,
                email
        );
    }
}