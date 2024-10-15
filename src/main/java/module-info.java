module tp1_javafx.tp1_prog2_v2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens tp1_javafx.tp1_prog2_v2 to javafx.fxml;
    exports tp1_javafx.tp1_prog2_v2;
}