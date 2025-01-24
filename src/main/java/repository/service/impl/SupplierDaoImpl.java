package repository.service.impl;

import dto.EmployeeDto;
import dto.SupplierDto;
import dto.tm.EmployeeTm;
import dto.tm.SupplierTm;
import entity.Employee;
import entity.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import repository.service.SupplierDao;
import util.CrudUtil;
import util.HibernateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SupplierDaoImpl implements SupplierDao {
    @Override
    public ObservableList<SupplierTm> findAll() throws SQLException, ClassNotFoundException {
        ObservableList<SupplierTm> list = FXCollections.observableArrayList();
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        List<Supplier> supplierList = session.createQuery("from Supplier", Supplier.class).getResultList();

        session.getTransaction().commit();
        session.close();

        ModelMapper modelMapper = new ModelMapper();
        supplierList.stream()
                .map(supplier -> modelMapper.map(supplier, SupplierDto.class))
                .collect(Collectors.toList());
        supplierList.forEach(supplier -> {
            list.add(modelMapper.map(supplier,SupplierTm.class));
        });
        return list;
    }

    @Override
    public ObservableList<String> getAllSupplierNames() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            List<Supplier> suppliers = session.createQuery("FROM Supplier", Supplier.class).list();
            return FXCollections.observableArrayList(
                    suppliers.stream()
                            .map(Supplier::getSCompany) // Get supplier company names
                            .collect(Collectors.toList())
            );
        } finally {
            session.close();
        }
    }

    @Override
    public Integer findSupplierIDByName(String supplierName) throws SQLException, ClassNotFoundException {
        String sql = "SELECT sID FROM Supplier WHERE sCompany = ?";
        ResultSet resultSet = CrudUtil.execute(sql, supplierName);
        if (resultSet.next()) {
            return resultSet.getInt("sID");
        }
        return null;
    }

    @Override
    public String findSupplierNameByID(Integer supplierID) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT sCompany FROM Supplier WHERE sID = ?", supplierID);
        if (rst.next()) {
            return rst.getString("sCompany");
        }
        return null;
    }

    @Override
    public boolean save(SupplierDto dto) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Supplier supEntity = new ModelMapper().map(dto, Supplier.class);
            session.persist(supEntity);
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
    public boolean update(SupplierDto dto, Integer id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Supplier set sCompany=?,sContact=?,sName=? WHERE sID=?",
                dto.getSCompany(),
                dto.getSContact(),
                dto.getSName(),
                id
        );
    }

    @Override
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Supplier WHERE sID = ?", id);
    }

    @Override
    public Integer findLastId() throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public SupplierDto find(Integer id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Supplier WHERE sID = ?", id);
        if (rst.next()) {
            return new SupplierDto(
                    rst.getString("sName"),
                    rst.getString("sCompany"),
                    rst.getString("sContact")
            );
        }
        return null;
    }
}
