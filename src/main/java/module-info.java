module TTL {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    exports com.ttl.alu;
    opens com.ttl.alu.gui.controllers;
}