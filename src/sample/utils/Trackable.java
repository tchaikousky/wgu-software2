package sample.utils;

import java.sql.Timestamp;

/***
 * This class defines a trackable object.
 * @author Tchaikousky Thomas
 */
public class Trackable {

    private Timestamp createDate; //timestamp of the date the object was created
    private String createdBy; //user the object was created by
    private Timestamp lastUpdate; //timestamp of the last update of the object
    private String lastUpdatedBy; //user the object was last updated by

    /***
     * This empty constructor creates a Trackable object with none of its fields set.
     */
    public Trackable() {

    }

    /***
     * This constructor creates a Trackable object with all of its fields set.
     * @param createDate The date the object was created
     * @param createdBy The person who created the object
     * @param lastUpdate The date the object was last updated
     * @param lastUpdatedBy The person who last updated the object
     */
    public Trackable(Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /***
     * This method returns the create date field of the calling Trackable object.
     * @return createDate
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /***
     * This method sets the create date field of the calling Trackable object to timestamp passed as a parameter.
     * @param createDate The date the object was created
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /***
     * This method returns the created by field of the calling Trackable object.
     * @return
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /***
     * This method sets the created by field of the calling Trackable object to the string passed as a parameter.
     * @param createdBy The person who created the object
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /***
     * This method returns the last update field of the calling Trackable object.
     * @return lastUpdate
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /***
     * This method sets the lastUpdate field of the calling Trackable object to the Timestamp passed as a parameter.
     * @param lastUpdate The last date the object was updated
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /***
     * This method returns the lastUpdatedBy field of the calling Trackable object.
     * @return lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /***
     * This method sets the lastUpdatedBy field of the calling Trackable object to the string passed as a parameter.
     * @param lastUpdatedBy The person who last updated the object
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
