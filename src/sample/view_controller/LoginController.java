package sample.view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.model.Appointment;
import sample.model.User;
import sample.utils.DbAccess;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/***
 * This class define the view and behaviors of the login.FXML and the users interactions with the login user interface.
 * @author Tchaikousky Thomas
 */
public class LoginController {

    @FXML
    private TextField userNameTextField; //userName TextField

    @FXML
    private PasswordField passwordPasswordField; //password Password

    @FXML
    private Button loginButton; //login button

    @FXML
    private Label loginHeaderLabel; //main header

    @FXML
    private Label userNameLabel; //userName TextField label

    @FXML
    private Label passwordLabel; //password PasswordField label

    @FXML
    private Label zoneLabel; //label for zone id

    /***
     * This method sets the scene's fields before the scene is loaded. The labels are set based on the user's default locale.
     */
    @FXML
    private void initialize() {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        loginHeaderLabel.setText(rb.getString("loginHeaderLabel"));
        userNameLabel.setText(rb.getString("userNameLabel"));
        passwordLabel.setText(rb.getString("passwordLabel"));
        loginButton.setText(rb.getString("login"));
        zoneLabel.setText(ZoneId.systemDefault().getId());

    }

    /***
     * This method determines what actions to take when the login button is pressed. All fields are checked to ensure that
     * they are not null, then the values are checked at the database to ensure they match.
     * @param event The action event
     * @throws SQLException
     */
    @FXML
    void handleLoginButton(ActionEvent event) throws SQLException {
        ResourceBundle rb = ResourceBundle.getBundle("sample/UTILRB", Locale.getDefault());
        StringBuilder completeContent = new StringBuilder();
        boolean cleared = completeContent.toString().trim().isEmpty();
        if(userNameTextField.getText().trim().isEmpty()) {
            completeContent.append(rb.getString("emptyUserNameField") + "\n");
        }
        if(passwordPasswordField.getText().trim().isEmpty()) {
            completeContent.append(rb.getString("emptyPasswordField") + "\n");
        }
        if(cleared) {
            DbAccess dbAccess = new DbAccess();
            User user = dbAccess.checkCredentials(userNameTextField.getText(), passwordPasswordField.getText());

            if(user!=null) {
                try {
                    loginAttempts(userNameTextField.getText(), "Successful");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentsView.fxml"));
                    Parent parent = loader.load();
                    AppointmentsController controller = loader.getController();
                    controller.setUser(user);
                    Scene scene = new Scene(parent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();
                    window.setWidth(bounds.getWidth());
                    window.setHeight(bounds.getHeight());

                    window.setScene(scene);
                    window.setMaximized(true);
                    window.show();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<Appointment> appointments = dbAccess.getUpcomingAppointments();
                completeContent = new StringBuilder();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(rb.getString("appointmentCheck"));
                if(appointments.size() > 0) {
                    completeContent.append(rb.getString("upcomingAppointments") + ":\n");
                    completeContent.append("---------------------------------------------------\n");
                    completeContent.append(" ");
                    for (Appointment appointment : appointments) {
                        completeContent.append(rb.getString("appointmentID") + " - " + appointment.getAppointmentID() + "     " +
                                rb.getString("start") + " - " + appointment.startProperty().getValue() + "     " +
                                rb.getString("end") + " - " + appointment.endProperty().getValue() + "\n\n");
                    }
                } else {
                    completeContent.append(rb.getString("noAppointments"));
                }
                alert.setContentText(completeContent.toString());
                alert.getButtonTypes().remove(1);
                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(rb.getString("warning"));
                alert.setContentText(rb.getString("invalidCredentials"));
                alert.showAndWait();
                try {
                    loginAttempts(userNameTextField.getText(), "Unsuccessful");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("warning"));
            alert.setContentText(completeContent.toString());
            alert.showAndWait();
        }


    }

    /***
     * This method saves all attempted logins to a text file named login_activity.txt. The string typed into the userName TextField and a current timestamp is appended to the file.
     * @param userName The string that was typed into the userName TextField.
     * @param outcome The string outcome of the attempted login. (Sucessful or Unsuccessful).
     * @throws IOException
     */
    private void loginAttempts(String userName, String outcome) throws IOException {
        StringBuilder loginData = new StringBuilder();
        Timestamp now = Timestamp.from(Instant.now());
        loginData.append(userName);
        for(int i = 0; i <= 25 - userName.length(); i++) {
            loginData.append(" ");
        }
        loginData.append(now);
        for(int i = 0; i < 50 - now.toString().length(); i++) {
            loginData.append(" ");
        }
        loginData.append(outcome + "\n");
        Files.write(
                Paths.get("login_activity.txt"),
                loginData.toString().getBytes(),
                StandardOpenOption.APPEND
        );
    }

}
