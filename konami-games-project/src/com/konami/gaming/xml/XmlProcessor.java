package com.konami.gaming.xml;

import com.konami.gaming.server.GUIServer;

import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Modern XML processor with improved error handling and resource management.
 * This class processes XML messages and extracts relevant data for display.
 */
public class XmlProcessor {
    
    private static final Logger LOGGER = Logger.getLogger(XmlProcessor.class.getName());
    
    private final String xmlData;
    private final GUIServer guiServer;
    private String command = "Unknown";
    private final List<String> names = new ArrayList<>();
    private final List<String> addresses = new ArrayList<>();
    
    /**
     * Constructor for XmlProcessor
     * @param xmlData The XML data to process
     * @param guiServer Reference to the GUI server
     */
    public XmlProcessor(String xmlData, GUIServer guiServer) {
        this.xmlData = xmlData;
        this.guiServer = guiServer;
    }
    
    /**
     * Process the XML data and extract information
     */
    public void process() {
        if (xmlData == null || xmlData.trim().isEmpty()) {
            LOGGER.warning("Empty or null XML data provided");
            return;
        }
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            // Security settings for XML processing
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            try (StringReader stringReader = new StringReader(xmlData)) {
                InputSource inputSource = new InputSource(stringReader);
                Document document = builder.parse(inputSource);
                
                // Normalize the document
                document.getDocumentElement().normalize();
                
                // Extract command
                extractCommand(document);
                
                // Extract data rows
                extractDataRows(document);
                
                LOGGER.info("XML processing completed successfully. Command: " + command + 
                           ", Names: " + names.size() + ", Addresses: " + addresses.size());
                
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing XML data", e);
            command = "Error";
        }
    }
    
    /**
     * Extract command from XML document
     * @param document The parsed XML document
     */
    private void extractCommand(Document document) {
        try {
            NodeList messageNodes = document.getElementsByTagName("Message");
            if (messageNodes.getLength() > 0) {
                Element messageElement = (Element) messageNodes.item(0);
                NodeList commandNodes = messageElement.getElementsByTagName("Command");
                
                if (commandNodes.getLength() > 0) {
                    Element commandElement = (Element) commandNodes.item(0);
                    command = getTextContent(commandElement);
                    LOGGER.info("Extracted command: " + command);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error extracting command from XML", e);
        }
    }
    
    /**
     * Extract data rows from XML document
     * @param document The parsed XML document
     */
    private void extractDataRows(Document document) {
        try {
            NodeList rowNodes = document.getElementsByTagName("Row");
            LOGGER.info("Found " + rowNodes.getLength() + " row(s) in XML");
            
            for (int i = 0; i < rowNodes.getLength(); i++) {
                Element rowElement = (Element) rowNodes.item(i);
                processDataRow(rowElement);
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error extracting data rows from XML", e);
        }
    }
    
    /**
     * Process individual data row
     * @param rowElement The row element to process
     */
    private void processDataRow(Element rowElement) {
        try {
            // Get description
            NodeList descriptionNodes = rowElement.getElementsByTagName("Description");
            NodeList valueNodes = rowElement.getElementsByTagName("Value");
            
            if (descriptionNodes.getLength() > 0 && valueNodes.getLength() > 0) {
                String description = getTextContent((Element) descriptionNodes.item(0));
                String value = getTextContent((Element) valueNodes.item(0));
                
                // Remove quotes from description and value
                description = cleanText(description);
                value = cleanText(value);
                
                // Categorize data based on description
                if ("Name".equalsIgnoreCase(description)) {
                    names.add(value);
                    LOGGER.fine("Added name: " + value);
                } else if ("Address".equalsIgnoreCase(description)) {
                    addresses.add(value);
                    LOGGER.fine("Added address: " + value);
                } else {
                    LOGGER.info("Unknown description type: " + description + " with value: " + value);
                }
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error processing data row", e);
        }
    }
    
    /**
     * Extract text content from element
     * @param element The element to extract text from
     * @return The text content, or "Unknown" if extraction fails
     */
    private String getTextContent(Element element) {
        if (element == null) {
            return "Unknown";
        }
        
        Node child = element.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData characterData = (CharacterData) child;
            return characterData.getData();
        }
        
        return element.getTextContent();
    }
    
    /**
     * Clean text by removing quotes and trimming whitespace
     * @param text The text to clean
     * @return The cleaned text
     */
    private String cleanText(String text) {
        if (text == null) {
            return "";
        }
        
        return text.trim().replaceAll("^\"|\"$", "");
    }
    
    // Getters
    public String getCommand() {
        return command;
    }
    
    public String[] getNames() {
        return names.toArray(new String[0]);
    }
    
    public String[] getAddresses() {
        return addresses.toArray(new String[0]);
    }
}