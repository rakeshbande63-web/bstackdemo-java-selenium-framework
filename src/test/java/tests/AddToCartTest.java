package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.ProductPage;
import utils.ConfigReader;

import java.util.List;

public class AddToCartTest extends BaseTest {

    @Test(description = "TC_004 - Add a single item to the cart")
    public void TC_004_addSingleItem() {
        String product = ConfigReader.get("product1");

        ProductPage products = new ProductPage(driver).open();
        Assert.assertTrue(products.productCount() > 0, "Catalogue should contain products");

        products.addToCart(product);
        CartPage cart = new CartPage(driver).waitUntilOpen().waitForItemCount(1);

        Assert.assertEquals(cart.itemTitles(), List.of(product), "Cart should contain exactly the added product");
    }

    @Test(description = "TC_005 - Add three different items to the cart")
    public void TC_005_addMultipleItems() {
        List<String> expected = List.of(
                ConfigReader.get("product1"),
                ConfigReader.get("product2"),
                ConfigReader.get("product3"));

        ProductPage products = new ProductPage(driver).open();
        CartPage cart = new CartPage(driver);
        for (String name : expected) {
            products.addToCart(name);
            cart.waitUntilOpen().close();   // cart slides open after each add; close it to reach the next product
        }

        cart.open().waitForItemCount(expected.size());
        List<String> actual = cart.itemTitles();
        Assert.assertEquals(actual.size(), expected.size(), "Cart should hold exactly 3 items: " + actual);
        Assert.assertTrue(actual.containsAll(expected), "Cart should contain " + expected + " but had " + actual);
    }

    @Test(description = "TC_006 - Removing an item decreases the cart count by one")
    public void TC_006_removeItem() {
        ProductPage products = new ProductPage(driver).open();
        CartPage cart = new CartPage(driver);

        products.addToCart(ConfigReader.get("product1"));
        cart.waitUntilOpen().close();
        products.addToCart(ConfigReader.get("product2"));
        cart.waitUntilOpen().waitForItemCount(2);

        int before = cart.itemCount();
        cart.removeFirstItem();

        Assert.assertEquals(cart.itemCount(), before - 1, "Cart count should decrease by one after removal");
    }
}
