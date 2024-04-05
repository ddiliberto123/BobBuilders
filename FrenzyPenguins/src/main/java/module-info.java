open module org.BobBuilders.FrenzyPenguins {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.almasb.fxgl.all;


//    opens org.BobBuilders.FrenzyPenguins to javafx.fxml;
    exports org.BobBuilders.FrenzyPenguins;
}