package sample.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/***
 * This class creates a connection to the database and execute database queries.
 * @author Tchaikousky Thomas
 */
public class DbAccess {

    private final String url = "jdbc:mysql://wgudb.ucertify.com"; //database url
    private final String databaseName = "WJ03ST8"; //database name
    private final String userName = "U03ST8"; //user name
    private final String password = "53688069128"; //password
    private final Conn conn = new Conn(url, databaseName, userName, password); //Conn object

    /***
     * This constructor creates a DbAccess object.
     * @throws SQLException
     */
    public DbAccess() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    /***
     * This method adds a customer to the database.
     * @param name Customer's name
     * @param address Customer's address
     * @param postalCode Customer's postal code
     * @param phone Customer phone number
     * @param createdBy Person who created the customer
     * @param lastUpdatedBy Person to last update the customer
     * @param divisionID Customer's division id
     * @throws SQLException
     */
    public void addCustomer(String name, String address, String postalCode, String phone, String createdBy, String lastUpdatedBy, int divisionID) throws SQLException {
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            statement.execute("INSERT INTO customers (`Customer_Name`, `Address`, `Postal_Code`, `Phone`, `Created_By`, `Last_Updated_By`, `Division_ID`) VALUES ('" + name + "', '" + address + "', '" + postalCode + "', '" + phone + "', '" + createdBy + "', '" + lastUpdatedBy + "', '" + divisionID + "')");
            dbConnection.close();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /***
     * This method updates a customer in the database with the parameters passed.
     * @param customerID Customer's id
     * @param name Customer's name
     * @param address Customer's address
     * @param postalCode Customer's postal code
     * @param phone Customer phone number
     * @param lastUpdatedBy Person to last update the customer
     * @param divisionID Customer's division id
     */
    public void updateCustomer(String customerID, String name, String address, String postalCode, String phone, String lastUpdatedBy, int divisionID) {
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            statement.execute("UPDATE customers SET customer_name = '" + name + "', address = '" + address + "', postal_code = '" + postalCode + "', phone = '" + phone + "', last_updated_by = '" + lastUpdatedBy + "', division_id = '" + divisionID + "' WHERE customer_id = " + customerID);
            dbConnection.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * This customer receives a customr as a parameter and returns its country.
     * @param customer Customer whose country is needed
     * @return
     * @throws SQLException
     */
    public String getCountry(Customer customer) throws SQLException {
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM first_level_divisions WHERE division_id =" + customer.getDivisionID());
            while(rs.next()) {
                int countryID = rs.getInt("country_id");

                if (countryID == 3) {
                    return "Canada";
                } else if (countryID == 2) {
                    return "UK";
                } else if (countryID == 1) {
                    return "U.S";
                }
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * This method returns a division list. The country id is passed to the method and the method returns all FirstLevelDivisions belonging to that country.
     * @param countryID The country id
     * @return
     */
    public ObservableList<FirstLevelDivision> getDivisionList(int countryID) {
        ObservableList<FirstLevelDivision> list = FXCollections.observableArrayList();

        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM first_level_divisions WHERE country_id = " + countryID);
            while (rs.next()) {
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(
                        rs.getInt("division_id"),
                        rs.getString("division"),
                        rs.getInt("country_id")
                        );
                list.add(firstLevelDivision);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /***
     * This method takes a customer object and returns its division name.
     * @param customer The Customer object that the division name is needed for.
     * @return division
     * @throws SQLException
     */
    public String getDivision(Customer customer) throws SQLException{
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            customer.getDivisionID();
            ResultSet rs = statement.executeQuery("SELECT * FROM first_level_divisions WHERE division_id =" + customer.getDivisionID());
            while(rs.next()) {
                String division = rs.getString("division");
                return division;
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * This method takes a customer id and deletes that customer from the database.
     * @param customerID The customer id of the customer to delete.
     */
    public void deleteSingleCustomer(int customerID) {
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            statement.execute("DELETE FROM customers WHERE customer_id = " + customerID);
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method checks to see if any appointments belong to the customer with the customer id passed as a parameter.
     * @param customerID The customer's id.
     * @return true
     */
    public Boolean hasAppointments(int customerID) {
        boolean flag = false;
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs  = statement.executeQuery("SELECT COUNT(1) FROM appointments WHERE customer_id = " + customerID);
            while(rs.next()) {
                int count = rs.getInt("count(1)");
                if((count) > 0) {
                    return true;
                }
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /***
     * This method returns an arraylist of type string[] containing customers and amount of appointments the customers have.
     * @return customerTotals
     */
    public ArrayList<String[]> getCustomerAppointmentTotals() {
        ArrayList<String[]> customerTotals = new ArrayList<>();
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT customers.Customer_Name, count(customers.Customer_ID) as amount " +
                    "from appointments left join customers on (customers.Customer_ID = appointments.Customer_ID) group by " +
                    "customers.Customer_ID");
            while(rs.next()) {
                String[] array = {rs.getString("customer_name"), String.valueOf(rs.getInt("amount"))};
                customerTotals.add(array);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerTotals;
    }

    /***
     * This method return a list of type appointment. This is all appointments in the database.
     * @return list
     */
    public ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT appointments.appointment_id, appointments.title, appointments.description, appointments.location, appointments.type, appointments.start, appointments.end, appointments.created_by," +
                    "appointments.last_updated_by, appointments.customer_id, appointments.user_id, appointments.contact_id, contacts.contact_name FROM appointments" +
                    " INNER JOIN contacts ON appointments.contact_id = contacts.contact_id");
            while(rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    ZonedDateTime.parse(rs.getTimestamp("start").toString().replace(" ", "T").concat("Z")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                    ZonedDateTime.parse(rs.getTimestamp("end").toString().replace(" ", "T").concat("Z")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getString("last_updated_by"),
                    rs.getInt("customer_id"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id"),
                    rs.getString("contact_name")
                );

                list.add(appointment);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /***
     * This method returns the contact name of the contact id passed as a parameter.
     * @param contactID The id of the contact.
     * @return
     */
    public String getContactName(int contactID) {
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM contacts WHERE contact_id = " + contactID);
            while(rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("contact_id"),
                        rs.getString("contact_name"),
                        rs.getString("email")

                );
                 return contact.getContactName();
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unavailable";
    }

    /***
     * This method returns a list of all customers.
     * @return customers
     */
    public ObservableList<Customer> getCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM customers");
            while(rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("postal_code"),
                        rs.getString("phone"),
                        rs.getString("created_by"),
                        rs.getString("last_updated_by"),
                        rs.getInt("division_id")
                );
                customers.add(customer);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /***
     * This method returns a list of all users.
     * @return users
     */
    public ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name")
                );
                users.add(user);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /***
     * This method adds a new appointment to the database.
     * @param title Appontment's title
     * @param description Appointment's description
     * @param location Appointment's location
     * @param type Appointment's type
     * @param start Appointment's start
     * @param end Appointments' end
     * @param createdBy Person who created the appointment
     * @param customerID Appointment's customer id
     * @param userID Appointment's user id
     * @param contactID Appointment's contact id
     */
    public void addAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, String createdBy, int customerID, int userID, int contactID)  {
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            statement.execute("INSERT INTO appointments (`title`, `description`, `location`, `type`, `start`, `end`, `created_by`," +
                    " `last_updated_by`, `customer_id`, `user_id`, `contact_id`) VALUES ('" + title + "', '" + description + "', '" + location
                    + "', '" + type + "', '" + start + "', '" + end + "', '" + createdBy + "', '" + createdBy + "', '" + customerID + "', '" + userID + "', '" + contactID + "')");
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method updates the appointment with this appointment id with the parameters passed.
     * @param appointmentID Appointment's id
     * @param title Appointment's title
     * @param description Appointment's description
     * @param location Appointment's location
     * @param type Appointment's type
     * @param start Appointment's start
     * @param end Appointment's end
     * @param createdBy Person who created the appointment
     * @param customerID Appointment's customer id
     * @param userID Appointment's user id
     * @param contactID Appointment's contact id
     */
    public void updateAppointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, String createdBy, int customerID, int userID, int contactID) {
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            statement.execute("UPDATE appointments SET title = '" + title + "', description = '" + description + "', location = '" + location + "', type = '"
                    + type + "', start = '" + start + "', end = '" + end + "', last_updated_by = '" + createdBy + "', customer_id = '" + customerID + "', user_id = '"
                    + userID + "', contact_id = '" + contactID + "' WHERE appointment_id =" + appointmentID);

            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method uses the customer name passed as a parameter to get a list of type customer.
     * @param customerName The name of the customer
     * @return customers
     * @throws SQLException
     */
    public ObservableList<Customer> getCustomerID(String customerName) throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM customers WHERE customer_name = '" + customerName + "'");
            while(rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("postal_code"),
                        rs.getString("phone"),
                        rs.getString("created_by"),
                        rs.getString("last_updated_by"),
                        rs.getInt("division_id")
                );
                customers.add(customer);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
        return customers;
    }

    /***
     * This method uses the contact name passed as a parameter to get a list of type contact.
     * @param contactName The name of the customer
     * @return contacts
     * @throws SQLException
     */
    public ObservableList<Contact> getContactID(String contactName) throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM contacts WHERE contact_name = '" + contactName + "'");
            while(rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("contact_id"),
                        rs.getString("contact_name"),
                        rs.getString("email")
                );
                contacts.add(contact);
            }
            dbConnection.close();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
        return contacts;
    }

    /***
     * This method returns all users in the database.
     * @return users
     * @throws SQLException
     */
    public ArrayList<User> getUserNameList() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            while(rs.next()) {
                User user = new User(
                rs.getInt("user_id"),
                rs.getString("user_name")
                );
                users.add(user);
            }
            dbConnection.close();
        } catch (SQLException e) {
            throw e;
        }
        return users;
    }

    /***
     * This method returns all contacts in the database.
     * @return contacts
     * @throws SQLException
     */
    public ArrayList<Contact> getContactNameList() throws SQLException {
        ArrayList<Contact> contacts = new ArrayList<>();
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM contacts");
            while(rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("contact_id"),
                        rs.getString("contact_name"),
                        rs.getString("email")
                );
                contacts.add(contact);
            }
            dbConnection.close();
        } catch (SQLException e) {
            throw e;
        }
        return contacts;
    }

    /***
     * This method returns all customers in the database.
     * @return customers
     * @throws SQLException
     */
    public ArrayList<Customer> getCustomerNameList() throws SQLException{
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM customers");
            while(rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("postal_code"),
                        rs.getString("phone"),
                        rs.getString("created_by"),
                        rs.getString("last_updated_by"),
                        rs.getInt("division_id")
                );
                customers.add(customer);
            }
            dbConnection.close();
        } catch (SQLException e) {
            throw e;
        }
        return customers;
    }

    /***
     * This method returns a list of user with the name of the username passed as a parameter.
     * @param userName The user's userName
     * @return users
     * @throws SQLException
     */
    public ObservableList<User> getUserID(String userName) throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE user_name = '" + userName + "'");
            while(rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name")
                );
                users.add(user);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
        return users;
    }

    /***
     * This method deletes the appointment with the appointment id passed as a parameter.
     * @param appointmentID The appointment id.
     * @throws SQLException
     */
    public void deleteAppointment(int appointmentID) throws SQLException{
        try {
           Connection dbConnection = conn.getConnection();
           Statement statement = dbConnection.createStatement();
           statement.execute("DELETE FROM appointments WHERE appointment_id = " + appointmentID);
           dbConnection.close();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /***
     * This method takes the userName and password passed as parameters and checks if they are valid.
     * @param userName The userName provided
     * @param password The password provided
     * @return user
     */
    public User checkCredentials(String userName, String password) {
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE user_name = '" + userName + "' AND password = '" + password + "'");
                while(rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name")
                    );
                    return user;
                }
                dbConnection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        User user = new User();
        return null;
    }

    /***
     * This method return a list of upcoming appointments. The current time is checked to see if there are any appointments within the next fifteen minutes.
     * @return appointments
     */
    public ArrayList<Appointment> getUpcomingAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<>();
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM appointments WHERE start >= '" + LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")) + "' AND start <= '" + LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")) + "'");
            while(rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        ZonedDateTime.parse(rs.getTimestamp("start").toString().replace(" ", "T").concat("Z")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                        ZonedDateTime.parse(rs.getTimestamp("end").toString().replace(" ", "T").concat("Z")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                        rs.getString("created_by"),
                        rs.getString("last_updated_by"),
                        rs.getInt("customer_id"),
                        rs.getInt("user_id"),
                        rs.getInt("contact_id")
                );
                appointments.add(appointment);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /***
     * This method returns a list of appointments with the customer id of the passed customer id parameter.
     * @param customerID The provided customer id
     * @return list
     */
    public ArrayList<Appointment> checkSchedule(int customerID) {
        ArrayList<Appointment> list = new ArrayList<>();
        try{
            Connection dbConnection = conn.getConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from appointments where customer_id = " + customerID);
            while(rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        ZonedDateTime.parse(rs.getTimestamp("start").toString().replace(" ", "T").concat("Z")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                        ZonedDateTime.parse(rs.getTimestamp("end").toString().replace(" ", "T").concat("Z")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                        rs.getString("created_by"),
                        rs.getString("last_updated_by"),
                        rs.getInt("customer_id"),
                        rs.getInt("user_id"),
                        rs.getInt("contact_id")
                );
                list.add(appointment);
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
