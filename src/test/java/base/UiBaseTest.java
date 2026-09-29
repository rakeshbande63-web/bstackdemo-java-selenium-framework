package base;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import reporting.ExtentManager;
import utils.ScreenshotUtil;
import utils.WebDriverFactory;

import java.lang.reflect.Method;

public class UiBaseTest {

    protected WebDriver driver;
    protected ExtentTest extentTest;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        driver = WebDriverFactory.createDriver();
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
}
