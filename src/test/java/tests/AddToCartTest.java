package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.ProductPage;

public class AddToCartTest extends BaseTest {
    @Test
    public void TC_004_addSingleItem() {
        ProductPage p = new ProductPage(driver);
        p.open();
        Assert.assertTrue(p.productCount() > 0, "Catalog should contain products");
        p.addFirstProduct();
        extentTest.pass("[PASS] First product added to cart");
    }

    @Test
    public void TC_005_addMultipleItems() {
        ProductPage p = new ProductPage(driver);
        p.open();
        p.addThreeProducts();
        CartPage c = new CartPage(driver);
        Assert.assertTrue(c.itemCount() >= 3, "Cart should contain at least two products");
        extentTest.pass("[PASS] Multiple products added to cart");
    }

    @Test
    public void TC_006_removeItem() {
        ProductPage p = new ProductPage(driver);
        p.open();
        p.addFirstProduct();
        CartPage c = new CartPage(driver);
        int before = c.itemCount();
        Assert.assertTrue(before > 0, "Cart should contain an item before removal");
        c.removeFirstItem();
        Assert.assertTrue(c.itemCount() < before, "Cart count should decrease");
        extentTest.pass("[PASS] Cart item removed successfully");
    }
}
