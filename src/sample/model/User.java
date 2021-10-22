package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import sample.utils.Trackable;
import java.sql.Timestamp;

/***
 * This class defines an User object.
 * @author Tchaikousky Thomas
 */
public class User extends Trackable {

    private SimpleIntegerProperty userId; //user's id number
    private SimpleStringProperty userName; //username field
    private SimpleStringProperty password; //password field

    /***
     * This constructor creates an User object with null fields.
     */
    public User() {

    }

    /***
     * This constructor creates an User object and sets that object's userName field.
     * @param userName The user's username.
     */
    public User(String userName) {
        this.userName = new SimpleStringProperty(userName);
    }

    /***
     * This constructor creates an User object and sets the userName and password fields.
     * @param userId The user's id.
     * @param userName The user's username.
     */
    public User(int userId, String userName) {
        this.userId = new SimpleIntegerProperty(userId);
        this.userName = new SimpleStringProperty(userName);
    }

    /***
     * This constructor creates an User object and sets all of the User object's fields.
     * @param userId The user's id.
     * @param userName The user's username.
     * @param password The user's password.
     * @param createDate The date of creation of the user.
     * @param createdBy The person who created the user.
     * @param lastUpdate The date of the last update of the user.
     * @param lastUpdateBy The person who last updated the user.
     */
    public User(int userId, String userName, String password, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.userId = new SimpleIntegerProperty(userId);
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
    }

    /***
     * This method returns the user id field of the calling User object.
     * @return userId
     */
    public int getUserId() {
        return userId.get();
    }

    /***
     * This method sets the user id field of the calling User object to the integer passed as a parameter.
     * @param userId The id of the user.
     */
    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    /***
     * This method returns the user id property of the calling User object.
     * @return
     */
    public SimpleIntegerProperty userId() {
        return userId;
    }

    /***
     * This method returns the user name field of the calling User object.
     * @return userName
     */
    public String getUserName() {
        return userName.get();
    }

    /***
     * This method sets the user name field of the calling User object to the string passed as a parameter.
     * @param userName The user's userName.
     */
    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    /***
     * Ths method returns the user name property of the calling User object.
     * @return
     */
    public SimpleStringProperty userNameProperty() {
        return userName;
    }

    /***
     * This method returns the password field of the calling User object.
     * @return password
     */
    public String getPassword() {
        return password.get();
    }

    /***
     * This method sets the password field of the calling User object to the string passed as a parameter.
     * @param password The user's password.
     */
    public void setText(String password) {
        this.password.set(password);
    }

    /***
     * This method returns the password property of the calling User object.
     * @return password
     */
    public SimpleStringProperty passwordProperty() {
        return password;
    }
}
