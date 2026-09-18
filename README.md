# bstackdemo Java Selenium POM Framework

Java + Selenium WebDriver + TestNG + Maven + ExtentReports automation framework for
https://bstackdemo.com/.

## GitHub

GitHub profile:
https://github.com/rakeshbande63-web

Recommended repository name:

`bstackdemo-java-selenium-framework`

Repository URL after creating it:

`https://github.com/rakeshbande63-web/bstackdemo-java-selenium-framework`

> The supplied GitHub profile currently has no public repositories, so the repository
> URL cannot be verified until the repository is created.

## Test cases

| ID | Scenario | Validation |
|---|---|---|
| TC_001 | Valid login | `demouser` + `testingisfun99` and Logout visible |
| TC_002 | Invalid login | Invalid credentials are rejected |
| TC_003 | Empty login | Empty credentials are rejected |
| TC_004 | Add single item | Cart contains at least one item |
| TC_005 | Add multiple items | Exactly 3 named products are added |
| TC_006 | Remove item | Cart count decreases by one |
| TC_007 | Place order | Confirmation message is displayed |
| TC_008 | Checkout without items | Empty cart has no Checkout action |

## TC_005 - three products and XPath

The multiple-item test adds exactly:

1. iPhone 12
2. iPhone 12 Mini
3. iPhone 12 Pro Max

Example XPath pattern:

```xpath
//div[contains(@class,'shelf-item')][.//*[normalize-space()='iPhone 12']]//button[contains(@class,'shelf-item__buy-btn')]
```

The project contains separate product-card-scoped XPath locators for all three products.

## Credentials

The public BrowserStack Demo examples document:

- Username: `demouser`
- Password: `testingisfun99`

The framework uses these values in `config.properties`.

## Run

```bash
mvn clean test
```

Headless:

```bash
mvn clean test -Dheadless=true
```

## Reports

Extent report:

```text
test-output/ExtentReport.html
```

Screenshots:

```text
test-output/screenshots/
```

Screenshots are also embedded as Base64 in the Extent report.

## Git commands

After creating the GitHub repository:

```bash
git init
git add .
git commit -m "Initial bstackdemo Selenium POM framework"
git branch -M main
git remote add origin https://github.com/rakeshbande63-web/bstackdemo-java-selenium-framework.git
git push -u origin main
```

Do not place a GitHub password, token, or access key in this project.
