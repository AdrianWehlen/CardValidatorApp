module project2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens adrian.project2 to javafx.fxml;
    exports adrian.project2;
}
