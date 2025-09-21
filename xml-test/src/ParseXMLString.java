
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;

public class ParseXMLString {

  public static void main(String arg[]) {
     String xmlRecords =
    		 "<?xml version = '1.0' encoding = 'UTF-8'?>"+
     "<Message>"+
       "<Command>Print</Command>"+
       "<Data>"+
          "<Row>"+
            "<Description>\"Name\"</Description>" +
            "<Value>\"Mr. Joe Chase\"</Value>" +
          "</Row>"+
          "<Row>"+
            "<Description>\"Address\"</Description>" +
            "<Value>\"123 Anywhere Lane\"</Value>"+
          "</Row>"+
       "</Data>" +
     "</Message>";

    try {
        DocumentBuilderFactory dbf =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        
        Document doc = db.parse(is);
    
        // removed the command from the XML message.
        NodeList nodesMessage = doc.getElementsByTagName("Message");
        Element elementMessage = (Element) nodesMessage.item(0);
        NodeList myCommands = elementMessage.getElementsByTagName("Command");
        Element commandStr = (Element) myCommands.item(0);
        String clientCommand = getCharacterDataFromElement(commandStr);
        
        System.out.println(clientCommand);
        
        
        
        
        NodeList nodes = doc.getElementsByTagName("Row");
        
        
        
        
        
        // loops thought the XML Message for values.
        for (int x = 0; x < nodes.getLength(); x++ ){
        
        
			        Element element = (Element) nodes.item(x); //what I need to loop
			        
			        NodeList name = element.getElementsByTagName("Description");
			        Element line = (Element) name.item(0); 
			        
			        String conVar = getCharacterDataFromElement(line);
			        conVar = conVar.replace("\"", "");
			        String nameObj = "Name";
			        String addressObj = "Address";
			        
			        

			        
					NodeList addr = element.getElementsByTagName("Value");
			        Element lineaddress = (Element) addr.item(0);
			   
			        	if (conVar.equals(addressObj)){
			                String xmlAddress = getCharacterDataFromElement(lineaddress);
			                xmlAddress = xmlAddress.replace("\"", "");
			                System.out.println(xmlAddress);
			                
			        		
			        	}
			        	if (conVar.equals(nameObj)){
			                String xmlName = getCharacterDataFromElement(lineaddress);
			                xmlName = xmlName.replace("\"", "");
			                System.out.println(xmlName);
			        		
			        	}
               
        	}
        
        
         
        
    }catch(Exception e){
    System.out.println ("issue");
    }
    	
    
    
    
    }

  
  
  
  
  /**
        // iterate the employees
        for (int i = 0; i < nodes.getLength(); i++) {
           Element element = (Element) nodes.item(i);
           
           

           NodeList name = element.getElementsByTagName("name");
           Element line = (Element) name.item(0);
           System.out.println("Name: " + getCharacterDataFromElement(line));

           NodeList title = element.getElementsByTagName("title");
           line = (Element) title.item(0);
           System.out.println("Title: " + getCharacterDataFromElement(line));
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }   
  }
**/
        
  public static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
       CharacterData cd = (CharacterData) child;
       return cd.getData();
    }
    return "Unknown";
  }
}
 
