package sample.view_controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.Customer;
import sample.model.User;
import sample.utils.DbAccess;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/***
 * This class define the view and behaviors of the addAppointmentView.FXML and the users interactions with the addAppointmentView user interface.
 * @author Tchaikousky Thomas
 */
public class AddAppointmentController {

    @FXML
    private VBox appMenuVBox; //vbox container for the menu buttons

    @FXML
    private MenuItem closeMenuItem; //close menu item

    @FXML
    private ComboBox<String> customerIDComboBox; //combobox container for the customer name

    @FXML
    private ComboBox<String> userIDComboBox; //combobox container for the user name

    @FXML
    private TextField titleTextField; //textfield for the title

    @FXML
    private TextField descriptionTextField; //textfield for the description

    @FXML
    private TextField locationTextField; //textfield for the location

    @FXML
    private ComboBox<String> contactComboBox; //combobox for the contact name

    @FXML
    private TextField typeTextField; //textfield for the type

    @FXML
    private DatePicker appointmentDatePicker; //datepicker for the start appointment date and time

    @FXML
    private DatePicker appointmentEndDatePicker; //datepicker for the end appointment date and time

    @FXML
    private ComboBox<String> startHourComboBox; //combobox for the start hour

    @FXML
    private ComboBox<String> startMinuteComboBox; //combobox for the start minute

    @FXML
    private ComboBox<String> endHourComboBox; //combobox for the end hour

    @FXML
    private ComboBox<String> endMinuteComboBox; //combobox for the end minute

    @FXML
    private Button saveButton; //button to save the new appointment

    @FXML
    private Button cancelButton; //button to cancel the creation of a new appointment

    @FXML
    private Button appointmentsButton; //button to direct the user interface to the appointmentsView.fxml

    @FXML
    private Button customersButton; //button to direct the user interface to the customersView.fxml

    @FXML
    private Button reportsButton; //button to direct the user interface to the reportsView.fxml

    @FXML
    private Label addAppointmentHeaderLabel; //main header label

    @FXML
    private Label appointmentIDLabel; //label for the appointment id textfield

    @FXML
    private Label customerLabel; //label for the customer combobox

    @FXML
    private Label userLabel; //label for the user combobox

    @FXML
    private Label titleLabel; //label for the title textfield

    @FXML
    private Label descriptionLabel; //label for the description textfield

    @FXML
    private Label locationLabel; //label for the location textfield

    @FXML
    private Label contactLabel; //label for the contact combobox

    @FXML
    private Label typeLabel; //label for the type textbox

    @FXML
    private Label dateLabel; //label for the datepicker

    @FXML
    private Label startLabel; //start label

    @FXML
    private Label hourLabel; //label for the start hour combobox

    @FXML
    private Label minuteLabel; //label for the start minute combobox

    @FXML
    private Label endLabel; //end label

    @FXML
    private Label endHourLabel; //label for the end hour combobox

    @FXML
    private Label endMinuteLabel; //label for the end minute combobox

    private User user; //User that is passed from the previous scene

    private int businessHours = ZonedDateTime.parse("2021-02-15T13:00Z").withZoneSameInstant(ZoneId.systemDefault()).toLocalTime().getHour(); //gets the start of business hours and converts it from UTC to users local time

    private final ObservableList<String> hours = FXCollections.observableArrayList(); //creates observable list of type string
    private final ObservableList<String> minutes = FXCollections.observableArrayList(); //creates observable list of type string
    private final ObservableList<String> userNames = FXCollections.observableArrayList(); //creates observable list of type string
    private final ObservableList<String> customerNames = FXCollections.observableArrayList(); //creates observable list of type string
    private final ObservableList<String> contactNames = FXCollections.observableArrayList(); //creates observable list of type string

    /***
     * This method sets the scene's fields before the scene is loaded. The labels are set based on the user's default locale. The start and end combo boxes are set based on the users
     * default locale as well. The User, Customer, and Contact ids are used to get the customer name, user name, and contact names from the database.
     */
    @FXML
    private void initialize() {
        ObservableList<User> users;
        ObservableList<Customer> customers;
        ArrayList<Contact> contacts;
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        addAppointmentHeaderLabel.setText(rb.getString("new") + " " + rb.getString("appointment"));
        appointmentIDLabel.setText(rb.getString("appointmentID"));
        customerLabel.setText(rb.getString("customer"));
        userLabel.setText(rb.getString("user"));
        titleLabel.setText(rb.getString("title"));
        descriptionLabel.setText(rb.getString("description"));
        locationLabel.setText(rb.getString("location"));
        contactLabel.setText(rb.getString("contact"));
        typeLabel.setText(rb.getString("type"));
        dateLabel.setText(rb.getString("date"));
        startLabel.setText(rb.getString("start"));
        endLabel.setText(rb.getString("end"));
        hourLabel.setText(rb.getString("hour"));
        minuteLabel.setText(rb.getString("minute"));
        endHourLabel.setText(rb.getString("hour"));
        endMinuteLabel.setText(rb.getString("minute"));
        saveButton.setText(rb.getString("save"));
        cancelButton.setText(rb.getString("cancel"));
        appointmentsButton.setText(rb.getString("appointmentsButton"));
        customersButton.setText(rb.getString("customersButton"));
        reportsButton.setText(rb.getString("reportsButton"));
        appMenuVBox.setStyle("-fx-background-color: #416d7d;");


        int count = 0;

        while(count <= 14) {
            if(businessHours < 10) {
                hours.add(String.format("0%d", businessHours));
            } else {
                hours.add(String.valueOf(businessHours));
            }
            businessHours++;

            if(businessHours == 24 ) {
                businessHours = 0;
            }
            count++;

        }

        for(int i = 0; i < 60; i++) {
            if( i < 10) {
                minutes.add(String.format("0%d", i));
            } else {
                minutes.add(String.valueOf(i));
            }
        }

        startHourComboBox.setItems(hours);
        startMinuteComboBox.setItems(minutes);
        endHourComboBox.setItems(hours);
        endMinuteComboBox.setItems(minutes);

        try {
            DbAccess dbAccess = new DbAccess();
            customers = dbAccess.getCustomers();
            users = dbAccess.getUsers();
            contacts = dbAccess.getContactNameList();
            for(Customer customer: customers) {
                customerNames.add(customer.getCustomerName());
            }
            for(User user: users) {
                userNames.add(user.getUserName());
            }
            for(Contact contact: contacts) {
                contactNames.add(contact.getContactName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        customerIDComboBox.setItems(customerNames);
        userIDComboBox.setItems(userNames);
        contactComboBox.setItems(contactNames);

    }

    /***
     * This method determines the action taken after the cancel button is pressed.
     * @param event The action event.
     * @throws IOException
     */
    @FXML
    void handleCancelButton(ActionEvent event) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentsView.fxml"));
            Parent parent = loader.load();
            AppointmentsController controller = loader.getController();
            controller.setUser(user);
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setMaximized(true);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method handles the action taken after pressing the save button. After the button is press all of the field are checked to ensure
     * all of the fields have been correctly filled and if so proceeds to create the appointment and save it to the database.
     * @param event The action event.
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void handleSaveButton(ActionEvent event) throws SQLException, IOException {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        boolean cleared = false;
        StringBuilder completedContext = new StringBuilder();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(rb.getString("warning"));
        String emptyTitle = rb.getString("emptyTitle");
        String emptyDescription = rb.getString("emptyDescription");
        String emptyLocation = rb.getString("emptyLocation");
        String emptyType = rb.getString("emptyType");
        String emptyDate = rb.getString("emptyDate");
        String emptyStartHour = rb.getString("emptyStartHour");
        String emptyStartMinute = rb.getString("emptyStartMinute");
        String emptyEndHour = rb.getString("emptyEndHour");
        String emptyEndMinute = rb.getString("emptyEndMinute");
        String emptyContact = rb.getString("emptyContact");
        String emptyCustomer = rb.getString("emptyCustomer");
        String emptyUser = rb.getString("emptyUser");
        String invalidAppointmentStart = rb.getString("invalidAppointmentStart");

        DbAccess dbAccess = new DbAccess();

        if(customerIDComboBox.getSelectionModel().isEmpty()) {
            completedContext.append(emptyCustomer);
            completedContext.append("\n");
        }
        if(userIDComboBox.getSelectionModel().isEmpty()) {
            completedContext.append(emptyUser);
            completedContext.append("\n");
        }
        if(titleTextField.getText().isEmpty()) {
            completedContext.append(emptyTitle);
            completedContext.append("\n");
        }
        if(descriptionTextField.getText().trim().isEmpty()) {
            completedContext.append(emptyDescription);
            completedContext.append("\n");
        }
        if(locationTextField.getText().trim().isEmpty()) {
            completedContext.append(emptyLocation);
            completedContext.append("\n");
        }
        if(contactComboBox.getSelectionModel().isEmpty()) {
            completedContext.append(emptyContact);
            completedContext.append("\n");
        }
        if(typeTextField.getText().trim().isEmpty()) {
            completedContext.append(emptyType);
            completedContext.append("\n");
        }
        if(appointmentDatePicker.getValue() == null || appointmentEndDatePicker.getValue() == null) {
            completedContext.append(emptyDate);
            completedContext.append("\n");
        }
        if(startHourComboBox.getSelectionModel().isEmpty()) {
            completedContext.append(emptyStartHour);
            completedContext.append("\n");
        }
        if(startMinuteComboBox.getSelectionModel().isEmpty()) {
            completedContext.append(emptyStartMinute);
            completedContext.append("\n");
        }
        if(endHourComboBox.getSelectionModel().isEmpty()) {
            completedContext.append(emptyEndHour);
            completedContext.append("\n");
        }
        if(endMinuteComboBox.getSelectionModel().isEmpty()) {
            completedContext.append(emptyEndMinute);
            completedContext.append("\n");
        }

        if(completedContext.toString().trim().isEmpty()) {
            cleared = true;
        }

        if(!cleared) {
            alert.setContentText(completedContext.toString());
            alert.showAndWait();
        } else {
            int customerID = dbAccess.getCustomerID(customerIDComboBox.getSelectionModel().getSelectedItem()).get(0).getCustomerID();
            int userID = dbAccess.getUserID(userIDComboBox.getSelectionModel().getSelectedItem()).get(0).getUserId();
            int contactID = dbAccess.getContactID(contactComboBox.getSelectionModel().getSelectedItem()).get(0).getContactId();
            String startAsString = (appointmentDatePicker.getValue() + " " + startHourComboBox.getSelectionModel().getSelectedItem() + ":" + startMinuteComboBox.getSelectionModel().getSelectedItem());
            String endAsString = (appointmentEndDatePicker.getValue() + " " + endHourComboBox.getSelectionModel().getSelectedItem() + ":" + endMinuteComboBox.getSelectionModel().getSelectedItem());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime start = LocalDateTime.parse(startAsString, formatter);
            LocalDateTime end = LocalDateTime.parse(endAsString, formatter);
            ZonedDateTime startTime = start.atZone(ZoneId.systemDefault());
            ZonedDateTime endTime = end.atZone(ZoneId.systemDefault());
            ZonedDateTime convertStart = startTime.withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime convertEnd = endTime.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime startUTC = convertStart.toLocalDateTime();
            LocalDateTime endUTC = convertEnd.toLocalDateTime();

            ArrayList<Appointment> customerAppointments = dbAccess.checkSchedule(customerID);
            StringBuilder invalidAppointment = new StringBuilder();
            invalidAppointment.append(customerIDComboBox.getSelectionModel().getSelectedItem() + " " + rb.getString("alreadyScheduled"));
            alert.setContentText(invalidAppointment.toString());
            boolean check = true;
            for(Appointment appointment : customerAppointments) {
                if(start.isAfter(LocalDateTime.parse(appointment.startProperty().getValue().replace(" ", "T"))) && start.isBefore(LocalDateTime.parse(appointment.endProperty().getValue().replace(" ", "T")))) {
                    alert.showAndWait();
                    check = false;
                    break;
                } else if(start.isBefore(LocalDateTime.parse(appointment.startProperty().getValue().replace(" ", "T"))) && end.isAfter(LocalDateTime.parse(appointment.endProperty().getValue().replace(" ", "T")))) {
                    alert.showAndWait();
                    check = false;
                    break;
                } else if(start.isEqual(LocalDateTime.parse(appointment.startProperty().getValue().replace(" ", "T")))
                        || start.isEqual(LocalDateTime.parse(appointment.endProperty().getValue().replace(" ", "T")))
                        || end.isEqual(LocalDateTime.parse(appointment.startProperty().getValue().replace(" ", "T")))
                        || end.isEqual(LocalDateTime.parse(appointment.endProperty().getValue().replace(" ", "T")))) {
                    alert.showAndWait();
                    check = false;
                    break;
                } else if(end.isAfter(LocalDateTime.parse(appointment.startProperty().getValue().replace(" ", "T"))) && end.isBefore(LocalDateTime.parse(appointment.endProperty().getValue().replace(" ", "T")))) {
                    alert.showAndWait();
                    check = false;
                    break;
                }
            }

            if(check) {
                if (start.isAfter(end)) {
                    completedContext.append(invalidAppointmentStart);
                    alert.setContentText(completedContext.toString());
                    alert.showAndWait();
                } else {
                    dbAccess.addAppointment(titleTextField.getText(), descriptionTextField.getText(), locationTextField.getText(), typeTextField.getText(), startUTC, endUTC, userIDComboBox.getSelectionModel().getSelectedItem(), customerID, userID, contactID);
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentsView.fxml"));
                        Parent parent = loader.load();
                        AppointmentsController controller = loader.getController();
                        controller.setUser(user);
                        Scene scene = new Scene(parent);
                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setScene(scene);
                        window.setMaximized(true);
                        window.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /***
     * This method handles the action taken after pressing the appointments button. The appointmentsView.fxml is loaded
     * @param event The action event.
     */
    @FXML
    private void handleAppointmentsButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentsView.fxml"));
            Parent parent = loader.load();
            AppointmentsController controller = loader.getController();
            controller.setUser(user);
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setMaximized(true);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method handles the action taken after pressing the customers button. The customersView.fxml is loaded
     * @param event The action event.
     */
    @FXML
    private void handleCustomersButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customersView.fxml"));
            Parent parent = loader.load();
            CustomersViewController controller = loader.getController();
            controller.setUser(user);
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setMaximized(true);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method handles the action taken after pressing the reports button. The reportsView.fxml is loaded
     * @param event The action event.
     */
    @FXML
    private void handleReportsButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reportsView.fxml"));
            Parent parent = loader.load();
            ReportsController controller = loader.getController();
            controller.setUser(user);
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setMaximized(true);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method handles the action taken when the start and end hour comboboxes are used. The method determines which hour is selected and if it is the
     * last hour of business hours, then it restricts the minutes options.
     * @param event The action event.
     */
    @FXML
    private void handleHoursSelection(ActionEvent event) {
        ObservableList<String> closingMinutes = FXCollections.observableArrayList();
        closingMinutes.add("00");
        Node node = (Node) event.getSource();
        if(node.getId().equals("startHourComboBox")) {
            if (startHourComboBox.getSelectionModel().getSelectedItem().equals(String.valueOf(businessHours -1))) {
                startMinuteComboBox.setItems(closingMinutes);
            } else {
                startMinuteComboBox.setItems(minutes);
            }
        } else {
            if (endHourComboBox.getSelectionModel().getSelectedItem().equals(String.valueOf(businessHours -1))) {
                endMinuteComboBox.setItems(closingMinutes);
            } else {
                endMinuteComboBox.setItems(minutes);
            }
        }
    }

    /***
     * This method receives an User object from the previous scene and sets it as the User object for this scene.
     * @param user The user object that is passed from the previous scene.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /***
     * This method handles the action to take when the close menu item is pressed.
     */
    @FXML
    private void handleClose() {
        Platform.exit();
    }

}

