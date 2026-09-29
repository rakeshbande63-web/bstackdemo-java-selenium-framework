package base;

import api.ContactApiClient;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import reporting.ExtentManager;

import java.lang.reflect.Method;

/**
 * Base for the RestAssured suite. One ContactApiClient per test class so the
 * auth token and contactId survive across the dependsOnMethods chain, the same
 * way {{token}} and {{contactId}} survive across requests in the Postman collection.
 */
public class ApiBaseTest {

    protected ContactApiClient api;
    protected ExtentTest extentTest;

    @BeforeClass(alwaysRun = true)
    public void createClient() {
        api = new ContactApiClient();
    }

    @BeforeMethod(alwaysRun = true)
    public void startReportEntry(Method method) {
        extentTest = ExtentManager.getExtent().createTest(method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void logResult(ITestResult result) {
        if (extentTest == null) {
            return;
        }
        if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.pass("Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("Skipped - a prior step in the chain did not complete: "
                    + (result.getThrowable() != null ? result.getThrowable().getMessage() : ""));
        } else {
            extentTest.fail(result.getThrowable());
        }
    }
}
