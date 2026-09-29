package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

/**
 * /addUser (sign-up) page.
 * Confirmed locators (same public Serenity page object as LoginPage): firstName, lastName,
 * email, password, submit.
 */
public class AddUserPage {

    private final WebDriver d;

    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By email = By.id("email");
    private final By password = By.id("password");
    private final By submit = By.id("submit");

    public AddUserPage(WebDriver d) {
        this.d = d;
    }

    public AddUserPage waitForForm() {
        WaitUtils.visible(d, firstName);
        return this;
    }

    public ContactListPage signUp(String first, String last, String userEmail, String userPassword) {
        waitForForm();
        WaitUtils.fill(d, firstName, first);
        WaitUtils.fill(d, lastName, last);
        WaitUtils.fill(d, email, userEmail);
        WaitUtils.fill(d, password, userPassword);
        WaitUtils.click(d, submit);
        return new ContactListPage(d);
    }
}
