package sample.model;

import sample.utils.Trackable;

/***
 * This class defines a First lever division object.
 * @author Tchaikousky Thomas
 */
public class FirstLevelDivision extends Trackable {

    private int divisionID; //division's id number
    private String division; //division's name
    private int countryID; //division's country id

    /***
     * This constructor creates a first level division object.
     * @param divisionID The id of the FirstLevelDivision.
     * @param division The name of the FirstLevelDivision.
     * @param countryID The country id of the FirstLevelDivision.
     */
    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    /***
     * This method returns the division id field of the calling FirstLevelDivision object.
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /***
     * This method sets the division id of the calling FirstLevelDivision object to the integer passed as a parameter.
     * @param divisionID The id of the FirstLevelDivision.
     */
    public void setDivisionId(int divisionID) {
        this.divisionID = divisionID;
    }

    /***
     *This method returns the division field of the calling FirstLevelDivision object.
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /***
     * This method sets the division field of the calling FirstLevelDivisionObject to the string that is passed as a parameter.
     * @param division The name of the FirstLevelDivision.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /***
     * This method returns the country id field of the calling FirstLevelDivision object.
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }

    /***
     * This method sets the country id field of the calling FirstLevelDivision object to the integer passed as a parameter.
     * @param countryID The country id of the FirstLevelDivision.
     */
    public void setCountryId(int countryID) {
        this.countryID = countryID;
    }
}
