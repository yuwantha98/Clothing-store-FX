package repository.service.impl;


import dto.ProductDto;
import dto.tm.ProductTm;
import entity.Product;
import entity.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.service.ProductDao;
import org.hibernate.Session;
import util.CrudUtil;
import util.HibernateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoImpl implements ProductDao {

    @Override
    public ObservableList<ProductTm> findAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            List<Product> products = session.createQuery("FROM Product", Product.class).list();
            return FXCollections.observableArrayList(
                    products.stream()
                            .map(product -> new ProductTm(
                                    product.getPID(),
                                    product.getPName(),
                                    product.getPSize(),
                                    product.getSupplier().getSCompany(),
                                    product.getPQuantity(),
                                    product.getPBuyingPrice(),
                                    product.getPPrice(),
                                    product.getPDiscount(),
                                    product.getPProfit(),
                                    product.getPCategory()
                            ))
                            .collect(Collectors.toList())
            );
        } finally {
            session.close();
        }
    }

    @Override
    public ObservableList<Integer> getAllProductIDs() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            List<Integer> productIds = session.createQuery("SELECT p.pID FROM Product p", Integer.class).list();
            return FXCollections.observableArrayList(productIds);
        } finally {
            session.close();
        }
    }
    @Override
    public boolean save(ProductDto dto) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();

            Supplier supplier = session.get(Supplier.class, dto.getSupplierID());
            if (supplier == null) {
                System.err.println("Supplier not found with ID: " + dto.getSupplierID());
                return false;
            }

            Product product = new Product();
            product.setPName(dto.getPName());
            product.setPSize(dto.getPSize());
            product.setSupplier(supplier);
            product.setPQuantity(dto.getPQuantity());
            product.setPBuyingPrice(dto.getPBuyingPrice());
            product.setPPrice(dto.getPPrice());
            product.setPDiscount(dto.getPDiscount());
            product.setPProfit(dto.getPProfit());
            product.setPCategory(dto.getPCategory());

            session.persist(product);
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
    public boolean update(ProductDto dto, Integer id) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product == null) return false;

            Supplier supplier = session.get(Supplier.class, dto.getSupplierID());
            if (supplier == null) return false;

            product.setPName(dto.getPName());
            product.setPSize(dto.getPSize());
            product.setSupplier(supplier);
            product.setPQuantity(dto.getPQuantity());
            product.setPBuyingPrice(dto.getPBuyingPrice());
            product.setPPrice(dto.getPPrice());
            product.setPDiscount(dto.getPDiscount());
            product.setPProfit(dto.getPProfit());
            product.setPCategory(dto.getPCategory());

            session.update(product);
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
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product == null) return false;

            session.delete(product);
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
    public Integer findLastId() throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public ProductDto find(Integer id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT p.pName, p.pSize, p.pQuantity, p.pBuyingPrice, p.pPrice, p.pDiscount, p.pProfit, p.pCategory, s.sID " +
                        "FROM Product p " +
                        "JOIN Supplier s ON p.sID = s.sID " +
                        "WHERE p.pID = ?",
                id
        );

        if (rst.next()) {
            return new ProductDto(
                    rst.getString("pName"),
                    rst.getString("pSize"),
                    rst.getInt("sID"),
                    rst.getInt("pQuantity"),
                    rst.getDouble("pBuyingPrice"),
                    rst.getDouble("pPrice"),
                    rst.getDouble("pDiscount"),
                    rst.getDouble("pProfit"),
                    rst.getString("pCategory")
            );
        }
        return null;
    }

    @Override
    public Integer getProductQuantity(Integer productID) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            return session.createQuery(
                            "SELECT p.pQuantity FROM Product p WHERE p.pID = :productID", Integer.class)
                    .setParameter("productID", productID)
                    .uniqueResultOptional()
                    .orElse(null);
        } finally {
            session.close();
        }
    }

    @Override
    public void updateProductQuantity(Integer productID, int newQuantity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();

            String hql = "UPDATE Product p SET p.pQuantity = :newQuantity WHERE p.pID = :productID";
            session.createQuery(hql)
                    .setParameter("newQuantity", newQuantity)
                    .setParameter("productID", productID)
                    .executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
            throw new SQLException("Error updating product quantity: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }


}
