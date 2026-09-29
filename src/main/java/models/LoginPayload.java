package models;

/** Request body for POST /users/login. */
public class LoginPayload {
    public String email;
    public String password;

    public LoginPayload(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
