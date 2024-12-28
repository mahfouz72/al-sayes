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

    public byte[] exportReport(String report) throws FileNotFoundException, JRException {
        byte[] reportBytes = null;
        if (report.equals("users")) {

            List<Account> accounts = accountDAO.listAll();
            reportBytes = generateReport(new JRBeanCollectionDataSource(accounts), "users_report.jrxml");

        } else if (report.equals("lots")) {

            List<Map<String, Object>> lots = statisticsDAO.getParkingSlotsWithRevenueAndOccupancy(Integer.MAX_VALUE);
            reportBytes = generateReport(new JRBeanCollectionDataSource(lots), "lots_report.jrxml");

        } else if (report.equals("topUsers")) {

            List<Map<String, Object>> topUsers = statisticsDAO.getTopUsersWithMostReservations(Integer.MAX_VALUE);
            reportBytes = generateReport(new JRBeanCollectionDataSource(topUsers), "topUsers_report.jrxml");

        } else {
            return null;
        }

        return reportBytes;
    }

    private byte[] generateReport(JRBeanCollectionDataSource dataSource, String fileName) throws JRException, FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:reports/"+fileName);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "admin");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
