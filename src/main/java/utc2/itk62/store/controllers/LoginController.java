package utc2.itk62.store.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utc2.itk62.store.Main;
import utc2.itk62.store.common.User;
import utc2.itk62.store.models.Auth;
import utc2.itk62.store.repositories.AuthRepo;
import utc2.itk62.store.services.LoginService;

import java.io.IOException;

public class LoginController {
    private static final LoginService loginService = new LoginService();
    private static final AuthRepo authRepo = new AuthRepo();

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label statusLabel;

    @FXML
    public void handleBtnLogin(ActionEvent actionEvent) throws IOException {
        User.staff = loginService.CheckLogin(usernameField.getText(), passwordField.getText());
        if (User.staff == null) {
            statusLabel.setText("Failed");
        } else {
            User.staff.getPosition().setAuthList(authRepo.getAllAuthByPosition(User.staff.getPosition()));
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
            stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }
}
