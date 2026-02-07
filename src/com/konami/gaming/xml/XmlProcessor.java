package com.konami.gaming.xml;

import org.xml.sax.InputSource;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Parses XML messages and extracts command + generic key-value pairs.
 */
public class XmlProcessor {

    private static final Logger LOGGER = Logger.getLogger(XmlProcessor.class.getName());

    private final String xmlData;
    private String command = "";
    private final List<String[]> keyValuePairs = new ArrayList<>();

    public XmlProcessor(String xmlData) {
        this.xmlData = xmlData;
    }

    public void process() {
        if (xmlData == null || xmlData.trim().isEmpty()) {
            LOGGER.warning("Empty or null XML data provided");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            DocumentBuilder builder = factory.newDocumentBuilder();

            try (StringReader stringReader = new StringReader(xmlData)) {
                InputSource inputSource = new InputSource(stringReader);
                Document document = builder.parse(inputSource);
                document.getDocumentElement().normalize();

                extractCommand(document);
                extractDataRows(document);

                LOGGER.info("XML processed: command=" + command + ", pairs=" + keyValuePairs.size());
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing XML", e);
            command = "Error";
        }
    }

    private void extractCommand(Document document) {
        try {
            NodeList messageNodes = document.getElementsByTagName("Message");
            if (messageNodes.getLength() > 0) {
                Element messageElement = (Element) messageNodes.item(0);
                NodeList commandNodes = messageElement.getElementsByTagName("Command");
                if (commandNodes.getLength() > 0) {
                    command = getTextContent((Element) commandNodes.item(0));
                    command = cleanText(command);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error extracting command", e);
        }
    }

    private void extractDataRows(Document document) {
        try {
            NodeList rowNodes = document.getElementsByTagName("Row");
            for (int i = 0; i < rowNodes.getLength(); i++) {
                Element rowElement = (Element) rowNodes.item(i);
                NodeList descNodes = rowElement.getElementsByTagName("Description");
                NodeList valNodes = rowElement.getElementsByTagName("Value");
                if (descNodes.getLength() > 0 && valNodes.getLength() > 0) {
                    String key = cleanText(getTextContent((Element) descNodes.item(0)));
                    String val = cleanText(getTextContent((Element) valNodes.item(0)));
                    keyValuePairs.add(new String[]{key, val});
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error extracting rows", e);
        }
    }

    private String getTextContent(Element element) {
        if (element == null) return "";
        Node child = element.getFirstChild();
        if (child instanceof CharacterData) {
            return ((CharacterData) child).getData();
        }
        return element.getTextContent();
    }

    private String cleanText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("^\"|\"$", "");
    }

    public String getCommand() {
        return command;
    }

    public List<String[]> getKeyValuePairs() {
        return keyValuePairs;
    }
}
