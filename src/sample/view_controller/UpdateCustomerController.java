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
import sample.model.Customer;
import sample.model.FirstLevelDivision;
import sample.model.User;
import sample.utils.DbAccess;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/***
 * This class define the view and behaviors of the updateAppointmentView.FXML and the users interactions with the updateAppointmentView user interface.
 * @author Tchaikousky Thomas
 */
public class UpdateCustomerController {

    private final ObservableList<String> countries = FXCollections.observableArrayList("Canada", "UK", "U.S"); //list of countries
    private ObservableList<FirstLevelDivision> usDivisions = FXCollections.observableArrayList(); //list of us divisions
    private ObservableList<FirstLevelDivision> ukDivisions = FXCollections.observableArrayList(); //list of uk divisions
    private ObservableList<FirstLevelDivision> canadaDivisions = FXCollections.observableArrayList(); //list of canadian divisions
    private final ObservableList<String> usDivisionNames = FXCollections.observableArrayList(); //list of us division names
    private final ObservableList<String> ukDivisionNames = FXCollections.observableArrayList(); //list of uk division names
    private final ObservableList<String> canadaDivisionNames = FXCollections.observableArrayList(); //list of canadian division names

    @FXML
    private VBox appMenuVBox; //VBox container for the application menu

    @FXML
    private MenuItem closeMenuItem; //close menu item

    @FXML
    private TextField customerIDTextField; //customer id textfield

    @FXML
    private TextField nameTextField; //customer name textfield

    @FXML
    private TextField addressTextField; //customer address textfield

    @FXML
    private TextField postalCodeTextField; //customer postal code textfield

    @FXML
    private TextField phoneNumberTextField; //customer phone number textfield

    @FXML
    private ComboBox<String> divisionComboBox; //division combobox

    @FXML
    private ComboBox<String> countryComboBox; //country combobox

    @FXML
    private Button updateButton; //update button

    @FXML
    private Button cancelButton; //cancel button

    @FXML
    private Button appointmentsButton; //appointments button

    @FXML
    private Button customersButton; //customers button

    @FXML
    private Button reportsButton; //reports button

    @FXML
    private Label updateCustomerLabel; //main header label

    @FXML
    private Label customerIDLabel; //customer id label

    @FXML
    private Label nameLabel; //customer name label

    @FXML
    private Label addressLabel; //customer address label

    @FXML
    private Label postalCodeLabel; //customer postal code label

    @FXML
    private Label phoneNumberLabel; //customer phone number label

    @FXML
    private Label divisionLabel; //division label

    @FXML
    private Label countryLabel; //country label

    private User user; //User object

    /***
     * This method sets the scene's fields before the scene is loaded. The labels are set based on the user's default locale.
     */
    @FXML
    private void initialize() {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        updateCustomerLabel.setText(rb.getString("updateCustomer"));
        customerIDLabel.setText(rb.getString("customer"));
        nameLabel.setText(rb.getString("name"));
        addressLabel.setText(rb.getString("address"));
        postalCodeLabel.setText(rb.getString("postalCode"));
        phoneNumberLabel.setText(rb.getString("phoneNumber"));
        divisionLabel.setText(rb.getString("division"));
        countryLabel.setText(rb.getString("country"));
        updateButton.setText(rb.getString("save"));
        cancelButton.setText(rb.getString("cancel"));
        appointmentsButton.setText(rb.getString("appointmentsButton"));
        customersButton.setText(rb.getString("customersButton"));
        reportsButton.setText(rb.getString("reportsButton"));
        appMenuVBox.setStyle("-fx-background-color: #416d7d;");
    }

    /***
     * This method handles which action to take when the cancel button is pressed.
     * @param event The action event.
     * @throws IOException
     */
    @FXML
    void handleCancelButton(ActionEvent event) throws IOException {
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
     * This method determines which action to take when the country combobox is interacted with.
     */
    @FXML
    void handleCountrySelection() {
        divisionComboBox.setDisable(false);
        String divisionName = countryComboBox.getSelectionModel().getSelectedItem();

        switch(divisionName) {
            case "U.S":
                divisionComboBox.setItems(usDivisionNames);
                break;
            case "UK":
                divisionComboBox.setItems(ukDivisionNames);
                break;
            case "Canada":
                divisionComboBox.setItems(canadaDivisionNames);
                break;
            default:
                divisionComboBox.setDisable(true);
        }
    }

    /***
     * This method determines which action to take when the update button is pressed. The fields are checked to ensure that they are not null. Then the fields are check to ensure they meet
     * business requirements and if they do the the customer is updated with the information from the fields.
     * @param event The action event.
     */
    @FXML
    void handleUpdateButton(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        boolean cleared = false;
        StringBuilder completeContent = new StringBuilder();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(rb.getString("warning"));
        String emptyName = rb.getString("emptyName");
        String emptyAddress = rb.getString("emptyAddress");
        String emptyPostalCode = rb.getString("emptyPostalCode");
        String emptyPhoneNumber = rb.getString("emptyPhoneNumber");
        String emptyDivision = rb.getString("emptyDivision");
        String emptyCountry = rb.getString("emptyCountry");

        if(nameTextField.getText().trim().isEmpty()) {
            completeContent.append(emptyName);
            completeContent.append("\n");
        }
        if(addressTextField.getText().trim().isEmpty()) {
            completeContent.append(emptyAddress);
            completeContent.append("\n");
        }
        if(postalCodeTextField.getText().trim().isEmpty()) {
            completeContent.append(emptyPostalCode);
            completeContent.append("\n");
        }
        if(phoneNumberTextField.getText().trim().isEmpty()) {
            completeContent.append(emptyPhoneNumber);
            completeContent.append("\n");
        }
        if(countryComboBox.getSelectionModel().isEmpty()) {
            completeContent.append(emptyCountry);
            completeContent.append("\n");
        }
        if(divisionComboBox.getSelectionModel().isEmpty()) {
            completeContent.append(emptyDivision);
            completeContent.append("\n");
        }

        if(completeContent.toString().trim().isEmpty()) {
            cleared = true;
        }
        if(cleared) {
            String selectedDivision = divisionComboBox.getSelectionModel().getSelectedItem();
            String selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
            int divisionID;

            switch (selectedCountry) {
                case "Canada":
                    divisionID = getDivisionID(selectedDivision, canadaDivisions);
                    break;
                case "UK":
                    divisionID = getDivisionID(selectedDivision, ukDivisions);
                    break;
                case "U.S":
                    divisionID = getDivisionID(selectedDivision, usDivisions);
                    break;
                default:
                    divisionID = 0;
                    break;
            }

            try {
                DbAccess dbAccess = new DbAccess();
                dbAccess.updateCustomer(customerIDTextField.getText(), nameTextField.getText(), addressTextField.getText(), postalCodeTextField.getText(), phoneNumberTextField.getText(), user.getUserName(), divisionID);
            } catch (SQLException e) {
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
        } else {
            alert.setContentText(completeContent.toString());
            alert.showAndWait();
        }
    }

    /***
     * This method sets all of the fields with the customer passed from the parameter. This method is executed before this scene is loaded.
     * @param customer The customer selected from the previous scene.
     */
    @FXML
    public void setCustomer(Customer customer)  {
        int canada = 3;
        int uk = 2;
        int us = 1;
        try {
            DbAccess dbAccess = new DbAccess();
            String country = dbAccess.getCountry(customer);
            ObservableList<FirstLevelDivision> divisionNames;
            customerIDTextField.setText(String.valueOf(customer.getCustomerID()));
            nameTextField.setText(customer.getCustomerName());
            addressTextField.setText(customer.getAddress());
            postalCodeTextField.setText(customer.getPostalCode());
            phoneNumberTextField.setText(customer.getPhone());
            countryComboBox.setItems(countries);
            countryComboBox.getSelectionModel().select(country);

            switch (country) {
                case "Canada":
                    divisionNames = dbAccess.getDivisionList(canada);
                    canadaDivisions = divisionNames;
                    divisionComboBox.setItems(getDivisionNames(canadaDivisions));
                    break;
                case "UK":
                    divisionNames = dbAccess.getDivisionList(uk);
                    ukDivisions = divisionNames;
                    divisionComboBox.setItems(getDivisionNames(ukDivisions));
                    break;
                case "U.S":
                    divisionNames = dbAccess.getDivisionList(us);
                    usDivisions = divisionNames;
                    divisionComboBox.setItems(getDivisionNames(usDivisions));
                    break;
            }

            divisionComboBox.getSelectionModel().select(dbAccess.getDivision(customer));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method returns the division id of the division selected.
     * @param selectedDivision The division selected.
     * @param observable The list the division belongs to.
     * @return divisionID
     */
    public int getDivisionID(String selectedDivision, ObservableList<FirstLevelDivision> observable) {
        int divisionID;
        for (FirstLevelDivision division : observable) {
            if(division.getDivision().equals(selectedDivision)) {
                divisionID = division.getDivisionID();
                return divisionID;
            }
        }
        return 0;
    }

    /***
     * This method returns a list of division names.
     * @param list The list of FirstLevelDivision to get the division names from.
     * @return list of division names
     */
    private ObservableList<String> getDivisionNames(ObservableList<FirstLevelDivision> list) {
        ObservableList<String> stringList = FXCollections.observableArrayList();
        for (FirstLevelDivision division: list) {
            stringList.add(division.getDivision());
        }
        return stringList;
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
