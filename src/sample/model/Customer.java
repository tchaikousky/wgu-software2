package sample.model;

import sample.utils.Trackable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.sql.Timestamp;

/***
 * This class defines a customer object.
 * @author Tchaikousky Thomas
 */
public class Customer extends Trackable {

    private SimpleIntegerProperty customerID; //customer's id number
    private SimpleStringProperty customerName; //customer's name
    private SimpleStringProperty address; //customer's address
    private SimpleStringProperty postalCode; //customer's postal code
    private SimpleStringProperty phone; //customer's phone number
    private SimpleStringProperty lastUpdatedBy; //last user to update the customer object
    private SimpleStringProperty createdBy; //user that created this customer object
    private SimpleIntegerProperty divisionID; //customer's division id

    /***
     * This constructor creates a customer object with just the customer's name.
     * @param customerName The customer's name.
     */
    public Customer(String customerName) {
        this.customerName = new SimpleStringProperty(customerName);
    }

    /***
     * This constructor creates a customer object with all fields except the create date field and the last updated field.
     * @param customerID The id of the customer.
     * @param customerName The name of the customer.
     * @param address The address of the customer.
     * @param postalCode The postal code of the customer.
     * @param phone The phone number of the customer.
     * @param createdBy The person who created the customer.
     * @param lastUpdatedBy The person who last updated the customer.
     * @param divisionID The division id of the customer.
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phone, String createdBy, String lastUpdatedBy, int divisionID) {
        this.customerID = new SimpleIntegerProperty(customerID);
        this.customerName = new SimpleStringProperty(customerName);
        this.address = new SimpleStringProperty(address);
        this. postalCode = new SimpleStringProperty(postalCode);
        this.phone = new SimpleStringProperty(phone);
        this.lastUpdatedBy = new SimpleStringProperty(lastUpdatedBy);
        this.createdBy = new SimpleStringProperty(createdBy);
        this.divisionID = new SimpleIntegerProperty(divisionID);
    }

    /***
     * This constructor creates a customer object with all available customer object fields.
     * @param customerID The id of the customer.
     * @param customerName The name of the customer.
     * @param address The address of the customer.
     * @param postalCode The postal code of the customer.
     * @param phone The phone number of the customer.
     * @param createDate The date of creation of the customer.
     * @param createdBy The person who created the customer.
     * @param lastUpdate The date of the last update of the customer.
     * @param lastUpdatedBy The person who last updated the customer.
     * @param divisionID The division id of the customer.
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phone,
                    Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int divisionID) {
        super(createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.customerName = new SimpleStringProperty(customerName);
        this.address = new SimpleStringProperty(address);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.phone = new SimpleStringProperty(phone);
        this.divisionID = new SimpleIntegerProperty(divisionID);
    }

    /***
     * This method returns the customer id field of the calling customer object.
     * @return customerID
     */
    public int getCustomerID() {
        return customerID.get();
    }

    /***
     * This method sets the customer id field of the calling customer object to the integer passed as a parameter.
     * @param customerID The id of the customer
     */
    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    /***
     * This method returns the customer id property of the calling customer object.
     * @return customerId
     */
    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }

    /***
     * This method returns customer name of the calling customer object.
     * @return customerName
     */
    public String getCustomerName() {
        return customerName.get();
    }

    /***
     * This method sets the customer name field of the calling customer object to the string passed as a parameter.
     * @param customerName The name of the customer.
     */
    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    /***
     * This method returns the customer name property of the calling customer object.
     * @return customerName
     */
    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    /***
     * This method returns the address field of the calling customer object.
     * @return address
     */
    public String getAddress() {
        return address.get();
    }

    /***
     * This method sets the address field of the calling customer object to the string passed as a parameter.
     * @param address The address of the customer.
     */
    public void setAddress(String address) {
        this.address.set(address);
    }

    /***
     * This method returns the address property of the calling customer object.
     * @return address
     */
    public SimpleStringProperty addressProperty() {
        return address;
    }

    /***
     * This method returns the postal code field of the calling customer object.
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode.get();
    }

    /***
     * This method sets the postal code field of the calling customer object to the string passed as a parameter.
     * @param postalCode The postal code of the customer.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    /***
     * This method returns the postal code property of the calling customer object.
     * @return postalCode
     */
    public SimpleStringProperty postalCodeProperty() {
        return postalCode;
    }

    /***
     * This method returns the phone number of the calling customer object.
     * @return phone
     */
    public String getPhone() {
        return phone.get();
    }

    /***
     * This method sets the phone number of the calling customer object to the string passed as a parameter.
     * @param phone The phone number of the customer.
     */
    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    /***
     * This method returns the phone property of the calling customer object.
     * @return phone
     */
    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    /***
     * This method returns the division id field of the calling customer object.
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID.get();
    }

    /***
     * This method sets the division id field of the calling customer object the integer passed as a parameter.
     * @param divisionID The division id of the customer.
     */
    public void setDivisionID(int divisionID) {
        this.divisionID.set(divisionID);
    }

    /***
     * This method returns the division id property of the calling customer object.
     * @return divisionID
     */
    public SimpleIntegerProperty divisionIDProperty() {
        return divisionID;
    }
}
