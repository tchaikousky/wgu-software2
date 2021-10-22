package sample.model;

/***
 * This class defines a contact object.
 * @author Tchaikousky Thomas
 */
public class Contact {

    private int contactId; //contact's id number
    private String contactName; //contact's name
    private String email; //contact's email

    /***
     * This constructor creates an contact object.
     * @param contactId The contact's id.
     * @param contactName The contact's name.
     * @param email The contact's email.
     */
    public Contact(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /***
     * This method returns the contact id field of the calling contact object.
     * @return contactId
     */
    public int getContactId() {
        return contactId;
    }

    /***
     * This method sets the contact id field of the calling contact object to the string passed as a parameter.
     * @param contactId The contact's id.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /***
     * This method returns the contact name field of the calling contact object.
     * @return contactName
     */
    public String getContactName() {
        return contactName;
    }

    /***
     * This method sets the contact name field of the calling contact object to the string passed as a parameter.
     * @param contactName The contact's name.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /***
     * This method returns the email field of the calling contact object.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /***
     * This method sets the email field of the calling contact object to the string passed as a parameter.
     * @param email The contact's email.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
