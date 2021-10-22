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
import sample.model.FirstLevelDivision;
import sample.model.User;
import sample.utils.Conn;
import sample.utils.DbAccess;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

/***
 * This class define the view and behaviors of the addCustomersView.FXML and the users interactions with the addCustomerView user interface.
 * @author Tchaikousky Thomas
 */
public class AddCustomerController {

    final ObservableList<String> countries =  FXCollections.observableArrayList("Canada", "UK", "U.S"); //creates observable list of type string
    final ObservableList<FirstLevelDivision> usDivisions = FXCollections.observableArrayList(); //creates observable list of type FirstLevelDivision
    final ObservableList<FirstLevelDivision> ukDivisions = FXCollections.observableArrayList(); //creates observable list of type FirstLevelDivision
    final ObservableList<FirstLevelDivision> canadaDivisions = FXCollections.observableArrayList(); //creates observable list of type FirstLevelDivision
    final ObservableList<String> usDivisionNames = FXCollections.observableArrayList(); //creates list of US first level division names
    final ObservableList<String> ukDivisionNames = FXCollections.observableArrayList(); //creates list of UK first level division names
    final ObservableList<String> canadaDivisionNames = FXCollections.observableArrayList(); //creates list of Canada first level division names

    @FXML
    private VBox appMenuVBox; //vbox container for app menu

    @FXML
    private MenuItem closeMenuItem; //close menu item

    @FXML
    private TextField nameTextField; //textfield for customer name

    @FXML
    private TextField addressTextField; //textfield for address

    @FXML
    private TextField postalCodeTextField; //textfield for postal code

    @FXML
    private TextField phoneNumberTextField; //textfield for phone number

    @FXML
    private ComboBox<String> divisionComboBox; //combobox for first level divisions

    @FXML
    private ComboBox<String> countryComboBox; //combobox for countries

    @FXML
    private Button saveButton; // save button

    @FXML
    private Button cancelButton; //cancel button

    @FXML
    private Button appointmentsButton; //appointments button

    @FXML
    private Button customersButton; //customers button

    @FXML
    private Button reportsButton; //reports button

    @FXML
    private Label newCustomerLabel; //main label

    @FXML
    private Label customerIDLabel; //label for customer id textfield

    @FXML
    private Label nameLabel; //label for name textfield

    @FXML
    private Label addressLabel; //label for address textfield

    @FXML
    private Label postalCodeLabel; //label for postal code textfield

    @FXML
    private Label phoneNumberLabel; //label for phone number textfield

    @FXML
    private Label divisionLabel; //label for division combobox

    @FXML
    private Label countryLabel; //label for country combobox

    private User user; //User object to set scene's user object


    /***
     * This method sets the scene's fields before the scene is loaded. The labels are set based on the user's default locale. All first level divisions from the database with the three possible
     * country ids are added, and then divided into separate observable list.
     */
    @FXML
    private void initialize() {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        newCustomerLabel.setText(rb.getString("newCustomer"));
        customerIDLabel.setText(rb.getString("customer"));
        nameLabel.setText(rb.getString("name"));
        addressLabel.setText(rb.getString("address"));
        postalCodeLabel.setText(rb.getString("postalCode"));
        phoneNumberLabel.setText(rb.getString("phoneNumber"));
        divisionLabel.setText(rb.getString("division"));
        countryLabel.setText(rb.getString("country"));
        saveButton.setText(rb.getString("save"));
        cancelButton.setText(rb.getString("cancel"));
        appointmentsButton.setText(rb.getString("appointmentsButton"));
        customersButton.setText(rb.getString("customersButton"));
        reportsButton.setText(rb.getString("reportsButton"));
        appMenuVBox.setStyle("-fx-background-color: #416d7d;");

        Conn dbm = new Conn();
        divisionComboBox.setDisable(true);
        countryComboBox.setItems(countries);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        try{
            Connection conn = dbm.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM first_level_divisions WHERE country_id = 1 OR country_id = 2 OR country_id = 3");
            while (rs.next()) {
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(
                        rs.getInt("division_id"),
                        rs.getString("division"),
                        rs.getInt("country_id")
                );
                if(firstLevelDivision.getCountryID() == 3) {
                    canadaDivisions.add(firstLevelDivision);
                    canadaDivisionNames.add(firstLevelDivision.getDivision());
                } else if(firstLevelDivision.getCountryID() == 2) {
                    ukDivisions.add(firstLevelDivision);
                    ukDivisionNames.add(firstLevelDivision.getDivision());
                } else if(firstLevelDivision.getCountryID() == 1) {
                    usDivisions.add(firstLevelDivision);
                    usDivisionNames.add(firstLevelDivision.getDivision());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /***
     * This method determines the action taken after the cancel button is pressed.
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
     * This method determines the action taken when a country is selected from the country combobox. The country's selection determines
     * the list that is populated in the division combobox.
     */
    @FXML
    void  handleCountrySelection() {
        String countryName = countryComboBox.getSelectionModel().getSelectedItem();

        switch(countryName) {
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
        divisionComboBox.setDisable(false);

    }

    /***
     * This method handles the action taken after pressing the save button. After the button is pressed all of the field are checked to ensure
     * all of the fields have been correctly filled and if so proceeds to create the customer object and save it to the database.
     * @param event The action event.
     */
    @FXML
    void handleSaveButton(ActionEvent event) {
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
            try {
                DbAccess dbAccess = new DbAccess();
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

                dbAccess.addCustomer(nameTextField.getText(), addressTextField.getText(), postalCodeTextField.getText(), phoneNumberTextField.getText(), user.getUserName(), user.getUserName(), divisionID);

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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            alert.setContentText(completeContent.toString());
            alert.showAndWait();
        }
    }

    /***
     * This method takes the selected division out of the division combobox and returns the division id.
     * @param selectedDivision The division that was selected.
     * @param observable The observable list that the division belongs to.
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
     * @param user The user that was passed from the previous scene.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /***
     * This method handles the action to take after the close menu item is pressed.
     */
    @FXML
    private void handleClose(ActionEvent event) {
        Platform.exit();
    }

}

