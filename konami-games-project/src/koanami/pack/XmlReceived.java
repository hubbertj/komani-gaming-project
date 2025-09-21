package koanami.pack;

import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;

public class XmlReceived {

	private String xmlCommand = "Null";
	private final String xml;
	private final GUIServer gui;
	private final String[] myStrings = new String[6];
	private final String[] myAdd = new String[6];

	public XmlReceived(String xml, GUIServer gui) {
		this.xml = xml;
		this.gui = gui;
	}

	// Converts the input element type to a string type
	private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		return (child instanceof CharacterData) ? ((CharacterData) child).getData() : "";
	}

	// Main method to process XML data
	public void process() {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(xml)));

			NodeList nodesMessage = doc.getElementsByTagName("Message");
			if (nodesMessage.getLength() == 0) return;
			Element elementMessage = (Element) nodesMessage.item(0);

			NodeList myCommands = elementMessage.getElementsByTagName("Command");
			if (myCommands.getLength() == 0) return;
			String clientCommand = getCharacterDataFromElement((Element) myCommands.item(0));

			System.out.println("Server please process this command " + clientCommand);
			this.xmlCommand = clientCommand;

			NodeList nodes = doc.getElementsByTagName("Row");
			for (int x = 0; x < nodes.getLength() && x < 6; x++) {
				Element element = (Element) nodes.item(x);

				NodeList nameList = element.getElementsByTagName("Description");
				NodeList addrList = element.getElementsByTagName("Value");
				if (nameList.getLength() == 0 || addrList.getLength() == 0) continue;

				String conVar = getCharacterDataFromElement((Element) nameList.item(0)).replace("\"", "");
				String value = getCharacterDataFromElement((Element) addrList.item(0)).replace("\"", "");

				if ("Address".equals(conVar)) {
					myAdd[x] = value;
				} else if ("Name".equals(conVar)) {
					myStrings[x] = value;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getXmlCommand() {
		return xmlCommand;
	}

	public String[] getMyStrings() {
		return myStrings;
	}

	public String[] getMyAdd() {
		return myAdd;
	}
}
