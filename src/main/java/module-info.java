module com.example.cloverville {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    exports com.example.cloverville.App;

    opens com.example.cloverville.App to javafx.fxml;
    opens com.example.cloverville.Login to javafx.fxml;
    opens com.example.cloverville.Dashboard to javafx.fxml;
    opens com.example.cloverville.Resident to javafx.base;
    opens com.example.cloverville.GreenAction to javafx.base;
    opens com.example.cloverville.Task to javafx.base;
    opens com.example.cloverville.Product to javafx.base;
}
