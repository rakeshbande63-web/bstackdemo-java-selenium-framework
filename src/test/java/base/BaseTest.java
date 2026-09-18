package base;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import reporting.ExtentManager;
import utils.ConfigReader;
import utils.ScreenshotUtil;
import utils.WebDriverFactory;

import java.lang.reflect.Method;

public class BaseTest {

    protected WebDriver driver;
    protected ExtentTest extentTest;

    @BeforeSuite(alwaysRun = true)
    public void suite() {
        ExtentManager.getExtent();
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(Method method) {
        driver = WebDriverFactory.createDriver();
        extentTest = ExtentManager.getExtent().createTest(method.getName());
        extentTest.info("[INFO] Browser session started");
        driver.get(ConfigReader.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void teardown(ITestResult result) {
        try {
            if (result.isSuccess()) {
                extentTest.pass("[PASS] Test completed successfully");
            } else {
                extentTest.fail(result.getThrowable());
            }

            if (driver != null) {
                String status = result.isSuccess() ? "PASS" : "FAIL";
                String path = ScreenshotUtil.capture(
                        driver,
                        result.getMethod().getMethodName() + "_" + status
                );

                if (path != null) {
                    String base64 = ScreenshotUtil.toBase64(path);
                    if (!base64.isEmpty()) {
                        extentTest.addScreenCaptureFromBase64String(
                                base64,
                                result.getMethod().getMethodName() + " - " + status
                        );
                    }
                }
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void end() {
        ExtentManager.flush();
    }
}
