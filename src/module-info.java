module com.konami.gaming {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.xml;

    exports com.konami.gaming.common;
    exports com.konami.gaming.client;
    exports com.konami.gaming.server;
    exports com.konami.gaming.xml;
}
