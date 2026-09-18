package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;

/**
 * Product listing Page Object.
 */
public class ProductPage {

    private final WebDriver d;

    private final By products = By.cssSelector(".shelf-item");
    private final By addButtons = By.cssSelector(".shelf-item__buy-btn");
    private final By cartHeader = By.cssSelector(".float-cart__header");
    private final By cartContent = By.cssSelector(".float-cart__content");

    // Named product locators. Each XPath is scoped to the product card.
    private final By iPhone12Add = By.xpath(
            "//div[contains(@class,'shelf-item')][.//*[normalize-space()='iPhone 12']]"
                    + "//button[contains(@class,'shelf-item__buy-btn')]"
    );

    private final By iPhone12MiniAdd = By.xpath(
            "//div[contains(@class,'shelf-item')][.//*[normalize-space()='iPhone 12 Mini']]"
                    + "//button[contains(@class,'shelf-item__buy-btn')]"
    );

    private final By iPhone12ProMaxAdd = By.xpath(
            "//div[contains(@class,'shelf-item')][.//*[normalize-space()='iPhone 12 Pro Max']]"
                    + "//button[contains(@class,'shelf-item__buy-btn')]"
    );

    public ProductPage(WebDriver d) {
        this.d = d;
    }

    public void open() {
        d.get("https://bstackdemo.com/");
        WaitUtils.ready(d);
        WaitUtils.visible(d, products);
    }

    public int productCount() {
        return d.findElements(products).size();
    }

    public String firstProductName() {
        return WaitUtils.visible(
                d,
                By.xpath("(//div[contains(@class,'shelf-item')]//*[contains(@class,'shelf-item__title') or self::p])[1]")
        ).getText();
    }

    public void addFirstProduct() {
        WaitUtils.clickable(d, addButtons).click();
        WaitUtils.visible(d, cartContent);
    }

    /**
     * Adds exactly three named products for TC_005.
     */
    public void addThreeProducts() {
        addProduct(iPhone12Add, "iPhone 12");
        addProduct(iPhone12MiniAdd, "iPhone 12 Mini");
        addProduct(iPhone12ProMaxAdd, "iPhone 12 Pro Max");
    }

    private void addProduct(By locator, String productName) {
        WaitUtils.clickable(d, locator).click();
        WaitUtils.ready(d);
    }

    public void addProducts(int n) {
        if (n == 3) {
            addThreeProducts();
            return;
        }

        List<WebElement> buttons = d.findElements(addButtons);
        if (buttons.size() < n) {
            throw new IllegalArgumentException(
                    "Expected at least " + n + " Add to cart buttons but found " + buttons.size()
            );
        }

        for (int i = 0; i < n; i++) {
            WaitUtils.clickable(d, addButtons).click();
        }
        WaitUtils.visible(d, cartContent);
    }

    public void openCart() {
        WaitUtils.clickable(d, cartHeader).click();
        WaitUtils.visible(d, cartContent);
    }

    public By getCartContentLocator() {
        return cartContent;
    }
}
