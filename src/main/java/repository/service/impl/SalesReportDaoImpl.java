package repository.service.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import repository.service.SalesReportDao;
import util.HibernateUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesReportDaoImpl implements SalesReportDao {

    @Override
    public Map<String, Double> getDailySales(LocalDate date) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();

            String hql = "SELECT DATE(o.orderDate), SUM(o.totalAmount) FROM Orders o WHERE DATE(o.orderDate) = :date GROUP BY DATE(o.orderDate)";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("date", date);

            Map<String, Double> result = new HashMap<>();
            for (Object[] row : query.list()) {
                result.put(row[0].toString(), (Double) row[1]);
            }

            session.getTransaction().commit();
            return result;
        } finally {
            session.close();
        }
    }


    @Override
    public Map<String, Double> getMonthlySales(int month) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT MONTH(o.orderDate), SUM(o.totalAmount) FROM Order o WHERE MONTH(o.orderDate) = :month GROUP BY MONTH(o.orderDate)";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("month", month);

            Map<String, Double> result = new HashMap<>();
            for (Object[] row : query.list()) {
                result.put("Month: " + row[0], (Double) row[1]);
            }

            session.getTransaction().commit();
            return result;
        } finally {
            session.close();
        }
    }

    @Override
    public Map<String, Double> getAnnualSales() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT YEAR(o.orderDate), SUM(o.totalAmount) FROM Order o GROUP BY YEAR(o.orderDate)";
            Query<Object[]> query = session.createQuery(hql, Object[].class);

            Map<String, Double> result = new HashMap<>();
            for (Object[] row : query.list()) {
                result.put("Year: " + row[0], (Double) row[1]);
            }

            session.getTransaction().commit();
            return result;
        } finally {
            session.close();
        }
    }
}
