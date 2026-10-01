package com.blazedemo.base;

import com.aventstack.extentreports.ExtentTest;
import com.blazedemo.pages.HomePage;
import com.blazedemo.utils.DriverFactory;
import com.blazedemo.utils.ExtentManager;
import com.blazedemo.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

/**
 * Per the capstone doc's Step 9: driver setup lives here (@BeforeMethod/@AfterMethod),
 * with optional screenshot capture on failure, so test classes stay focused on the flow.
 */
public class BaseTest {

    protected WebDriver driver;
    protected HomePage homePage;
    protected ExtentTest extentTest;

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        ExtentManager.getExtent();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        driver = DriverFactory.createDriver();
        homePage = new HomePage(driver);
        extentTest = ExtentManager.getExtent().createTest(method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            String status;
            if (result.getStatus() == ITestResult.SUCCESS) {
                status = "PASS";
                if (extentTest != null) extentTest.pass("Passed");
            } else if (result.getStatus() == ITestResult.SKIP) {
                status = "SKIP";
                if (extentTest != null) extentTest.skip(result.getThrowable());
            } else {
                status = "FAIL";
                if (extentTest != null) extentTest.fail(result.getThrowable());
            }
            String name = result.getMethod().getMethodName();
            String path = ScreenshotUtil.capture(driver, name + "_" + status);
            if (path != null && extentTest != null) {
                String base64 = ScreenshotUtil.toBase64(path);
                if (!base64.isEmpty()) {
                    extentTest.addScreenCaptureFromBase64String(base64, name + " - " + status);
                }
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        ExtentManager.flush();
    }
}
