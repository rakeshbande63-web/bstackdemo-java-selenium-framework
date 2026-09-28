package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.WaitUtils;

/**
 * Sign-in page of bstackdemo.com.
 * Username and password are react-select dropdowns: click the container, type the value,
 * press TAB to select the highlighted option.
 */
public class LoginPage {

    private static final int OUTCOME_WAIT_SECONDS = 8;

    private final WebDriver d;

    private final By signInLink = By.xpath("//*[@id='signin']");
    private final By usernameBox = By.xpath("//div[@id='username']");
    private final By usernameInput = By.xpath("//div[@id='username']//input");
    private final By passwordBox = By.xpath("//div[@id='password']");
    private final By passwordInput = By.xpath("//div[@id='password']//input");
    private final By loginButton = By.xpath("//button[@id='login-btn']");
    private final By logoutLink = By.xpath("//*[@id='logout' or normalize-space(text())='Logout']");
    private final By errorMessage = By.xpath("//h3[contains(@class,'api-error')]");

    public LoginPage(WebDriver d) {
        this.d = d;
    }

    /** Opens the home page and navigates to the sign-in form. */
    public LoginPage open() {
        d.get(ConfigReader.get("baseUrl"));
        WaitUtils.ready(d);
        WaitUtils.click(d, signInLink);
        WaitUtils.visible(d, usernameBox);
        WaitUtils.visible(d, passwordBox);
        return this;
    }

    /**
     * Fills the form, clicks Login and waits for the outcome.
     * A null/empty value leaves that field untouched.
     *
     * @return true when the user ended up logged in (Logout link visible)
     */
    public boolean login(String user, String pwd) {
        selectValue(usernameBox, usernameInput, user);
        selectValue(passwordBox, passwordInput, pwd);
        WaitUtils.click(d, loginButton);
        WaitUtils.waitForAny(d, OUTCOME_WAIT_SECONDS, logoutLink, errorMessage);
        return isLoggedIn();
    }

    private void selectValue(By box, By input, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        WaitUtils.click(d, box);
        WaitUtils.type(d, input, value, Keys.TAB);
    }

    public boolean isLoggedIn() {
        return WaitUtils.isDisplayedNow(d, logoutLink);
    }

    public boolean isOnSignInPage() {
        return d.getCurrentUrl().contains("signin");
    }

    /** Visible validation/error text, or an empty string when none is shown. */
    public String getErrorText() {
        return WaitUtils.isDisplayedNow(d, errorMessage) ? d.findElement(errorMessage).getText() : "";
    }
}
