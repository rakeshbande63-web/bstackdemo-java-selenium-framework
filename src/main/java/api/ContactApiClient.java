package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;

/**
 * Thin wrapper around RestAssured for the Contact List API.
 * One instance per test class keeps the auth token across the request chain
 * (Add User -> Login -> Add Contact -> ...), the same way the Postman
 * collection passes {{token}} and {{contactId}} between requests.
 */
public class ContactApiClient {

    private String token;
    private String contactId;

    public ContactApiClient() {
        RestAssured.baseURI = ConfigReader.get("baseUrl");
    }

    private RequestSpecification given() {
        RequestSpecification spec = RestAssured.given().contentType("application/json");
        if (token != null) {
            spec = spec.header("Authorization", "Bearer " + token);
        }
        return spec;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return contactId;
    }

    // ---- Users ----

    /** TC_001 - POST /users */
    public Response addUser(Object payload) {
        return given().body(payload).post("/users");
    }

    /** TC_002 - GET /users/me */
    public Response getUserProfile() {
        return given().get("/users/me");
    }

    /** TC_003 - PATCH /users/me */
    public Response updateUser(Object payload) {
        return given().body(payload).patch("/users/me");
    }

    /** TC_004 - POST /users/login */
    public Response loginUser(Object payload) {
        return given().body(payload).post("/users/login");
    }

    /** TC_010 - POST /users/logout */
    public Response logoutUser() {
        return given().post("/users/logout");
    }

    // ---- Contacts ----

    /** TC_005 - POST /contacts */
    public Response addContact(Object payload) {
        return given().body(payload).post("/contacts");
    }

    /** TC_006 - GET /contacts */
    public Response getContactList() {
        return given().get("/contacts");
    }

    /** TC_007 - GET /contacts/{id} */
    public Response getContact(String id) {
        return given().get("/contacts/" + id);
    }

    /** TC_008 - PUT /contacts/{id} */
    public Response updateContactFull(String id, Object payload) {
        return given().body(payload).put("/contacts/" + id);
    }

    /** TC_009 - PATCH /contacts/{id} */
    public Response updateContactPartial(String id, Object payload) {
        return given().body(payload).patch("/contacts/" + id);
    }

    /** Not in the flow, but needed so TC_005-TC_009 don't leave test data behind. */
    public Response deleteContact(String id) {
        return given().delete("/contacts/" + id);
    }

    /** Cleans up the user created by TC_001 so the suite can be re-run without a fresh email. */
    public Response deleteUser() {
        return given().delete("/users/me");
    }
}
