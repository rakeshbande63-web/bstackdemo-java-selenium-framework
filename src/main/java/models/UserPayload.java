package models;

/** Request body for POST /users and PATCH /users/me. */
public class UserPayload {
    public String firstName;
    public String lastName;
    public String email;
    public String password;

    public UserPayload() {
    }

    public UserPayload(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
