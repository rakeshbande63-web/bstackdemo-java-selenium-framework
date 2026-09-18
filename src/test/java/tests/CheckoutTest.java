package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.ProductPage;
import pages.LoginPage;
import utils.ConfigReader;

public class CheckoutTest extends BaseTest {

    @Test
    public void TC_007_placeOrder() {
        ProductPage p = new ProductPage(driver);
        p.open();
        p.addFirstProduct();

        CartPage cart = new CartPage(driver);
        cart.checkout();

        // BrowserStack Demo redirects an unauthenticated checkout to Sign In.
        LoginPage login = new LoginPage(driver);
        if (!login.isLoggedIn()) {
            login.loginFromCurrentPage(
                    ConfigReader.get("username"),
                    ConfigReader.get("password")
            );
        }

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.placeOrder(
                "Rakesh",
                "Bande",
                "123 Test Street",
                "Maharashtra",
                "411001"
        );

        Assert.assertTrue(
                checkout.isOrderConfirmed(),
                "Order confirmation should be displayed after checkout"
        );

        extentTest.pass("[PASS] Order placed successfully");
    }

    @Test
    public void TC_008_checkoutWithoutItems() {
        ProductPage p = new ProductPage(driver);
        p.open();
        p.openCart();

        CartPage cart = new CartPage(driver);

        Assert.assertTrue(cart.isEmpty(),
                "Cart should be empty when no products have been added");

        Assert.assertFalse(cart.isCheckoutVisible(),
                "Checkout should not be available for an empty cart");

        extentTest.pass("[PASS] Empty cart cannot proceed to checkout");
    }
}
