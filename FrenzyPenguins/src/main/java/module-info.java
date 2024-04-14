open module org.BobBuilders.FrenzyPenguins {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.almasb.fxgl.all;
    requires org.xerial.sqlitejdbc;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;


//    opens org.BobBuilders.FrenzyPenguins to javafx.fxml;
    exports org.BobBuilders.FrenzyPenguins;
}