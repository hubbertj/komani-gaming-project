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
        String xml = """
            <?xml version='1.0' encoding='UTF-8'?>
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
            </Message>
            """;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

        printNode(doc.getDocumentElement(), "");
    }

    private static void printNode(Node node, String indent) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            String value = node.getNodeValue().trim();
            if (!value.isEmpty()) {
                System.out.println(indent + value);
            }
            return;
        }

        System.out.print(indent + node.getNodeName());

        NamedNodeMap attrs = node.getAttributes();
        if (attrs != null && attrs.getLength() > 0) {
            System.out.print(" attributes=[");
            for (int i = 0; i < attrs.getLength(); i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(attrs.item(i));
            }
            System.out.print("]");
        }
        System.out.println();

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            printNode(children.item(i), indent + "  ");
        }
    }
}
