package test.Konami;

import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;

import java.io.*;

public class XmlReceived {

	private String xmlName = "Null";
	private String xmlAddress = "Null";
	private String xmlCommand = "Null";
	private final String xml;
	private final GUIServer gui;

	public XmlReceived(String xml, GUIServer gui) {
		this.xml = xml;
		this.gui = gui;
	}

	// Converts the input element type to a string type
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			return ((CharacterData) child).getData();
		}
		return "Please check the XML.";
	}

	// Main method to process XML data
	public void process() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);

			NodeList nodesMessage = doc.getElementsByTagName("Message");
			if (nodesMessage.getLength() > 0) {
				Element elementMessage = (Element) nodesMessage.item(0);
				NodeList myCommands = elementMessage.getElementsByTagName("Command");
				if (myCommands.getLength() > 0) {
					Element commandStr = (Element) myCommands.item(0);
					String clientCommand = getCharacterDataFromElement(commandStr);
					System.out.println("Server please process this command " + clientCommand);
					this.xmlCommand = clientCommand;
				}
			}

			NodeList nodes = doc.getElementsByTagName("Row");
			for (int x = 0; x < nodes.getLength(); x++) {
				Element element = (Element) nodes.item(x);

				NodeList nameList = element.getElementsByTagName("Description");
				NodeList addrList = element.getElementsByTagName("Value");

				if (nameList.getLength() > 0 && addrList.getLength() > 0) {
					String conVar = getCharacterDataFromElement((Element) nameList.item(0)).replace("\"", "");
					String value = getCharacterDataFromElement((Element) addrList.item(0)).replace("\"", "");

					if ("Address".equals(conVar)) {
						this.xmlAddress = value;
					} else if ("Name".equals(conVar)) {
						this.xmlName = value;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Issue in the XML processing: " + e.getMessage());
		}
	}

	public String updateNameGrid() {
		return this.xmlName;
	}

	public String updateAddressGrid() {
		return this.xmlAddress;
	}

	public String updateCommandGrid() {
		return this.xmlCommand;
	}
}
