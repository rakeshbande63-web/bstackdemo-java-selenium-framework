package tests.ui;

import base.UiBaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ContactListPage;
import pages.LoginPage;
import utils.ConfigReader;

/**
 * Selenium smoke coverage over the same app, complementing the RestAssured API suite:
 * sign up a new UI user, add a contact through the real form, confirm it appears in the
 * table, then log out. Each @Test is independent (its own browser session via UiBaseTest),
 * so a failure in one does not skip the others.
 */
public class ContactUiTest extends UiBaseTest {

    @Test(description = "TC_UI_001 - Invalid login is rejected and stays on the login page")
    public void TC_UI_001_invalidLoginRejected() {
        LoginPage login = new LoginPage(driver).open();
        login.loginExpectingFailure("no-such-user@fake.com", "wrongPassword1");

        Assert.assertTrue(login.isOnLoginPage(), "Invalid credentials should not leave the login page");
    }

    @Test(description = "TC_UI_002 - Sign up creates a user and lands on the contact list")
    public void TC_UI_002_signUpNewUser() {
        String email = "ui." + System.currentTimeMillis() + "@fake.com";

        LoginPage login = new LoginPage(driver).open();
        ContactListPage list = login.goToSignUp().signUp("UI", "Tester", email, "myPassword1");

        Assert.assertTrue(list.waitUntilLoaded().isDisplayed(), "New user should land on the Contact List page");
        extentTest.info("Signed up: " + email);
    }

    @Test(description = "TC_UI_003 - Add a contact through the UI and see it in the list")
    public void TC_UI_003_addContactThroughForm() {
        String email = "ui.contact." + System.currentTimeMillis() + "@fake.com";

        LoginPage login = new LoginPage(driver).open();
        ContactListPage list = login.goToSignUp()
                .signUp("UI", "Tester", email, "myPassword1")
                .waitUntilLoaded();

        int before = list.contactCount();
        list.goToAddContact().submitContact(
                "John", "Doe", "1970-01-01", "jdoe@fake.com", "8005555555",
                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");

        list.waitForContactCount(before + 1);
        Assert.assertEquals(list.contactCount(), before + 1, "Contact list should grow by exactly one");
        Assert.assertTrue(list.contactRowsText().stream().anyMatch(r -> r.contains("John") && r.contains("Doe")),
                "New contact John Doe should appear in the table");
    }

    @Test(description = "TC_UI_004 - Logout returns to the login page")
    public void TC_UI_004_logout() {
        String email = "ui.logout." + System.currentTimeMillis() + "@fake.com";

        LoginPage login = new LoginPage(driver).open();
        ContactListPage list = login.goToSignUp().signUp("UI", "Tester", email, "myPassword1").waitUntilLoaded();

        LoginPage backToLogin = list.logout();
        Assert.assertTrue(backToLogin.isOnLoginPage(), "Logout should return the user to the login page");
    }
}
