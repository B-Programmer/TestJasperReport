package main.java;


import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import com.lowagie.text.DocumentException;
//import com.itextpdf.text.DocumentException;
import org.joda.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;


public class JavaCallJasperReport {

    public static void main(String[] args) throws JRException,
            ClassNotFoundException, SQLException, DocumentException {

        String reportSrcFile = "/home/longbridge/Desktop/Longbridge/TestJasperReport/Reports/ao_report.jrxml";

        // First, compile jrxml file.
        JasperReport jasperReport =    JasperCompileManager.compileReport(reportSrcFile);

        Connection conn = MySqlConnUtil.getMySQLConnection();

        // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();

        Date endDate = new Date();
        Date startDate = LocalDate.parse("2000-01-01").toDate();
        String  branchCode = "002";
        String reportType = "aco";

        parameters.put("start_date", startDate);
        parameters.put("end_date", endDate);
        parameters.put("branch_code", branchCode);
        parameters.put("report_type", reportType);

        //Execute the compiled File
        JasperPrint print = JasperFillManager.fillReport(jasperReport,
                parameters, conn);

        // Make sure the output directory exists.
        String reportOutPath = "/home/longbridge/Desktop/Longbridge/TestJasperReport/JasperOutput";
        File outDir = new File(reportOutPath);
        if(!outDir.exists()) {
            outDir.mkdirs();
        }

        // PDF Exportor.
        JRPdfExporter exporter = new JRPdfExporter();

        ExporterInput exporterInput = new SimpleExporterInput(print);
        // ExporterInput
        exporter.setExporterInput(exporterInput);

        // ExporterOutput
        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                reportOutPath + "/AO_Report.pdf");
        // Output
        exporter.setExporterOutput(exporterOutput);

        //
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        System.out.print("Done!!!!!");
    }
}