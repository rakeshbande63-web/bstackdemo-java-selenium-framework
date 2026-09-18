package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginTest extends BaseTest {

    @Test
    public void TC_001_validLogin() {
        LoginPage p = new LoginPage(driver).open();
        p.login(ConfigReader.get("username"), ConfigReader.get("password"));

        Assert.assertTrue(p.isLoggedIn(), "Valid login should succeed");
        extentTest.pass("[PASS] User logged in successfully");
    }

    @Test
    public void TC_002_invalidLogin() {
        LoginPage p = new LoginPage(driver).open();
        p.login("invalid_user", "invalid_password");

        Assert.assertFalse(p.isLoggedIn(), "Invalid login should not succeed");
        extentTest.pass("[PASS] Invalid login was rejected");
    }

    @Test
    public void TC_003_emptyLogin() {
        LoginPage p = new LoginPage(driver).open();
        p.login("", "");

        Assert.assertFalse(p.isLoggedIn(), "Empty login should not succeed");
        extentTest.pass("[PASS] Empty login was rejected");
    }
}
