# Contact List App - Capstone Test Framework

API tests (RestAssured + TestNG) and UI smoke tests (Selenium + TestNG) for
<https://thinking-tester-contact-list.herokuapp.com/>, plus a Postman collection using
chai assertions, built from the Telecom Domain capstone document.

## What's here

| Deliverable | Location |
|---|---|
| Postman collection (chai `pm.expect`/`pm.test`) | `postman/ContactList-Capstone.postman_collection.json` |
| RestAssured API tests (Java) | `src/test/java/tests/api/ContactApiTest.java` |
| Selenium UI smoke tests (Java) | `src/test/java/tests/ui/ContactUiTest.java` |
| HTML report (ExtentReports) | generated at `test-output/ExtentReport.html` when you run the suite |

## Test flow (matches the capstone document exactly)

Add User -> Get user profile -> Update user -> Login user -> Add Contact -> Get contact list ->
Get contact -> Update full contact -> Update partial contact -> Logout User

| ID | Method | Endpoint | Validates |
|----|--------|----------|-----------|
| TC_001 | POST | `/users` | 201, response has a token, captures it |
| TC_002 | GET | `/users/me` | 200, profile email matches |
| TC_003 | PATCH | `/users/me` | 200, email/first name reflect the update |
| TC_004 | POST | `/users/login` | 200, returns a fresh token (uses the updated credentials from TC_003) |
| TC_005 | POST | `/contacts` | 201, response has `_id`, captures it |
| TC_006 | GET | `/contacts` | 200, list has at least one contact |
| TC_007 | GET | `/contacts/{id}` | 200, contact email matches |
| TC_008 | PUT | `/contacts/{id}` | 200, full update reflected |
| TC_009 | PATCH | `/contacts/{id}` | 200, partial update reflected |
| TC_010 | POST | `/users/logout` | 200; also deletes the test contact and user as cleanup |

In `ContactApiTest.java` these run in this exact order via TestNG's `dependsOnMethods`, sharing one
`ContactApiClient` instance so the token from TC_001/TC_004 and the contact `_id` from TC_005 carry
forward automatically - the same variable-chaining the Postman collection does with
`pm.collectionVariables`. If an earlier step fails, the tests that depend on it are marked SKIP, not
FAIL, so one broken step doesn't hide the results of the unrelated ones.

A fresh, timestamped email is generated each run (`capstone.<timestamp>@fake.com`), because this API
returns `422` for a duplicate email and would otherwise break TC_001 on a second run. TC_010 deletes
the contact and user it created so repeated runs don't accumulate test data.

## UI tests (Selenium)

`ContactUiTest.java` covers the sign-up / login / add-contact / logout flow through the real browser,
each as an independent test with its own session:

| ID | Scenario |
|----|----------|
| TC_UI_001 | Invalid login is rejected, stays on `/login` |
| TC_UI_002 | Sign-up creates a user and lands on the contact list |
| TC_UI_003 | Adding a contact through the form increases the list by one and shows the new name |
| TC_UI_004 | Logout returns to `/login` |

## Locator confidence

* **Confirmed** against public code written for this exact app: `email`, `password`, `submit`,
  `signup` on the login page; `firstName`, `lastName`, `email`, `password`, `submit` on the sign-up
  form; `logout` on the contact list page.
* **Not independently verified** against the live DOM: `add-contact`, the contact-table row
  structure, and every field id in `ContactFormPage.java` (`birthdate`, `phone`, `street1`,
  `street2`, `city`, `stateProvince`, `postalCode`, `country`). These follow the app's own REST API
  field names, which is a reasonable guess, not a confirmed one. If `TC_UI_003` fails, open the real
  form with F12, copy the field's `outerHTML`, and update `ContactFormPage.java` - see
  `pages/ContactFormPage.java`'s class comment.

The RestAssured tests (TC_001-TC_010) don't depend on any of this, since they call the API directly.

## Run

Requires JDK 17+, Maven 3.8+. Chrome is only needed for the UI suite; the matching chromedriver is
downloaded automatically.

```
mvn clean test                              # both suites (testng.xml)
mvn clean test -DsuiteFile=testng-api.xml   # API tests only, no browser needed
mvn clean test -DsuiteFile=testng-ui.xml -Dheadless=true   # UI tests only, headless
```

In Eclipse/IntelliJ: right-click `testng.xml`, `testng-api.xml` or `testng-ui.xml` -> Run As -> TestNG Suite.

### Postman / Newman

Import `postman/ContactList-Capstone.postman_collection.json` into Postman and run it with the
Collection Runner (requests execute top-to-bottom; variables carry automatically), or from the
command line:

```
npm install -g newman
newman run postman/ContactList-Capstone.postman_collection.json
```

## Reports

* `test-output/ExtentReport.html` - one shared HTML report for both suites, with PASS/FAIL/SKIP and
  screenshots (UI tests only) embedded as Base64
* `target/surefire-reports/` - standard Surefire/TestNG output

## CI

`.github/workflows/tests.yml` runs the API suite and the headless UI suite as separate jobs on every
push to `main`, and uploads both reports as build artifacts.
