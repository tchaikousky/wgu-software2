package sample.model;

import sample.utils.Trackable;

import java.sql.Timestamp;

/***
 * This class defines a country object.
 * @author Tchaikousky Thomas
 */
public class Country extends Trackable {

    private int countryId; //country's id number
    private String country; //country's name

    /***
     * This constructor creates a country object.
     * @param countryId The country's id.
     * @param country The country's name.
     * @param createDate The country's create date.
     * @param createdBy The person who created the country.
     * @param lastUpdate The date of the last update of the country.
     * @param lastUpdatedBy The person who last updated the country.
     */
    public Country(int countryId, String country, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        super(createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.countryId = countryId;
        this.country = country;
    }

    /***
     * This method returns the country id field of the calling country object.
     * @return countryId
     */
    public int getCountryId() {
        return countryId;
    }

    /***
     * This method sets the country id of the calling country object to the integer passed as a parameter.
     * @param countryId The country's id.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /***
     * This method returns the country name of the calling country objecte.
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /***
     * This method sets the country field of the calling country object to the string passed as a parameter.
     * @param country The country's name.
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
