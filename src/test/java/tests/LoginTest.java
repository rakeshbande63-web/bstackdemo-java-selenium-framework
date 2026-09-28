package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginTest extends BaseTest {

    @Test(description = "TC_001 - Valid login shows the Logout link")
    public void TC_001_validLogin() {
        LoginPage login = new LoginPage(driver).open();
        boolean loggedIn = login.login(ConfigReader.get("username"), ConfigReader.get("password"));

        Assert.assertTrue(loggedIn, "Valid credentials should log the user in (Logout link visible)");
       // Assert.assertFalse(login.isOnSignInPage(), "User should be redirected away from the sign-in page");
        extentTest.pass("User logged in successfully");
    }

    @Test(description = "TC_002 - Invalid credentials are rejected")
    public void TC_002_invalidLogin() {
        LoginPage login = new LoginPage(driver).open();
        boolean loggedIn = login.login("invalid_user", "invalid_password");

        Assert.assertFalse(loggedIn, "Invalid credentials must not log the user in");
        Assert.assertTrue(login.isOnSignInPage(), "User should remain on the sign-in page");
        extentTest.info("Error shown: " + login.getErrorText());
    }

    @Test(description = "TC_003 - Empty credentials are rejected")
    public void TC_003_emptyLogin() {
        LoginPage login = new LoginPage(driver).open();
        boolean loggedIn = login.login("", "");

        Assert.assertFalse(loggedIn, "Empty credentials must not log the user in");
        Assert.assertTrue(login.isOnSignInPage(), "User should remain on the sign-in page");
        extentTest.info("Error shown: " + login.getErrorText());
    }
}
