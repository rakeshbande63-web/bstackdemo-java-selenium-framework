package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.WaitUtils;

/** Home page with the product catalogue. */
public class ProductPage {

    private final WebDriver d;

    private final By addToCartButtons = By.xpath("//*[contains(@class,'shelf-item__buy-btn')]");

    public ProductPage(WebDriver d) {
        this.d = d;
    }

    public ProductPage open() {
        d.get(ConfigReader.get("baseUrl"));
        WaitUtils.ready(d);
        return waitUntilLoaded();
    }

    /** Products are loaded asynchronously by the React app, so wait for the first one. */
    public ProductPage waitUntilLoaded() {
        WaitUtils.visible(d, addToCartButtons);
        return this;
    }

    public int productCount() {
        return d.findElements(addToCartButtons).size();
    }

    /**
     * XPath scoped to one product card: finds the card whose title matches the name and
     * returns that card's own "Add to cart" button.
     */
    public static By addToCartButton(String productName) {
        return By.xpath("//div[contains(concat(' ',normalize-space(@class),' '),' shelf-item ')]"
                + "[.//p[normalize-space()='" + productName + "']]"
                + "//*[contains(@class,'shelf-item__buy-btn')]");
    }

    /** Clicks "Add to cart" for the named product (the cart slides open afterwards). */
    public void addToCart(String productName) {
        WaitUtils.click(d, addToCartButton(productName));
    }
}
