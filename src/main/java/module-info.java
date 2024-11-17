module com.prodigy_sd_03 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires mysql.connector.j;

    opens com.prodigy_sd_03 to javafx.fxml;
    exports com.prodigy_sd_03;
    exports com.prodigy_sd_03.database;
    exports com.prodigy_sd_03.entity;
}
