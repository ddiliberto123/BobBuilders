module com.example.FrenzyPenguins {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.FrenzyPenguins to javafx.fxml;
    exports com.example.FrenzyPenguins;
}