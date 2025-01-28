package repository.service.impl;

import entity.Order;
import org.hibernate.Session;
import repository.service.OrderDao;
import util.HibernateUtil;

public class OrderDaoImpl implements OrderDao {

    @Override
    public boolean save(Order order) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(order);
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
    public boolean update(Order order, Integer id) {
        // Update logic can be implemented here
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Order order = session.find(Order.class, id);
            if (order != null) {
                session.remove(order);
                session.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Integer findLastId() {
        Session session = HibernateUtil.getSession();
        try {
            return session.createQuery("SELECT MAX(o.orderID) FROM Order o", Integer.class).getSingleResult();
        } finally {
            session.close();
        }
    }

    @Override
    public Order find(Integer id) {
        Session session = HibernateUtil.getSession();
        try {
            return session.find(Order.class, id);
        } finally {
            session.close();
        }
    }
}
