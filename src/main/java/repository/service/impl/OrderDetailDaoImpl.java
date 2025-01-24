package repository.service.impl;

import entity.OrderDetail;
import org.hibernate.Session;
import repository.service.OrderDetailDao;
import util.HibernateUtil;

public class OrderDetailDaoImpl implements OrderDetailDao {

    @Override
    public boolean save(OrderDetail orderDetail) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(orderDetail);
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
    public boolean update(OrderDetail orderDetail, Integer id) {
        // Update logic can be implemented here
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            OrderDetail orderDetail = session.find(OrderDetail.class, id);
            if (orderDetail != null) {
                session.remove(orderDetail);
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
            return session.createQuery("SELECT MAX(od.detailID) FROM OrderDetail od", Integer.class).getSingleResult();
        } finally {
            session.close();
        }
    }

    @Override
    public OrderDetail find(Integer id) {
        Session session = HibernateUtil.getSession();
        try {
            return session.find(OrderDetail.class, id);
        } finally {
            session.close();
        }
    }
}
