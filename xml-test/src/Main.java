import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Main {

    public static void main(String... args) throws IOException, SAXException, ParserConfigurationException {

        String xml = ("<?xml version = '1.0' encoding = 'UTF-8'?>
<Message>
  <Command>Print</Command>
  <Data>
     <Row>
       <Description>"Name"</Description>
       <Value>"Mr. Joe Chase"</Value>
     </Row>
     <Row>
       <Description>"Address"</Description>
       <Value>"123 Anywhere Lane"</Value>
     </Row>
  </Data>
</Message>");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

        print(doc.getDocumentElement(), "");
    }

    private static void print(Node e, String tab) {

        if (e.getNodeType() == Node.TEXT_NODE) {
            System.out.println(tab + e.getNodeValue());
            return;
        }


        System.out.print(tab + e.getNodeName());

        NamedNodeMap as = e.getAttributes();
        if (as != null && as.getLength() > 0) {
            System.out.print(" attributes=[");
            for (int i = 0; i < as.getLength(); i++) 
                System.out.print((i == 0 ? "" : ", ") + as.item(i));
            System.out.print("]");
        }
        System.out.println();

        if (e.getNodeValue() != null)
            System.out.println(tab + " " + e.getNodeValue());

        NodeList childs = e.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++)
            print(childs.item(i), tab + " ");
    }
}
