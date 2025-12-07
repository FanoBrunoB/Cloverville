package com.example.cloverville.Login;

import com.example.cloverville.App.ClovervilleApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label errorMessage;

    private LoginModel model;

    public void setModel(LoginModel model) {
        this.model = model;
    }

    @FXML
    private void initialize() {
        // Inicializo el modelo acá si no lo injectás desde afuera
        if (model == null) {
            model = new LoginModel();
        }

        if (errorMessage != null) {
            errorMessage.setVisible(false);
        }
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) throws IOException {
        String user = username.getText();
        String pass = password.getText();

        // Validación campos vacíos
        if (model.isEmpty(user) || model.isEmpty(pass)) {
            errorMessage.setText("Please enter username and password");
            errorMessage.setStyle("-fx-text-fill: #ff6060");
            errorMessage.setVisible(true);
            return;
        }

        // Validar credenciales
        if (model.login(user, pass)) {
            // Login OK → ir a la pantalla Main
            errorMessage.setVisible(false);
            goToMain(event);
        } else {
            // Login inválido
            errorMessage.setText("Invalid credentials");
            errorMessage.setStyle("-fx-text-fill: #ff6060");
            errorMessage.setVisible(true);
        }
    }

    private void goToMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClovervilleApplication.class.getResource("/com/example/cloverville/Dashboard/Dashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 500);
        var cssUrl = ClovervilleApplication.class.getResource("/com/example/cloverville/Css/App.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        stage.setScene(scene);
        stage.setTitle("Cloverville App");
        stage.show();
    }

}
