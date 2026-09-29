package tests.api;

import base.ApiBaseTest;
import io.restassured.response.Response;
import models.ContactPayload;
import models.LoginPayload;
import models.PartialContactPayload;
import models.UserPayload;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * RestAssured implementation of the 10 test cases in the Telecom Domain capstone document,
 * run in the exact flow specified there:
 * Add User -> Get user profile -> Update user -> Login user -> Add Contact ->
 * Get contact list -> Get contact -> Update full contact -> Update partial contact -> Logout User.
 *
 * A fresh, timestamped email is generated per run (see uniqueEmail()) because this API
 * rejects a duplicate email with 422, which would otherwise break TC_001 on a re-run.
 *
 * TestNG runs test classes as a single shared instance, so the token captured in TC_004
 * and the contactId captured in TC_005 are visible to every later test via the same
 * ContactApiClient instance (see ApiBaseTest). dependsOnMethods enforces the required
 * order and skips (not fails) downstream tests if an earlier step did not run.
 */
public class ContactApiTest extends ApiBaseTest {

    private final String runId = String.valueOf(System.currentTimeMillis());
    private final String email = "capstone." + runId + "@fake.com";
    private final String password = "myPassword1";
    private String updatedEmail;
    private String updatedPassword;

    @Test(description = "TC_001 - Add New User")
    public void TC_001_addNewUser() {
        UserPayload payload = new UserPayload("Test", "User", email, password);
        Response res = api.addUser(payload);

        Assert.assertEquals(res.statusCode(), 201, "Expected 201 Created. Body: " + res.asPrettyString());
        String token = res.jsonPath().getString("token");
        Assert.assertNotNull(token, "Response should include an auth token");
        api.setToken(token);
        extentTest.info("User created: " + email + " | token captured for later tests");
    }

    @Test(description = "TC_002 - Get user Profile", dependsOnMethods = "TC_001_addNewUser")
    public void TC_002_getUserProfile() {
        Response res = api.getUserProfile();

        Assert.assertEquals(res.statusCode(), 200, "Expected 200 OK. Body: " + res.asPrettyString());
        Assert.assertEquals(res.jsonPath().getString("email"), email, "Profile email should match the created user");
    }

    @Test(description = "TC_003 - Update User", dependsOnMethods = "TC_002_getUserProfile")
    public void TC_003_updateUser() {
        updatedEmail = "capstone.updated." + runId + "@fake.com";
        updatedPassword = "myNewPassword1";
        UserPayload payload = new UserPayload("Updated", "Username", updatedEmail, updatedPassword);

        Response res = api.updateUser(payload);

        Assert.assertEquals(res.statusCode(), 200, "Expected 200 OK. Body: " + res.asPrettyString());
        Assert.assertEquals(res.jsonPath().getString("email"), updatedEmail, "Email should reflect the update");
        Assert.assertEquals(res.jsonPath().getString("firstName"), "Updated");
    }

    @Test(description = "TC_004 - Log In User", dependsOnMethods = "TC_003_updateUser")
    public void TC_004_loginUser() {
        // must log in with the credentials from TC_003, since the password just changed
        api.setToken(null);
        LoginPayload payload = new LoginPayload(updatedEmail, updatedPassword);

        Response res = api.loginUser(payload);

        Assert.assertEquals(res.statusCode(), 200, "Expected 200 OK. Body: " + res.asPrettyString());
        String token = res.jsonPath().getString("token");
        Assert.assertNotNull(token, "Login should return a fresh auth token");
        api.setToken(token);
    }

    @Test(description = "TC_005 - Add Contact", dependsOnMethods = "TC_004_loginUser")
    public void TC_005_addContact() {
        ContactPayload payload = new ContactPayload("John", "Doe", "1970-01-01", "jdoe@fake.com",
                "8005555555", "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");

        Response res = api.addContact(payload);

        Assert.assertEquals(res.statusCode(), 201, "Expected 201 Created. Body: " + res.asPrettyString());
        String contactId = res.jsonPath().getString("_id");
        Assert.assertNotNull(contactId, "Response should include the new contact's _id");
        api.setContactId(contactId);
    }

    @Test(description = "TC_006 - Get Contact List", dependsOnMethods = "TC_005_addContact")
    public void TC_006_getContactList() {
        Response res = api.getContactList();

        Assert.assertEquals(res.statusCode(), 200, "Expected 200 OK. Body: " + res.asPrettyString());
        java.util.List<Object> contacts = res.jsonPath().getList("$");
        Assert.assertTrue(contacts.size() >= 1, "Contact list should contain at least the contact just added");
    }

    @Test(description = "TC_007 - Get Contact", dependsOnMethods = "TC_005_addContact")
    public void TC_007_getContact() {
        Response res = api.getContact(api.getContactId());

        Assert.assertEquals(res.statusCode(), 200, "Expected 200 OK. Body: " + res.asPrettyString());
        Assert.assertEquals(res.jsonPath().getString("email"), "jdoe@fake.com");
    }

    @Test(description = "TC_008 - Update Contact (full, PUT)", dependsOnMethods = "TC_005_addContact")
    public void TC_008_updateContactFull() {
        ContactPayload payload = new ContactPayload("Amy", "Miller", "1992-02-02", "amiller@fake.com",
                "8005554242", "13 School St.", "Apt. 5", "Washington", "QC", "A1A1A1", "Canada");

        Response res = api.updateContactFull(api.getContactId(), payload);

        Assert.assertEquals(res.statusCode(), 200, "Expected 200 OK. Body: " + res.asPrettyString());
        Assert.assertEquals(res.jsonPath().getString("email"), "amiller@fake.com", "Email should reflect the full update");
    }

    @Test(description = "TC_009 - Update Contact (partial, PATCH)", dependsOnMethods = "TC_008_updateContactFull")
    public void TC_009_updateContactPartial() {
        Response res = api.updateContactPartial(api.getContactId(), new PartialContactPayload("Anna"));

        Assert.assertEquals(res.statusCode(), 200, "Expected 200 OK. Body: " + res.asPrettyString());
        Assert.assertEquals(res.jsonPath().getString("firstName"), "Anna", "First name should reflect the partial update");
    }

    @Test(description = "TC_010 - Logout User", dependsOnMethods = "TC_009_updateContactPartial", alwaysRun = true)
    public void TC_010_logoutUser() {
        // best-effort cleanup so re-running the suite doesn't accumulate contacts/users
        if (api.getContactId() != null) {
            api.deleteContact(api.getContactId());
        }

        Response res = api.logoutUser();
        Assert.assertEquals(res.statusCode(), 200, "Expected 200 OK. Body: " + res.asPrettyString());

        api.deleteUser();
    }
}
