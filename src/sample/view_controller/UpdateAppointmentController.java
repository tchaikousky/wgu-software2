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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/***
 * This class define the view and behaviors of the updateAppointmentView.FXML and the users interactions with the updateAppointmentView user interface.
 * @author Tchaikousky Thomas
 */
public class UpdateAppointmentController {

    @FXML
    private VBox appMenuVBox; //VBox container for the application menu.

    @FXML
    private MenuItem closeMenuItem; //close menu item

    @FXML
    private TextField appointmentIDTextField; //appointment id textfield

    @FXML
    private ComboBox<String> customerComboBox; //customer name combobox

    @FXML
    private ComboBox<String> userComboBox; //user name combobox

    @FXML
    private TextField titleTextField; //title textfield

    @FXML
    private TextField descriptionTextField; //description textfield

    @FXML
    private TextField locationTextField; //location textfield

    @FXML
    private ComboBox<String> contactComboBox; //contact name combobox

    @FXML
    private TextField typeTextField; //type textfield

    @FXML
    private DatePicker appointmentDatePicker; //appointment start date picker

    @FXML
    private DatePicker appointmentEndDatePicker; //appointment end date picker

    @FXML
    private ComboBox<String> startHourComboBox; //start hour combobox

    @FXML
    private ComboBox<String> startMinuteComboBox; //start minute combobox

    @FXML
    private ComboBox<String> endHourComboBox; //end hour combobox

    @FXML
    private ComboBox<String> endMinuteComboBox; //end minute combobox

    @FXML
    private Button saveButton; //save button

    @FXML
    private Button cancelButton; //cancel button

    @FXML
    private Button appointmentsButton; //appointments button

    @FXML
    private Button customersButton; //customers button

    @FXML
    private Button reportsButton; //reports button

    @FXML
    private Label updateAppointmentHeaderLabel; //main header label

    @FXML
    private Label appointmentIDLabel; //appointment id label

    @FXML
    private Label customerLabel; //customer label

    @FXML
    private Label userLabel; //user label

    @FXML
    private Label titleLabel; //title label

    @FXML
    private Label descriptionLabel; //description label

    @FXML
    private Label locationLabel; //location label

    @FXML
    private Label contactLabel; //contact label

    @FXML
    private Label typeLabel; //type label

    @FXML
    private Label dateLabel; //date label

    @FXML
    private Label startLabel; //start label

    @FXML
    private Label endLabel; //end label

    @FXML
    private Label hourLabel; //start hour label

    @FXML
    private Label minuteLabel; //start minute label

    @FXML
    private Label endHourLabel; //end hour label

    @FXML
    private Label endMinuteLabel; //end minute label

    private User user; //User object

    private int businessHours = ZonedDateTime.parse("2021-02-15T13:00Z").withZoneSameInstant(ZoneId.systemDefault()).toLocalTime().getHour(); //start of business hours

    private ArrayList<Customer> customers = new ArrayList<>(); //list of customers
    private ArrayList<Contact> contacts = new ArrayList<>(); //list of contacts
    private ArrayList<User> users = new ArrayList<>(); //list of users
    private ObservableList<String> customerNames = FXCollections.observableArrayList(); //list of customer names
    private ObservableList<String> contactNames = FXCollections.observableArrayList(); //list of contact names
    private ObservableList<String>  userNames = FXCollections.observableArrayList(); //list of user names
    private final ObservableList<String> hours = FXCollections.observableArrayList(); //list of hours
    private final ObservableList<String> minutes = FXCollections.observableArrayList(); //list of minutes

    /***
     * This method sets the scene's fields before the scene is loaded. The labels are set based on the user's default locale.
     */
    @FXML
    private void initialize() {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        updateAppointmentHeaderLabel.setText(rb.getString("updateAppointment"));
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

    }

    /***
     * This method determines what action to take when the cancel button is pressed.
     * @param event The action event.
     * @throws IOException
     */
    @FXML
    void handleCancelButton(ActionEvent event) throws IOException {
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
     * This method determines what action is taken when the save button is pressed. The fields are checked to ensure none of the fields are null. Then the fields are check against business
     * requirements to ensure the requirements are met. The database is then updated with the information from the input fields.
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

        if(customerComboBox.getSelectionModel().isEmpty()) {
            completedContext.append(emptyCustomer);
            completedContext.append("\n");
        }
        if(userComboBox.getSelectionModel().isEmpty()) {
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
            int customerID = dbAccess.getCustomerID(customerComboBox.getSelectionModel().getSelectedItem()).get(0).getCustomerID();
            int userID = dbAccess.getUserID(userComboBox.getSelectionModel().getSelectedItem()).get(0).getUserId();
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
            invalidAppointment.append(customerComboBox.getSelectionModel().getSelectedItem() + " " + rb.getString("alreadyScheduled"));
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
                    dbAccess.addAppointment(titleTextField.getText(), descriptionTextField.getText(), locationTextField.getText(), typeTextField.getText(), startUTC, endUTC, userComboBox.getSelectionModel().getSelectedItem(), customerID, userID, contactID);
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
     * This method sets all of the fields with the appointment passed from the parameter. This method is executed before this scene is loaded.
     * @param appointment The appointment selected from the previous scene.
     * @throws SQLException
     */
    @FXML
    public void setAppointment(Appointment appointment) throws SQLException {
        String endAsString = appointment.endProperty().getValue();
        String startAsString = appointment.startProperty().getValue();
        String[] startDate = startAsString.split(" ");
        String[] startTime = startDate[1].split(":");
        String startHour = startTime[0];
        String startMinute = startTime[1];
        String[] endDate = endAsString.split(" ");
        String[] endTime = endDate[1].split(":");
        String endHour = endTime[0];
        String endMinute = endTime[1];

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

        try {
            DbAccess dbAccess = new DbAccess();
            users = dbAccess.getUserNameList();
            contacts = dbAccess.getContactNameList();
            customers = dbAccess.getCustomerNameList();
        } catch (SQLException e) {
            throw e;
        }

        for (Customer customer:customers) {
            customerNames.add(customer.getCustomerName());
        }
        for (User user:users) {
            userNames.add(user.getUserName());
        }
        for (Contact contact:contacts) {
            contactNames.add(contact.getContactName());
        }

        appointmentIDTextField.setText(String.valueOf(appointment.getAppointmentID()));
        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        typeTextField.setText(appointment.getType());
        customerComboBox.setItems(customerNames);
        contactComboBox.setItems(contactNames);
        userComboBox.setItems(userNames);
        customerComboBox.getSelectionModel().select(getCustomerName(appointment));
        contactComboBox.getSelectionModel().select(getContactName(appointment));
        userComboBox.getSelectionModel().select(getUserName(appointment));
        startHourComboBox.setItems(hours);
        startMinuteComboBox.setItems(minutes);
        endHourComboBox.setItems(hours);
        endMinuteComboBox.setItems(minutes);
        appointmentDatePicker.setValue(LocalDate.parse(startDate[0]));
        appointmentEndDatePicker.setValue(LocalDate.parse(endDate[0]));
        startHourComboBox.getSelectionModel().select(startHour);
        startMinuteComboBox.getSelectionModel().select(startMinute);
        endHourComboBox.getSelectionModel().select(endHour);
        endMinuteComboBox.getSelectionModel().select(endMinute);

    }

    /***
     * This method returns the customer's name of the appointment passed in the parameter.
     * @param appointment The appointment the customer name comes from.
     * @return
     */
    public String getCustomerName(Appointment appointment) {
        for(Customer person:customers) {
            if(person.getCustomerID() == appointment.getCustomerID()) {
                return person.getCustomerName();
            }
        };
        return null;
    }

    /***
     * This method returns the contact's name of the appointment passed in the parameter.
     * @param appointment The appointment the contact name comes from.
     * @return
     */
    public String getContactName(Appointment appointment) {
        for(Contact contact:contacts) {
            if(contact.getContactId() == appointment.getContactID()) {
                return contact.getContactName();
            }
        };
        return null;
    }

    /***
     * This method returns the user's name of the appointment passed in the parameter.
     * @param appointment The appointment the user name comes from.
     * @return
     */
    public String getUserName(Appointment appointment) {
        for(User user:users) {
            if(user.getUserId() == appointment.getUserID()) {
                return user.getUserName();
            }
        };
        return null;
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
     * This method handles the action to take when the hours combobox is interacted with.
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
     * @param user The user passed from the previous scene.
     */
    @FXML
    public void setUser(User user) {
        this.user = user;
    }

    /***
     * This method handles the action to take after the close menu item is pressed.
     * @param event The action event
     */
    @FXML
    private void handleClose(ActionEvent event) {
        Platform.exit();
    }

}

