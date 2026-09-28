package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/** The sliding cart panel ("float cart") and its bag icon. */
public class CartPage {

    private final WebDriver d;

    // Class-token matches (' bag ', ' buy-btn ') avoid also matching bag__quantity and
    // shelf-item__buy-btn, which a plain contains(@class,'bag' / 'buy-btn') would hit.
    private final By bagIcon = By.xpath("//*[contains(concat(' ',normalize-space(@class),' '),' bag ')]");
    private final By cartPanelOpen = By.xpath("//*[contains(concat(' ',normalize-space(@class),' '),' float-cart--open ')]");
    private final By closeButton = By.xpath("//*[contains(@class,'float-cart__close-btn')]");
    private final By cartItems = By.xpath("//div[contains(@class,'float-cart__shelf-container')]"
            + "/div[.//div[contains(@class,'shelf-item__details')]]");
    private final By cartItemTitles = By.xpath("//div[contains(@class,'float-cart__shelf-container')]"
            + "//div[contains(@class,'shelf-item__details')]/p[contains(@class,'title')]");
    private final By removeButtons = By.xpath("//div[contains(@class,'float-cart__shelf-container')]"
            + "//*[contains(@class,'shelf-item__del')]");
    private final By checkoutButton = By.xpath("//*[contains(concat(' ',normalize-space(@class),' '),' buy-btn ')]");

    public CartPage(WebDriver d) {
        this.d = d;
    }

    public boolean isOpen() {
        return !d.findElements(cartPanelOpen).isEmpty();
    }

    public CartPage waitUntilOpen() {
        WaitUtils.visible(d, cartPanelOpen);
        return this;
    }

    /** Opens the cart via the bag icon if it is not open already. */
    public CartPage open() {
        if (!isOpen()) {
            WaitUtils.click(d, bagIcon);
        }
        return waitUntilOpen();
    }

    public CartPage close() {
        if (isOpen()) {
            WaitUtils.click(d, closeButton);
            WaitUtils.invisible(d, cartPanelOpen);
        }
        return this;
    }

    public int itemCount() {
        return d.findElements(cartItems).size();
    }

    public List<String> itemTitles() {
        List<String> titles = new ArrayList<>();
        for (WebElement e : d.findElements(cartItemTitles)) {
            titles.add(e.getText().trim());
        }
        return titles;
    }

    /** Waits until the cart shows exactly the expected number of items. */
    public CartPage waitForItemCount(int expected) {
        WaitUtils.waitFor(d).until(ExpectedConditions.numberOfElementsToBe(cartItems, expected));
        return this;
    }

    public void removeFirstItem() {
        int before = itemCount();
        WaitUtils.click(d, removeButtons);
        waitForItemCount(before - 1);
    }

    public boolean isCheckoutAvailable() {
        return WaitUtils.isDisplayedNow(d, checkoutButton);
    }

    public void checkout() {
        WaitUtils.click(d, checkoutButton);
    }
}
