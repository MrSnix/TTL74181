module TTL74181 {
    requires javafx.controls;
    requires javafx.fxml;
    // This gotta be exported for some undefined behaviour
    exports com.ttl.alu;
    // This stuff must be opens cuz it contains the FXML annotations
    opens com.ttl.alu.gui.controllers;
}