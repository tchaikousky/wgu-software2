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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.Customer;
import sample.model.User;
import sample.utils.DbAccess;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/***
 * This class define the view and behaviors of the reportsView.FXML and the users interactions with the reportsView user interface.
 * @author Tchaikousky Thomas
 */
public class ReportsController {

    @FXML
    private VBox appMenuVBox; //VBox container for the application menu

    @FXML
    private MenuItem closeMenuItem; //close menu item

    @FXML
    private Button appointmentsButton; //appointments button

    @FXML
    private Button customersButton; //customers button

    @FXML
    private Button reportsButton; //reports button

    @FXML
    private Label reportsHeaderLabel; //main header

    @FXML
    private ComboBox<String> reportTypeComboBox; //report type combobox

    @FXML
    private ComboBox<String> contactNameComboBox; //contact name combobox

    @FXML
    private TextArea customerNumberOfAppointmentsTextArea; //textarea for customer number of appointments report

    @FXML
    private TableView<Appointment> contactScheduleTableView; //tableview for contact schedule report

    @FXML
    private TableColumn<Appointment, Integer> idTableColumn; //appointment id table column

    @FXML
    private TableColumn<Appointment, String> titleTableColumn; //title table column

    @FXML
    private TableColumn<Appointment, String> typeTableColumn; //type table column

    @FXML
    private TableColumn<Appointment, String> descriptionTableColumn; //description table column

    @FXML
    private TableColumn<Appointment, String> startTableColumn; //start table column

    @FXML
    private TableColumn<Appointment, String> endTableColumn; //end table column

    @FXML
    private TableColumn<Appointment, Integer> customerIDTableColumn; //customer id table column

    @FXML
    private TextArea appointmentsByTypeTextArea; //textarea for appointments by type and month report

    @FXML
    ObservableList<String> contactNames = FXCollections.observableArrayList(); //list of contact names

    @FXML
    ObservableList<Appointment> appointments = FXCollections.observableArrayList(); //list of appointments

    @FXML
    ObservableList<Customer> customers = FXCollections.observableArrayList(); //list of customers

    private User user; //User object
    private ArrayList<String> months = new ArrayList<>(); //list of months
    private ArrayList<String> types = new ArrayList<>(); //list of appointment types

    /***
     * This method sets the scene's fields before the scene is loaded. The labels are set based on the user's default locale.
     */
    @FXML
    private void initialize() {
        try {
            DbAccess dbAccess = new DbAccess();
            ArrayList<Contact> contacts = dbAccess.getContactNameList();
            appointments = dbAccess.getAppointmentList();
            customers = dbAccess.getCustomers();

            for(Contact contact : contacts) {
                contactNames.add(contact.getContactName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        ObservableList<String> reportOptions = FXCollections.observableArrayList();
        reportOptions.add(rb.getString("viewAppointmentsByMonthAndType"));
        reportOptions.add(rb.getString("viewScheduleByContact"));
        reportOptions.add(rb.getString("viewCustomerAppointmentTotals"));
        reportTypeComboBox.setItems(reportOptions);
        reportTypeComboBox.setPromptText(rb.getString("typePrompt"));
        contactNameComboBox.setItems(contactNames);
        reportsHeaderLabel.setText(rb.getString("reportsButton"));
        appointmentsButton.setText(rb.getString("appointmentsButton"));
        customersButton.setText(rb.getString("customersButton"));
        idTableColumn.setText(rb.getString("appointmentID"));
        titleTableColumn.setText(rb.getString("title"));
        typeTableColumn.setText(rb.getString("type"));
        descriptionTableColumn.setText(rb.getString("description"));
        startTableColumn.setText(rb.getString("start"));
        endTableColumn.setText(rb.getString("end"));
        customerIDTableColumn.setText(rb.getString("customer_ID"));
        reportsButton.setText(rb.getString("reportsButton"));
        contactNameComboBox.setPromptText(rb.getString("selectContact"));
        idTableColumn.setCellValueFactory(cell -> cell.getValue().appointmentIDProperty().asObject());
        titleTableColumn.setCellValueFactory(cell -> cell.getValue().titleProperty());
        descriptionTableColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        typeTableColumn.setCellValueFactory(cell -> cell.getValue().typeProperty());
        startTableColumn.setCellValueFactory(cell -> cell.getValue().startProperty());
        endTableColumn.setCellValueFactory(cell -> cell.getValue().endProperty());
        customerIDTableColumn.setCellValueFactory(cell -> cell.getValue().customerIDProperty().asObject());
        appMenuVBox.setStyle("-fx-background-color: #416d7d;");
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        types = getTypes();

    }

    /***
     * This method handles the action taken after pressing the appointments button. The appointmentsView.fxml is loaded
     * @param event The action event.
     */
    @FXML
    void handleAppointmentsButton(ActionEvent event) {
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
    void handleCustomersButton(ActionEvent event) {
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
     * This method handles the action taken after the reportType combobox is interacted with.
     * @param event The action event.
     */
    @FXML
    void handleReportType(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        if(reportTypeComboBox.getSelectionModel().getSelectedItem().equals(rb.getString("viewAppointmentsByMonthAndType"))) {
            setAppointmentByMonthAndType();
        } else if(reportTypeComboBox.getSelectionModel().getSelectedItem().equals(rb.getString("viewScheduleByContact"))) {
            viewScheduleByContact();
        } else if(reportTypeComboBox.getSelectionModel().getSelectedItem().equals(rb.getString("viewCustomerAppointmentTotals"))) {
            viewCustomerAppointmentTotals();
        }
    }

    /***
     * This method handles the action taken after pressing the reports button. The reportsView.fxml is loaded
     * @param event The action event.
     */
    @FXML
    void handleReportsButton(ActionEvent event) {
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
     * This method sets the fields for the appointmentByType report.
     */
    @FXML
    private void setAppointmentByMonthAndType() {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        StringBuilder apptByType = new StringBuilder();
        apptByType.append("                                             Jan     Feb     Mar     Apr     May     June    July    Aug     Sep     Oct     Nov     Dec");
        contactNameComboBox.setVisible(false);
        customerNumberOfAppointmentsTextArea.setVisible(false);
        contactScheduleTableView.setVisible(false);
        appointmentsByTypeTextArea.setVisible(true);

        for (String type: types) {
            int spaceCount = 0;
            apptByType.append("\n" + type.toUpperCase());
            for (int i = 0 + 1; i < 47 - type.length() ; i++) {
                apptByType.append(" ");
                spaceCount++;
            }

            for (String month: months) {
                int count = 0;
                for(Appointment appointment: appointments) {
                    if(appointment.getType().equals(type)) {
                        if(LocalDate.parse(appointment.startProperty().getValue().substring(0,10)).getMonth().toString().equals(month.toUpperCase())) {
                            count++;
                        }
                    }
                }
                apptByType.append(count + "       ");
            }
        }
        appointmentsByTypeTextArea.setText(apptByType.toString());
        appointmentsByTypeTextArea.setFont(Font.font("Monospaced", 16));
    }

    /***
     * This method sets the fields for the scheduleByContact report.
     */
    @FXML
    private void viewScheduleByContact() {
        contactNameComboBox.setVisible(true);
        customerNumberOfAppointmentsTextArea.setVisible(false);
        contactScheduleTableView.setVisible(true);
        appointmentsByTypeTextArea.setVisible(false);;

    }

    /***
     * This method sets the fields for the customerAppointmentTotals report.
     */
    @FXML
    private void viewCustomerAppointmentTotals() {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        contactNameComboBox.setVisible(false);
        customerNumberOfAppointmentsTextArea.setVisible(true);
        contactScheduleTableView.setVisible(false);
        appointmentsByTypeTextArea.setVisible(false);

        ArrayList<String[]> customerTotals = new ArrayList<>();
        StringBuilder customerReport = new StringBuilder();
        customerReport.append(rb.getString("customer") + "                              " + rb.getString("numberOfAppointments") + "\n");
        customerReport.append("_______________________________________________________________________\n");
        try {
            DbAccess dbAccess = new DbAccess();
            customerTotals = dbAccess.getCustomerAppointmentTotals();
            for(Customer customer : customers) {
                boolean contain = false;
                for(String[] cust: customerTotals) {
                    if(cust[0].equals(customer.getCustomerName())) {
                        contain = true;
                        break;
                    }
                }
                if(!contain) {
                    String[] array = {customer.getCustomerName(), String.valueOf(0)};
                    customerTotals.add(array);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (String[] customer: customerTotals) {
            customerReport.append(customer[0]);
            for(int i = 0; i < (42 - customer[0].length()); i++) {
                customerReport.append(" ");
            }
            customerReport.append(customer[1]);
            customerReport.append("\n");

        }
        customerNumberOfAppointmentsTextArea.setText(customerReport.toString());
        customerNumberOfAppointmentsTextArea.setFont(Font.font("Monospaced", 16));
    }

    /***
     * This method handles what actions to take when the contactName combobox is interacted with.
     */
    @FXML
    private void handleContactNameComboBox() {

        ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList(appointments.stream()
                .filter(appointment -> appointment.contactNameProperty().getValue().equals(contactNameComboBox.getSelectionModel().getSelectedItem()))
                .collect(Collectors.toList()));
        contactScheduleTableView.getSortOrder().add(startTableColumn);
        contactScheduleTableView.setItems(contactSchedule);

    }

    /***
     * This method returns all of the types of appointments.
     * @return types
     */
    private ArrayList<String> getTypes() {
        ArrayList<String> types = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if(!types.contains(appointment.getType())) {
                types.add(appointment.getType());
            }
        }
        return types;
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