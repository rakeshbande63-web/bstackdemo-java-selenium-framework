package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

/**
 * Floating cart Page Object.
 */
public class CartPage {

    private final WebDriver d;

    private final By items = By.cssSelector(".float-cart__content .shelf-item");
    private final By remove = By.cssSelector(".float-cart__content .shelf-item__del");

    // BrowserStack Demo uses .buy-btn for Checkout in the floating cart.
    private final By checkout = By.cssSelector(".float-cart__content .buy-btn");

    // Message displayed when the cart is empty. Use normalize-space(.) to
    // ignore surrounding whitespace.
    private final By emptyMessage = By.xpath("//*[contains(normalize-space(.),'Add some products in the bag')]");

    public CartPage(WebDriver d) {
        this.d = d;
    }

    public int itemCount() {
        return d.findElements(items).size();
    }

    public void removeFirstItem() {
        WaitUtils.clickable(d, remove).click();
        WaitUtils.ready(d);
    }

    public void checkout() {
        WaitUtils.clickable(d, checkout).click();
        WaitUtils.ready(d);
    }

    public boolean isEmpty() {
        // Cart is considered empty when either no item elements are visible
        // or the empty-message element is visible.
        boolean anyItemVisible = WaitUtils.visibleIfPresent(d, items);
        boolean emptyMsgVisible = WaitUtils.visibleIfPresent(d, emptyMessage);
        return !anyItemVisible || emptyMsgVisible;
    }

    public boolean isCheckoutVisible() {
        return WaitUtils.visibleIfPresent(d, checkout);
    }
}
