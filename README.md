# bstackdemo Java Selenium POM Framework

Java 17 + Selenium WebDriver + TestNG + Maven + ExtentReports test framework for
<https://bstackdemo.com/>, built with the Page Object Model and XPath locators.

## Test cases

| ID | Scenario | What is asserted |
|----|----------|------------------|
| TC_001 | Valid login | `demouser` / `testingisfun99` -> Logout link visible, left the sign-in page |
| TC_002 | Invalid login | Not logged in, still on the sign-in page |
| TC_003 | Empty login | Not logged in, still on the sign-in page |
| TC_004 | Add single item | Cart contains exactly the product that was added |
| TC_005 | Add multiple items | Cart holds exactly the 3 configured products |
| TC_006 | Remove item | Cart count drops by one |
| TC_007 | Place order | Logged in -> add item -> checkout -> "successfully placed" confirmation |
| TC_008 | Checkout with empty cart | Cart is empty and the app does not proceed to checkout/sign-in |

## Project layout

```
src/main/java/pages      LoginPage, ProductPage, CartPage, CheckoutPage  (XPath locators)
src/main/java/utils      ConfigReader, WaitUtils, WebDriverFactory, ScreenshotUtil
src/main/java/reporting  ExtentManager
src/main/resources       config.properties
src/test/java/base       BaseTest (driver lifecycle, report, screenshots)
src/test/java/tests      LoginTest, AddToCartTest, CheckoutTest
testng.xml               suite definition (used by Maven Surefire)
```

## Run

Requires JDK 17+, Maven 3.8+ and Google Chrome. The matching chromedriver is downloaded automatically.

```
mvn clean test                       # visible browser
mvn clean test -Dheadless=true       # headless
mvn clean test -Dusername=fav_user   # any config.properties key can be overridden
```

In Eclipse/IntelliJ: right-click `testng.xml` -> Run As -> TestNG Suite.

## Locator notes

* Class checks use whole-token matches where a substring would be ambiguous, e.g. the cart
  Checkout button is `contains(concat(' ',normalize-space(@class),' '),' buy-btn ')` because a plain
  `contains(@class,'buy-btn')` would also match every product's `shelf-item__buy-btn`.
* "Add to cart" is scoped to the product card by title, so `iPhone 12` never clicks `iPhone 12 Mini`.
* Product names, credentials and shipping details live in `config.properties`.

## Reports

* `test-output/ExtentReport.html` - HTML report with PASS/FAIL screenshots embedded as Base64
* `test-output/screenshots/` - screenshots as PNG files
* `target/surefire-reports/` - standard Surefire/TestNG output

## CI

`.github/workflows/tests.yml` runs the suite headless on every push to `main` and uploads the reports as a build artifact.
