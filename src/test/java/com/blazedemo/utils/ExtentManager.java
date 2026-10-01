package com.blazedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            try {
                Files.createDirectories(Path.of("test-output"));
            } catch (Exception ignored) {
                // reporter surfaces the problem when it tries to write
            }
            ExtentSparkReporter reporter = new ExtentSparkReporter("test-output/ExtentReport.html");
            reporter.config().setReportName("BlazeDemo Automation Report");
            reporter.config().setDocumentTitle("Flight Booking Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Application", "blazedemo.com");
            extent.setSystemInfo("Framework", "Java + Selenium + TestNG + POM");
        }
        return extent;
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
