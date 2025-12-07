package com.example.cloverville.App;

import com.example.cloverville.Login.LoginController;
import com.example.cloverville.Login.LoginModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClovervilleApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                ClovervilleApplication.class.getResource("/com/example/cloverville/Login/Login.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 490, 380);
        LoginController controller = fxmlLoader.getController();
        LoginModel model = new LoginModel();
        controller.setModel(model);
        stage.setTitle("Cloverville App");
        stage.setScene(scene);
        stage.show();
    }
}