import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;

public class ParseXMLString {

  public static void main(String[] args) {
    String xmlRecords =
      "<?xml version='1.0' encoding='UTF-8'?>" +
      "<Message>" +
        "<Command>Print</Command>" +
        "<Data>" +
          "<Row>" +
            "<Description>Name</Description>" +
            "<Value>Mr. Joe Chase</Value>" +
          "</Row>" +
          "<Row>" +
            "<Description>Address</Description>" +
            "<Value>123 Anywhere Lane</Value>" +
          "</Row>" +
        "</Data>" +
      "</Message>";

    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      InputSource is = new InputSource(new StringReader(xmlRecords));
      Document doc = db.parse(is);

      // Get command
      String clientCommand = getTagValue(doc, "Command");
      System.out.println(clientCommand);

      // Process rows
      NodeList rows = doc.getElementsByTagName("Row");
      for (int i = 0; i < rows.getLength(); i++) {
        Element row = (Element) rows.item(i);
        String description = getTagValue(row, "Description");
        String value = getTagValue(row, "Value");
        System.out.println(value);
      }
    } catch (Exception e) {
      System.out.println("issue");
    }
  }

  private static String getTagValue(Document doc, String tag) {
    NodeList nodes = doc.getElementsByTagName(tag);
    if (nodes.getLength() > 0) {
      return getCharacterDataFromElement((Element) nodes.item(0));
    }
    return "";
  }

  private static String getTagValue(Element element, String tag) {
    NodeList nodes = element.getElementsByTagName(tag);
    if (nodes.getLength() > 0) {
      return getCharacterDataFromElement((Element) nodes.item(0));
    }
    return "";
  }

  private static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
      CharacterData cd = (CharacterData) child;
      return cd.getData().replace("\"", "").trim();
    }
    return "";
  }
}
