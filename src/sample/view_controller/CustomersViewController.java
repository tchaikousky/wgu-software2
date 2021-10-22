package sample.view_controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sample.model.User;
import sample.utils.Conn;
import sample.utils.DbAccess;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/***
 * This class define the view and behaviors of the customersView.FXML and the users interactions with the customersView user interface.
 * @author Tchaikousky Thomas
 */
public class CustomersViewController {

    @FXML
    private VBox appMenuVBox; //VBox container for the application menu.

    @FXML
    private MenuItem closeMenuItem; //close menu item

    @FXML
    private TableView<Customer> customersTableView; //customers tableview

    @FXML
    private TableColumn<Customer, Integer> customerIDTableColumn; //customer id table column

    @FXML
    private TableColumn<Customer, String> customerNameTableColumn; //customer name table column

    @FXML
    private TableColumn<Customer, String> addressTableColumn; //customer address table column

    @FXML
    private TableColumn<Customer, String> postalCodeTableColumn; //customer postal code table column

    @FXML
    private TableColumn<Customer, String> phoneTableColumn; //customer phone number table column

    @FXML
    private TableColumn<Customer, Integer> divisionIDTableColumn; //customer division id table column

    @FXML
    private Button deleteButton; //delete button

    @FXML
    private Button addButton; //add button

    @FXML
    private Button updateButton; //update button

    @FXML
    private Button appointmentsButton; //appointments button

    @FXML
    private Button customersButton; //customers button

    @FXML
    private Button reportsButton; //reports button

    @FXML
    private Label customerHeaderLabel; //main header

    private User user; //User object


    final ObservableList<Customer> customers = FXCollections.observableArrayList(); //list of customer objects

    /***
     * This method sets the scene's fields before the scene is loaded. The labels are set based on the user's default locale. <b>Lambda are used to set the table column's
     * cell properties.</b>
     */
    @FXML
    private void initialize() {

        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        customerHeaderLabel.setText(rb.getString("customers"));
        customerIDTableColumn.setText(rb.getString("customer_ID"));
        customerNameTableColumn.setText(rb.getString("customerName"));
        addressTableColumn.setText(rb.getString("address"));
        postalCodeTableColumn.setText(rb.getString("postalCode"));
        phoneTableColumn.setText(rb.getString("phone"));
        divisionIDTableColumn.setText(rb.getString("divisionID"));
        appointmentsButton.setText(rb.getString("appointmentsButton"));
        customersButton.setText(rb.getString("customersButton"));
        reportsButton.setText(rb.getString("reportsButton"));
        addButton.setText(rb.getString("add"));
        updateButton.setText(rb.getString("update"));
        deleteButton.setText(rb.getString("delete"));

        Conn dbm = new Conn("jdbc:mysql://wgudb.ucertify.com", "WJ03ST8", "U03ST8", "53688069128");

        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = dbm.getConnection();
            Statement statement = conn.createStatement();
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

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        customerIDTableColumn.setCellValueFactory(test -> test.getValue().customerIDProperty().asObject());
        customerNameTableColumn.setCellValueFactory(test -> test.getValue().customerNameProperty());
        addressTableColumn.setCellValueFactory(test -> test.getValue().addressProperty());
        postalCodeTableColumn.setCellValueFactory(test -> test.getValue().postalCodeProperty());
        phoneTableColumn.setCellValueFactory(test -> test.getValue().phoneProperty());
        divisionIDTableColumn.setCellValueFactory(test -> test.getValue().divisionIDProperty().asObject());
        customersTableView.setItems(customers);
        customersTableView.getSelectionModel().select(0);
        appMenuVBox.setStyle("-fx-background-color: #416d7d;");
        customersTableView.setFixedCellSize(75);
        customersTableView.setStyle("-fx-font-size:16px;");

    }

    /***
     * This method determines what action to take when the delete button is pressed. An alert is displayed to confirm the user wants to delete a customer. Then the database is checked to ensure
     * the customer does not have any appointments schedule and if not the delete is made. An alert is then displayed to confirm which customer was deleted.
     * @param event The action event.
     */
    @FXML
    void handleDeleteButton(ActionEvent event) {
        Customer customer = customersTableView.getSelectionModel().getSelectedItem();
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        int customerToDelete = customer.getCustomerID();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(rb.getString("confirmation"));
            alert.setContentText(rb.getString("confirmDelete"));
            ButtonType yesButton = new ButtonType(rb.getString("yes"), ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType(rb.getString("no"), ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            Optional<ButtonType> pressedButton = alert.showAndWait();
            if(pressedButton.get().getText().equals(rb.getString("yes"))) {
                DbAccess dbAccess = new DbAccess();
                if(dbAccess.hasAppointments(customerToDelete)) {
                    Alert deleteAlert = new Alert(Alert.AlertType.WARNING);
                    deleteAlert.setTitle(rb.getString("unableToDelete"));
                    deleteAlert.setContentText(rb.getString("invalidCustomerDelete"));
                    deleteAlert.showAndWait();
                } else {
                    dbAccess.deleteSingleCustomer(customerToDelete);
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle(rb.getString("successfulDelete"));
                    alert1.setContentText(rb.getString("customer") + ": " + customerToDelete + " - " + customersTableView.getSelectionModel().getSelectedItem().getCustomerName() + " " + rb.getString("successfulDeletion"));
                    alert1.showAndWait();
                }
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
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
     * This method determines what action to take when the add button is pressed. The method creates a new scene and directs the user to
     * the addCustomerView.fxml.
     * @param event The action event
     * @throws IOException
     */
    @FXML
    public void handleAddButton(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addCustomerView.fxml"));
            Parent parent = loader.load();
            AddCustomerController controller = loader.getController();
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
     * This method determines what action should be taken when the update button is pressed. The method creates a new scene and directs the user to
     *      * the updateCustomerView.fxml.
     * @param event The action event.
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    public void handleUpdateButton(ActionEvent event) throws SQLException, IOException {
        Customer customer = customersTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateCustomerView.fxml"));
            Parent parent = loader.load();
            UpdateCustomerController controller = loader.getController();
            controller.setCustomer(customer);
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
