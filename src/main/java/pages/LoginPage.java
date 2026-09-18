package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

/**
 * Page Object for the BrowserStack Demo sign-in page.
 *
 * The demo application uses react-select controls for username/password.
 */
public class LoginPage {

    private final WebDriver d;

    private final By signIn = By.id("signin");
    private final By username = By.id("username");
    private final By password = By.id("password");

    // BrowserStack demo uses react-select option ids for the first/default credentials.
    private final By demoUserOption = By.id("react-select-2-option-0-0");
    private final By demoPasswordOption = By.id("react-select-3-option-0-0");

    private final By loginButton = By.id("login-btn");
    private final By logout = By.id("logout");

    public LoginPage(WebDriver d) {
        this.d = d;
    }

    public LoginPage open() {
        d.get("https://bstackdemo.com/");
        WaitUtils.ready(d);
        WaitUtils.clickable(d, signIn).click();
        WaitUtils.visible(d, username);
        return this;
    }

    public void login(String user, String pwd) {
        selectUsername(user);
        selectPassword(pwd);
        WaitUtils.clickable(d, loginButton).click();
        WaitUtils.ready(d);
    }

    /**
     * Logs in from the already-open sign-in page. Useful when checkout redirects
     * directly to the sign-in screen.
     */
    public void loginFromCurrentPage(String user, String pwd) {
        WaitUtils.visible(d, username);
        login(user, pwd);
    }

    private void selectUsername(String value) {
        WaitUtils.clickable(d, username).click();

        if ("demouser".equalsIgnoreCase(value)) {
            WaitUtils.clickable(d, demoUserOption).click();
        } else {
            // Supports negative/empty scenarios without selecting the valid option.
            WaitUtils.visible(d, username).sendKeys(value);
        }
    }

    private void selectPassword(String value) {
        WaitUtils.clickable(d, password).click();

        if ("testingisfun99".equals(value)) {
            WaitUtils.clickable(d, demoPasswordOption).click();
        } else {
            WaitUtils.visible(d, password).sendKeys(value);
        }
    }

    public boolean isLoggedIn() {
        return !d.findElements(logout).isEmpty()
                && d.findElements(logout).stream().anyMatch(e -> e.isDisplayed());
    }
}
