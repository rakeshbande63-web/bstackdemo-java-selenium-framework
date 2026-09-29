package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.WaitUtils;

/**
 * /login page.
 * Confirmed locators (matches a public Serenity BDD page object built against this exact
 * app): email, password, submit, signup.
 */
public class LoginPage {

    private final WebDriver d;

    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By submitButton = By.id("submit");
    private final By signupLink = By.id("signup");
    private final By errorMessage = By.xpath("//*[contains(@class,'error') or contains(normalize-space(text()),'Incorrect')]");

    public LoginPage(WebDriver d) {
        this.d = d;
    }

    public LoginPage open() {
        d.get(ConfigReader.get("baseUrl") + "/login");
        WaitUtils.ready(d);
        WaitUtils.visible(d, emailInput);
        return this;
    }

    public ContactListPage login(String email, String password) {
        WaitUtils.fill(d, emailInput, email);
        WaitUtils.fill(d, passwordInput, password);
        WaitUtils.click(d, submitButton);
        return new ContactListPage(d);
    }

    /** Attempts login and stays on this page when it fails (invalid-credentials scenarios). */
    public LoginPage loginExpectingFailure(String email, String password) {
        WaitUtils.fill(d, emailInput, email);
        WaitUtils.fill(d, passwordInput, password);
        WaitUtils.click(d, submitButton);
        WaitUtils.waitForAny(d, 8, errorMessage);
        return this;
    }

    public AddUserPage goToSignUp() {
        WaitUtils.click(d, signupLink);
        return new AddUserPage(d);
    }

    public boolean isOnLoginPage() {
        // URL-based checks are unreliable here: logout can redirect to '/' rather than
        // '/login', and both serve the same login form. Wait for the form itself instead.
        return WaitUtils.waitForAny(d, 8, emailInput);
    }

    public String getErrorText() {
        return WaitUtils.isDisplayedNow(d, errorMessage) ? d.findElement(errorMessage).getText() : "";
    }
}
