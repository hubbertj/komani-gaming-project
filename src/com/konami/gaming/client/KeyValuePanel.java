package com.konami.gaming.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX editable key-value pair editor - matches server card styling.
 */
public class KeyValuePanel extends VBox {

    private final VBox rowsBox;
    private final TextField commandField;
    private final List<KeyValueRow> rows = new ArrayList<>();

    private static final String CARD_STYLE =
        "-fx-background-color: white; -fx-background-radius: 10; " +
        "-fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-border-width: 1; " +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 6, 0, 0, 1);";

    public KeyValuePanel() {
        setSpacing(12);
        setPadding(new Insets(0));
        setStyle("-fx-background-color: #f8fafc;");

        VBox headerCard = new VBox(8);
        headerCard.setPadding(new Insets(16, 20, 16, 20));
        headerCard.setStyle(CARD_STYLE);
        Label cmdLbl = new Label("COMMAND");
        cmdLbl.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11; -fx-font-weight: bold;");
        commandField = new TextField("Print");
        commandField.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 6; -fx-padding: 8 12;");
        headerCard.getChildren().addAll(cmdLbl, commandField);

        Button addButton = new Button("+ Add");
        addButton.setStyle(
            "-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; " +
            "-fx-background-radius: 6; -fx-padding: 6 14; -fx-font-size: 12; -fx-font-weight: bold;");
        addButton.setOnAction(e -> addRow("", ""));

        rowsBox = new VBox(10);
        rowsBox.setStyle("-fx-background-color: #f8fafc;");
        ScrollPane scroll = new ScrollPane(rowsBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f8fafc; -fx-background: #f8fafc; -fx-border-width: 0;");
        scroll.setPadding(new Insets(0));
        VBox.setVgrow(scroll, Priority.ALWAYS);

        getChildren().addAll(headerCard, addButton, scroll);

        addRow("Name", "John Doe");
        addRow("Address", "123 Main Street");
    }

    public void addRow(String key, String value) {
        KeyValueRow row = new KeyValueRow(this::removeRow);
        row.setKey(key);
        row.setValue(value);
        rows.add(row);
        rowsBox.getChildren().add(row);
    }

    private void removeRow(KeyValueRow row) {
        if (rows.size() <= 1) return;
        rows.remove(row);
        rowsBox.getChildren().remove(row);
    }

    public List<KeyValue> getKeyValues() {
        List<KeyValue> result = new ArrayList<>();
        for (KeyValueRow r : rows) {
            String k = r.getKey().trim();
            String v = r.getValue().trim();
            if (!k.isEmpty() || !v.isEmpty()) {
                result.add(new KeyValue(k.isEmpty() ? " " : k, v));
            }
        }
        return result;
    }

    public String getCommand() {
        return commandField.getText().trim();
    }

    public String toXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
        sb.append("<Message>\n");
        sb.append("  <Command>").append(escapeXml(getCommand())).append("</Command>\n");
        sb.append("  <Data>\n");
        for (KeyValue kv : getKeyValues()) {
            sb.append("     <Row>\n");
            sb.append("       <Description>\"").append(escapeXml(kv.key)).append("\"</Description>\n");
            sb.append("       <Value>\"").append(escapeXml(kv.value)).append("\"</Value>\n");
            sb.append("     </Row>\n");
        }
        sb.append("  </Data>\n");
        sb.append("</Message>");
        return sb.toString();
    }

    private static String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    public static class KeyValue {
        public final String key;
        public final String value;
        KeyValue(String key, String value) { this.key = key; this.value = value; }
    }

    private static class KeyValueRow extends HBox {
        private final TextField keyField;
        private final TextField valueField;

        KeyValueRow(java.util.function.Consumer<KeyValueRow> onRemove) {
            setSpacing(10);
            setPadding(new Insets(12, 16, 12, 16));
            setAlignment(Pos.CENTER_LEFT);
            setStyle(CARD_STYLE);

            keyField = new TextField();
            keyField.setPrefWidth(90);
            keyField.setMinWidth(60);
            keyField.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 6; -fx-padding: 8 10;");
            valueField = new TextField();
            valueField.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 6; -fx-padding: 8 10;");
            HBox.setHgrow(valueField, Priority.ALWAYS);
            Button removeBtn = new Button("−");
            removeBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 6;");
            removeBtn.setPrefSize(32, 28);
            removeBtn.setOnAction(e -> onRemove.accept(this));

            getChildren().addAll(keyField, valueField, removeBtn);
        }

        String getKey() { return keyField.getText(); }
        String getValue() { return valueField.getText(); }
        void setKey(String k) { keyField.setText(k); }
        void setValue(String v) { valueField.setText(v); }
    }
}
