package repository.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public interface SalesReportDao {
    Map<String, Double> getDailySales(LocalDate date) throws SQLException, ClassNotFoundException;

    Map<String, Double> getMonthlySales(int month) throws SQLException, ClassNotFoundException;

    Map<String, Double> getAnnualSales() throws SQLException, ClassNotFoundException;
}
