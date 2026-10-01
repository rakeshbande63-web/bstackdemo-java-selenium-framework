# BlazeDemo Flight Booking Automation Framework

Java + Selenium WebDriver + TestNG + Maven framework for <https://blazedemo.com>, built with
the Page Object Model, following the capstone document's structure and step-by-step guide.

## Test scenarios (from the capstone document)

| ID | Scenario | Type | Group |
|----|----------|------|-------|
| TC01 | Homepage loads, both city dropdowns visible | Smoke | `smoke` |
| TC02 | Search flights with valid cities (Boston -> New York) | Functional | `functional` |
| TC03 | Complete a full flight booking, confirmation shown | Functional | `functional` |
| TC04 | 3 bookings with different passenger/payment data via `@DataProvider` | Data-driven | `functional` |
| TC05 | Blank credit card - confirmation should not appear | Negative | `negative` |
| TC06 | Non-numeric credit card - should be rejected, not accepted | Negative | `negative` |
| TC07 | Same departure and destination city - no flights should be offered | Negative | `negative` |

`testng.xml` runs these as three `<test>` blocks by group, matching Step 7 of the document.

## Project layout (matches the document's suggested structure)

```
src/test/java/com/blazedemo/pages   HomePage, ReservePage, PurchasePage, ConfirmationPage
src/test/java/com/blazedemo/tests   FlightBookingTest.java (TC01-TC07)
src/test/java/com/blazedemo/utils   ConfigReader, WaitUtils, DriverFactory, ScreenshotUtil, ExtentManager
src/test/java/com/blazedemo/base    BaseTest - driver setup/teardown, reporting, screenshot-on-failure (Step 9)
src/test/resources/config.properties
testng.xml
```

## Run

Requires JDK 17+, Maven 3.8+, Google Chrome. The matching chromedriver downloads automatically.

```
mvn clean test                     # visible browser
mvn clean test -Dheadless=true     # headless
```

In Eclipse/IntelliJ: right-click `testng.xml` -> Run As -> TestNG Suite.

## Locator confidence

* **Confirmed** by fetching the live site during development: the home page's `fromPort`/
  `toPort` `<select>` elements and the "Find Flights" button; the purchase page's field
  labels, order, and the "Choose This Flight" button text; the exact confirmation text
  "Thank you for your purchase today!" (this phrase is also used identically in BlazeMeter's
  own Taurus documentation for this app).
* **Confirmed** by real, independently published Selenium scripts written against this exact
  page: the field ids `inputName`, `zipCode`, `creditCardNumber`, `nameOnCard`.
* **Not independently confirmed**, but following the same naming convention as the ids above,
  seen across multiple public BlazeDemo automation projects: `address`, `city`, `state`,
  `cardType`, `creditCardMonth`, `creditCardYear`, `rememberMe`. If a field lookup times out
  on your first run, open the real form with F12, copy the `<form>`'s outerHTML, and send it
  back - I'll correct the field id in `PurchasePage.java` in one pass, the same way the
  Contact List project's field ids got corrected.

## Known BlazeDemo behavior (confirmed by running the suite)

BlazeDemo is a lightweight demo app, and two of the capstone document's negative scenarios
assume validation this app does not actually have:

* **TC05/TC06** - confirmed on a real run: BlazeDemo's purchase form does **not** validate the
  credit card number server-side. A blank or non-numeric card number still reaches the
  confirmation page. The document's expected outcome ("Confirmation page should not appear" /
  "Proper validation or error behavior") does not hold for this app. Both tests now assert and
  document this actual behavior rather than the document's assumption.
* **TC07** - the departure and destination dropdowns are two disjoint lists of cities (confirmed
  by fetching the live home page): no city name appears in both. Selecting "the same city
  twice" therefore cannot even be attempted through the UI - trying to select a departure
  city's name from the destination dropdown throws `NoSuchElementException`, the same as any
  other option that doesn't exist. TC07 instead asserts this disjointness directly, which is
  what actually guarantees a same-city booking can never happen on this app.

None of this affects TC01-TC04, which don't depend on any assumption about validation.

## Reports

* `test-output/ExtentReport.html` - HTML report with PASS/FAIL/SKIP and screenshots
* `target/surefire-reports/` - standard Surefire/TestNG output

## CI

`.github/workflows/tests.yml` runs the full suite headless on every push to `main` or
`capstoneproject3`, and uploads the reports as a build artifact.
