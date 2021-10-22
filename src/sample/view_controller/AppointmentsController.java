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
import sample.model.User;
import sample.utils.DbAccess;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/***
 * This class define the view and behaviors of the appointmentsView.FXML and the users interactions with the appointmentView user interface.
 * @author Tchaikousky Thomas
 */
public class AppointmentsController {

    @FXML
    private VBox appMenuVBox; //VBox container for application menu

    @FXML
    private MenuItem closeMenuItem; //close menu item

    @FXML
    private RadioButton weeklyRadioButton; //RadioButton for weekly tableView

    @FXML
    private RadioButton monthlyRadioButton; //RadioButton for monthly tableView

    @FXML
    private RadioButton allRadioButton; //RadioButton for all tableView

    @FXML
    private TableView<Appointment> appointmentTableView; //appointments Tableview

    @FXML
    private TableColumn<Appointment, Integer> appointmentIDTableColumn; //appointment id table column

    @FXML
    private TableColumn<Appointment, String> titleTableColumn;  //title table column

    @FXML
    private TableColumn<Appointment, String> descriptionTableColumn; //description table column

    @FXML
    private TableColumn<Appointment, String> locationTableColumn; //location table column

    @FXML
    private TableColumn<Appointment, String> contactTableColumn; //contact table column

    @FXML
    private TableColumn<Appointment, String> typeTableColumn; //type table column

    @FXML
    private  TableColumn<Appointment, String> startTableColumn; //start table column

    @FXML
    private TableColumn<Appointment, String> endTableColumn; //end table column

    @FXML
    private TableColumn<Appointment, Integer> customerIDTableColumn; //customer id table column

    @FXML
    private Button newButton; //new button

    @FXML
    private Button updateButton; //update button

    @FXML
    private Button deleteButton; //delete button

    @FXML
    private Button appointmentsButton; //appointments button

    @FXML
    private Button customersButton; //customers button

    @FXML
    private Button reportsButton; //reports button

    @FXML
    private Label appointmentsHeaderLabel; //main button

    private User user; //User object to set scene's user object

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList(); //observable list of type appointment
    private ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList(); //observable list of type appointment
    private ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList(); //observable list of type appointment

    /***
     * This method sets the scene's fields before the scene is loaded. The labels are set based on the user's default locale. <b>Lambdas are used to filter through the appointments list and create
     * new dynamic weekly and monthly lists. Lambdas are used again to set the cell factories of the appointment table view table columns.</b>
     */
    @FXML
    private void initialize() {

        try {
            DbAccess dbAccess = new DbAccess();
            appointments = dbAccess.getAppointmentList();
            weeklyAppointments = FXCollections.observableArrayList(appointments.stream()
                    .filter(appointment -> LocalDateTime.parse(appointment.startProperty().getValue()
                    .replace(" ", "T")).isBefore(LocalDateTime.now().plusWeeks(1))&&LocalDateTime
                    .parse(appointment.endProperty().getValue().replace(" ", "T")).isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList()));
            monthlyAppointments = FXCollections.observableArrayList(appointments.stream()
                    .filter(appointment -> LocalDateTime.parse(appointment.startProperty().getValue()
                    .replace(" ", "T")).isBefore(LocalDateTime.now().plusMonths(1))&&LocalDateTime
                    .parse(appointment.endProperty().getValue().replace(" ", "T")).isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        appointmentsHeaderLabel.setText(rb.getString("appointmentsHeaderLabel"));
        weeklyRadioButton.setText(rb.getString("weekly"));
        monthlyRadioButton.setText(rb.getString("monthly"));
        allRadioButton.setText(rb.getString("all"));
        appointmentIDTableColumn.setText(rb.getString("appointmentID"));
        titleTableColumn.setText(rb.getString("title"));
        descriptionTableColumn.setText(rb.getString("description"));
        locationTableColumn.setText(rb.getString("location"));
        contactTableColumn.setText(rb.getString("contact"));
        typeTableColumn.setText(rb.getString("type"));
        startTableColumn.setText(rb.getString("start"));
        endTableColumn.setText(rb.getString("end"));
        customerIDTableColumn.setText(rb.getString("customer_ID"));
        newButton.setText(rb.getString("new"));
        updateButton.setText(rb.getString("update"));
        deleteButton.setText(rb.getString("delete"));
        appointmentsButton.setText(rb.getString("appointmentsButton"));
        customersButton.setText(rb.getString("customersButton"));
        reportsButton.setText(rb.getString("reportsButton"));
        appointmentIDTableColumn.setCellValueFactory(cell -> cell.getValue().appointmentIDProperty().asObject());
        titleTableColumn.setCellValueFactory(cell -> cell.getValue().titleProperty());
        descriptionTableColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        locationTableColumn.setCellValueFactory(cell -> cell.getValue().locationProperty());
        contactTableColumn.setCellValueFactory(cell -> cell.getValue().contactNameProperty());
        typeTableColumn.setCellValueFactory(cell -> cell.getValue().typeProperty());
        startTableColumn.setCellValueFactory(cell -> cell.getValue().startProperty());
        endTableColumn.setCellValueFactory(cell -> cell.getValue().endProperty());
        customerIDTableColumn.setCellValueFactory(cell -> cell.getValue().customerIDProperty().asObject());
        weeklyRadioButton.selectedProperty().set(true);
        appointmentTableView.setItems(weeklyAppointments);
        appointmentTableView.getSelectionModel().select(0);
        appMenuVBox.setStyle("-fx-background-color: #416d7d;");
        appointmentTableView.setFixedCellSize(75);
        appointmentTableView.setStyle("-fx-font-size:16px;");
        appointmentTableView.getSortOrder().add(startTableColumn);

    }

    /***
     * This method determines what action to take when the delete button is pressed. An alert is displayed to confirm the users intent of deleting an appointment. If the user confirms the delete,
     * then another alert is displayed to confirm to the user what was deleted.
     * @param event The action event.
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void handleDeleteButton(ActionEvent event) throws SQLException, IOException {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        int appointmentID = appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentID();
        try {
            DbAccess dbAccess = new DbAccess();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(rb.getString("confirmation"));
            alert.setContentText(rb.getString("confirmDelete"));
            ButtonType yesButton = new ButtonType(rb.getString("yes"), ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType(rb.getString("no"), ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            Optional<ButtonType> pressedButton = alert.showAndWait();
            if(pressedButton.get().getText().equals(rb.getString("yes"))) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle(rb.getString("successfulDelete"));
                alert1.setContentText(rb.getString("appointment") + ": id - " + appointmentID + " - " + rb.getString("type") + " - " + appointmentTableView.getSelectionModel().getSelectedItem().getType() + " " + rb.getString("successfulDeletion"));
                alert1.showAndWait();
                dbAccess.deleteAppointment(appointmentID);

            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }

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
     * This method determines what action to take when the monthly RadioButton is selected. If the monthly RadioButton is selected, then the 'weekly'
     * and 'all' RadioButtons are automatically deselected.
     */
    @FXML
    void handleMonthlyRadioButton() {
        monthlyRadioButton.selectedProperty().set(true);
        weeklyRadioButton.selectedProperty().set(false);
        allRadioButton.selectedProperty().set(false);
        appointmentTableView.setItems(monthlyAppointments);
        appointmentTableView.getSortOrder().add(startTableColumn);
    }

    /***
     * This method determines what action to take when the weekly RadioButton is selected. If the weekly RadioButton is selected, then the 'monthly'
     * and 'all' RadioButtons are automatically deselected.
     */
    @FXML
    void handleWeeklyRadioButton() {
        weeklyRadioButton.selectedProperty().set(true);
        monthlyRadioButton.selectedProperty().set(false);
        allRadioButton.selectedProperty().set(false);
        appointmentTableView.setItems(weeklyAppointments);
        appointmentTableView.getSortOrder().add(startTableColumn);

    }

    /***
     * This method determines what action to take when the all RadioButton is selected. If the all RadioButton is selected, then the 'weekly'
     * and 'monthly' RadioButtons are automatically deselected.
     */
    @FXML
    private void handleAllRadioButton() {
        weeklyRadioButton.selectedProperty().set(false);
        monthlyRadioButton.selectedProperty().set(false);
        allRadioButton.selectedProperty().set(true);
        appointmentTableView.setItems(appointments);
        appointmentTableView.getSortOrder().add(startTableColumn);

    }

    /***
     * This method determines what action to take when the new button is pressed. The scene is redirected to addAppointmentView.fxml upon completeion of this method.
     * @param event The action event.
     */
    @FXML
    void handleNewButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addAppointmentView.fxml"));
            Parent parent = loader.load();
            AddAppointmentController controller = loader.getController();
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
     * This method determines what action to take when the update button is pressed. The scene is redirected to updateAppointmentView.fxml upon the completion of this method.
     * The updateAppointmentView scene is set from the selected appointment in the appointments tableView.
     * @param event The action event.
     * @throws SQLException
     */
    @FXML
    void handleUpdateButton(ActionEvent event) throws SQLException {
        Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateAppointmentView.fxml"));
            Parent parent = loader.load();
            UpdateAppointmentController controller = loader.getController();
            controller.setAppointment(appointment);
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
