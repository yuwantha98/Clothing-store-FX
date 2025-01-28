package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import repository.DaoFactory;
import repository.service.SalesReportDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class SalesReportFormController {

    @FXML
    public BarChart<String, Double> salesChart;
    @FXML
    public DatePicker datePicker;

    SalesReportDao salesReportDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SALESREPORT);

    @FXML
    public void getDailyReport(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            System.out.println("Please select a date!");
            return;
        }

        Map<String, Double> dailyReport = salesReportDao.getDailySales(selectedDate);
        loadChartData(dailyReport, "Daily Report");
    }

    @FXML
    public void getMonthlyReport(ActionEvent actionEvent) {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            System.out.println("Please select a date!");
            return;
        }

        try {
            Map<String, Double> monthlyReport = salesReportDao.getMonthlySales(selectedDate.getMonthValue());
            loadChartData(monthlyReport, "Monthly Report");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error fetching monthly report: " + e.getMessage());
        }
    }

    @FXML
    public void getAnnualReport(ActionEvent actionEvent) {
        try {
            Map<String, Double> annualReport = salesReportDao.getAnnualSales();
            loadChartData(annualReport, "Annual Report");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error fetching annual report: " + e.getMessage());
        }
    }

    private void loadChartData(Map<String, Double> data, String title) {
        salesChart.getData().clear();
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName(title);

        data.forEach((period, value) -> {
            series.getData().add(new XYChart.Data<>(period, value));
        });

        salesChart.getData().add(series);
    }
}
