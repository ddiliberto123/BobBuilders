module com.example.integrativeproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.integrativeproject to javafx.fxml;
    exports com.example.integrativeproject;
}