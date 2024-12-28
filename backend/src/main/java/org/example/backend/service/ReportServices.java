package org.example.backend.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import org.example.backend.dao.AccountDAO;
import org.example.backend.dao.StatisticsDAO;
import org.example.backend.entity.Account;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServices {

    private AccountDAO accountDAO;
    private StatisticsDAO statisticsDAO;

    public ReportServices(AccountDAO accountDAO, StatisticsDAO statisticsDAO) {
        this.accountDAO = accountDAO;
        this.statisticsDAO = statisticsDAO;
    }

    public String exportReport(String report) throws FileNotFoundException, JRException {
        if (report.equals("users")) {

            List<Account> accounts = accountDAO.listAll();
            generateReport(new JRBeanCollectionDataSource(accounts), "users_report.jrxml");

        } else if (report.equals("lots")) {

            List<Map<String, Object>> lots = statisticsDAO.getParkingSlotsWithRevenueAndOccupancy(Integer.MAX_VALUE);
            generateReport(new JRBeanCollectionDataSource(lots), "lots_report.jrxml");

        } else if (report.equals("topUsers")) {

            List<Map<String, Object>> topUsers = statisticsDAO.getTopUsersWithMostReservations(Integer.MAX_VALUE);
            generateReport(new JRBeanCollectionDataSource(topUsers), "topUsers_report.jrxml");

        } else {
            return "Report not found";
        }

        return "Report exported";
    }

    private void generateReport(JRBeanCollectionDataSource dataSource, String fileName) throws JRException, FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:reports/"+fileName);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "admin");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, fileName.replace("jrxml", "pdf"));
    }
}
