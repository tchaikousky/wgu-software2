package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import sample.utils.DbAccess;
import sample.utils.Trackable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
  This class defines an Appointment object.
  @author Tchaikousky Thomas
 */

public class Appointment extends Trackable {
    private SimpleIntegerProperty appointmentID; //appointment id number
    private SimpleStringProperty title; // appointment title
    private SimpleStringProperty description; //appointment description
    private SimpleStringProperty location; //appointment location
    private SimpleStringProperty type; //appointment type
    private LocalDateTime start; //appointment start date and time
    private LocalDateTime end; //appointment end date and time
    private String lastUpdatedBy; //last person to update the appointment
    private String createdBy; //name of person that created the appointment
    private SimpleIntegerProperty customerID; //appointment's customer id number
    private SimpleIntegerProperty userID; //appointment's user id number
    private SimpleIntegerProperty contactID; //appointment's contact id number
    private SimpleStringProperty startAsString; //start date and time as a string
    private SimpleStringProperty endAsString; //end date and time as a string
    private SimpleStringProperty contactNameProperty; //contact name as a property
    private SimpleStringProperty contactName; //appointment's contact name
    private SimpleStringProperty customerName; //appointment's customer name
    private SimpleStringProperty userName; //appointment's user's name


    /***
     * This constructor creates an appointment object with the start and end DateTimes as strings.
     * @param appointmentID The appointment's id.
     * @param title The appointment's title.
     * @param description The appointment's description.
     * @param location The appointment's location.
     * @param type The appointment's type.
     * @param start The appointment's start date and time.
     * @param end The appointment's end date and time.
     * @param lastUpdatedBy The person that last updated the appointment.
     * @param createdBy The person that created the appointment.
     * @param customerID The appointment's customer id.
     * @param userID The appointment's user id.
     * @param contactID The appointment's contact id.
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, String lastUpdatedBy, String createdBy, int customerID, int userID, int contactID) {
        this.appointmentID = new SimpleIntegerProperty(appointmentID);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.contactID = new SimpleIntegerProperty(contactID);
        this.type = new SimpleStringProperty(type);
        this.startAsString = new SimpleStringProperty(start.toString().replace("T", " "));
        this.endAsString = new SimpleStringProperty(end.toString().replace("T", " "));
        this.lastUpdatedBy = lastUpdatedBy;
        this.createdBy = createdBy;
        this.userID = new SimpleIntegerProperty(userID);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.contactNameProperty = getContactName(contactID);

    }

    /***
     * This constructor creates an appointment object with the contact id being replaced with the contact's name.
     * @param appointmentID The appointment's id.
     * @param title The appointment's title.
     * @param description The appointment's description.
     * @param location The appointment's location.
     * @param type The appointment's type.
     * @param start The appointment's start date and time.
     * @param end The appointment's end date and time.
     * @param lastUpdatedBy The person that last updated the appointment.
     * @param createdBy The person that created the appointment.
     * @param customerID The appointment's customer id.
     * @param userID The appointment's user id.
     * @param contactID The appointment's contact id.
     * @param contactName The appointment's contact name.
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, String lastUpdatedBy, String createdBy, int customerID, int userID, int contactID,
                       String contactName) {
        this.appointmentID = new SimpleIntegerProperty(appointmentID);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.contactID = new SimpleIntegerProperty(contactID);
        this.type = new SimpleStringProperty(type);
        this.startAsString = new SimpleStringProperty(start.toString().replace("T", " "));
        this.endAsString = new SimpleStringProperty(end.toString().replace("T", " "));
        this.lastUpdatedBy = lastUpdatedBy;
        this.createdBy = createdBy;
        this.userID = new SimpleIntegerProperty(userID);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.contactName = new SimpleStringProperty(contactName);
    }

    /***
     * This constructor creates an appointment object with the start and end DateTimes as LocalDateTime objects.
     * @param appointmentID The appointment's id.
     * @param title The appointment's title.
     * @param description The appointment's description.
     * @param location The appointment's location.
     * @param type The appointment's type.
     * @param start The appointment's start date and time.
     * @param end The appointment's end date and time.
     * @param createDate The create date of the appointment.
     * @param createdBy The person that created the appointment.
     * @param lastUpdate The date of the last update.
     * @param lastUpdatedBy The person that last updated the appointment.
     * @param customerID The appointment's customer id.
     * @param userID The appointment's user id.
     * @param contactID The appointment's contact id.
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) {
        super(createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.appointmentID = new SimpleIntegerProperty(appointmentID);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.type = new SimpleStringProperty(type);
        this.start = start;
        this.end = end;
        this.customerID = new SimpleIntegerProperty(customerID);
        this.userID = new SimpleIntegerProperty(userID);
        this.contactID = new SimpleIntegerProperty(contactID);
    }

    /***
     * This method returns the appointment id field of the calling appointment object.
     * @return appointmentID
     */
    public int getAppointmentID() {
        return appointmentID.get();
    }

    /***
     * This method sets the appointmentID field of the calling appointment object to the integer that is passed as a parameter.
     * @param appointmentID The appointment id.
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID.set(appointmentID);
    }

    /***
     * This method returns the appointment id property of the calling appointment object.
     * @return appointmentID
     */
    public SimpleIntegerProperty appointmentIDProperty() {
        return appointmentID;
    }

    /***
     * This method returns the title field of the calling appointment object.
     * @return title
     */
    public String getTitle() {
        return title.get();
    }

    /***
     * This method sets the title field of the calling appointment object to the string that is passed as a parameter.
     * @param title The appointment title.
     */
    public void setTitle(String title) {
        this.title.set(title);
    }

    /***
     * This method returns the title property of the calling appointment object.
     * @return title
     */
    public SimpleStringProperty titleProperty() {
        return title;
    }

    /***
     * This method returns the description field of the calling appointment object.
     * @return description
     */
    public String getDescription() {
        return description.get();
    }

    /***
     * This method sets the description field of the calling appointment object to the string that is passed as a parameter.
     * @param description The appointments description.
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /***
     * This method return the description property of the calling appointment object.
     * @return description
     */
    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    /***
     * This method returns the location field of the calling appointment object.
     * @return location
     */
    public String getLocation() {
        return location.get();
    }

    /***
     * This method sets the location field of the calling appointment object to the string that is passed as a parameter.
     * @param location The appointment location.
     */
    public void setLocation(String location) {
        this.location.set(location);
    }

    /***
     * This method returns the location property of thte calling appointment object.
     * @return location
     */
    public SimpleStringProperty locationProperty() {
        return location;
    }

    /***
     * This method returns the type field of the calling appointment object.
     * @return type
     */
    public String getType() {
        return type.get();
    }

    /***
     * This method sets the type field of the calling appointment object to the given parameter.
     * @param type The appointment type.
     */
    public void setType(String type) {
        this.type.set(type);
    }

    /***
     * This method returns the type property of the calling appointment object.
     * @return type
     */
    public SimpleStringProperty typeProperty() {
        return type;
    }

    /***
     * This method returns the start field of the calling appointment object.
     * @return start
     */
    public LocalDateTime getStart() {
        return start;
    }

    /***
     * This method sets the start field of the calling appointment object to the given parameter.
     * @param start The appointment start.
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /***
     * This method returns the end field of the calling appointment object.
     * @return end
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /***
     * This method sets the end field of the calling appointment object to the string that is passed as a parameter.
     * @param end The appointment end.
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /***
     * This method returns the customer id field of the calling appointment object.
     * @return customerID
     */
    public int getCustomerID() {
        return customerID.get();
    }

    /***
     * This method sets the customer id field of the calling appointment object to the integer passed as a parameter.
     * @param customerID The appointment customer id.
     */
    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    /***
     * This method returns the customer id property of the calling appointment object.
     * @return customerID
     */
    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }

    /***
     * This method returns the user id field of the calling appointment object.
     * @return userID
     */
    public int getUserID() {
        return userID.get();
    }

    /***
     * This method sets the user id field of the calling appointment object to the integer passed as a parameter.
     * @param userID The appointment user id.
     */
    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    /***
     * This method returns the user id property of the calling appointment object.
     * @return userID
     */
    public SimpleIntegerProperty userIDProperty() {
        return userID;
    }

    /***
     * This method returns the contact id field of the calling appointment object.
     * @return contactID
     */
    public int getContactID() {
        return contactID.get();
    }

    /***
     * This method sets the contact id field of the calling appointment object to the integer passed as a parameter.
     * @param contactID The appointment contact id.
     */
    public void setContactID(int contactID) {
        this.contactID.set(contactID);
    }

    /***
     * This method returns the contact id property of the calling appointment object.
     * @return contactID
     */
    public SimpleIntegerProperty contactIDProperty() {
        return contactID;
    }

    /***
     * This method returns the start property of the calling appointment object.
     * @return startAsString
     */
    public SimpleStringProperty startProperty() {
        return startAsString;
    }

    /***
     * This method returns the end property of the calling appointment object.
     * @return endAsString
     */
    public SimpleStringProperty endProperty() {
        return endAsString;
    }

    /***
     * This method returns the contact name for a given appointment. The contact id field of the calling appointment object that is passed as a parameter is then passed to a DbAccess object/method which returns the contact name.
     * The contact name is then returned to the calling appointment object.
     * @param contactID The appointment contact id.
     * @return contactName
     */
    public SimpleStringProperty getContactName(int contactID) {
        try {
            DbAccess dbAccess = new DbAccess();
            contactName = new SimpleStringProperty(dbAccess.getContactName(contactID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactName;
    }

    /***
     * This method returns the contact name property of the calling appointment object.
     * @return contactName
     */
    public SimpleStringProperty contactNameProperty() {
        return contactName;
    }

    /***
     * This method returns the customer name property of the calling appointment object.
     * @return customerName
     */
    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    /***
     * This method returns the username property of the calling appointment object.
     * @return userName
     */
    public SimpleStringProperty userNameProperty() {
        return userName;
    }
}
