package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductPage;
import utils.ConfigReader;
import utils.WaitUtils;

public class CheckoutTest extends BaseTest {

    @Test(description = "TC_007 - A logged-in user can place an order")
    public void TC_007_placeOrder() {
        LoginPage login = new LoginPage(driver).open();
        Assert.assertTrue(login.login(ConfigReader.get("username"), ConfigReader.get("password")),
                "Login is required before checkout");

        // login redirects to the home page, so continue there without reloading
        ProductPage products = new ProductPage(driver).waitUntilLoaded();
        products.addToCart(ConfigReader.get("product1"));

        CartPage cart = new CartPage(driver).waitUntilOpen().waitForItemCount(1);
        cart.checkout();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.placeOrder(
                ConfigReader.get("checkout.firstName"),
                ConfigReader.get("checkout.lastName"),
                ConfigReader.get("checkout.address"),
                ConfigReader.get("checkout.province"),
                ConfigReader.get("checkout.postCode"));

        Assert.assertTrue(checkout.isOrderConfirmed(), "Order confirmation message should be displayed");
        extentTest.info("Confirmation: " + checkout.confirmationText());
    }

    @Test(description = "TC_008 - Checkout with an empty cart does not proceed")
    public void TC_008_checkoutWithoutItems() {
        new ProductPage(driver).open();
        CartPage cart = new CartPage(driver).open();
        Assert.assertEquals(cart.itemCount(), 0, "Cart should start empty");

        if (cart.isCheckoutAvailable()) {
            cart.checkout();
            String alertText = WaitUtils.acceptAlertIfPresent(driver, 3);
            extentTest.info("Alert shown on empty checkout: " + alertText);
        }

        String url = driver.getCurrentUrl();
        Assert.assertFalse(url.contains("checkout") || url.contains("signin"),
                "Empty cart must not proceed to checkout/sign-in, but URL was " + url);
    }
}
