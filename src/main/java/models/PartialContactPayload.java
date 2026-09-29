package models;

/** Request body for PATCH /contacts/{id} - only the field(s) being changed. */
public class PartialContactPayload {
    public String firstName;

    public PartialContactPayload(String firstName) {
        this.firstName = firstName;
    }
}
