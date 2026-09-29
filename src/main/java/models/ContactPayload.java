package models;

/** Request body for POST /contacts, PUT /contacts/{id}. */
public class ContactPayload {
    public String firstName;
    public String lastName;
    public String birthdate;
    public String email;
    public String phone;
    public String street1;
    public String street2;
    public String city;
    public String stateProvince;
    public String postalCode;
    public String country;

    public ContactPayload() {
    }

    public ContactPayload(String firstName, String lastName, String birthdate, String email, String phone,
                           String street1, String street2, String city, String stateProvince,
                           String postalCode, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.stateProvince = stateProvince;
        this.postalCode = postalCode;
        this.country = country;
    }
}
