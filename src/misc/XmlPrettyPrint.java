package misc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*
 * Created on Jan 22, 2019
 * By Kenneth Evans, Jr.
 */

public class XmlPrettyPrint
{
    // private static final String TEST_FILE =
    // "C:/Users/evans/Documents/GPSLink/Polar/Kenneth_Evans_2018-08-10_09-02-44.tcx";
    // private static final String TEST_FILE =
    // "C:/Users/evans/Documents/GPSLink/Polar/Kenneth_Evans_2018-08-10_09-02-44.gpx";
    // private static final String TEST_FILE =
    // "C:/Users/evans/Documents/GPSLink/STL/track2018-10-24-Walking-Kensington-2513579.gpx";
    private static final String TEST_FILE = "C:/Users/evans/Documents/GPSLink/FitnessHistoryDetail.tcx";
    private static final int INDENT = 2;

    public static String toPrettyString(String xml, int indent) {
        try {
            // Turn xml string into a document
            Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new InputSource(
                    new ByteArrayInputStream(xml.getBytes("utf-8"))));

            // Remove whitespaces outside tags
            document.normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList)xPath.evaluate(
                "//text()[normalize-space()='']", document,
                XPathConstants.NODESET);

            for(int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                node.getParentNode().removeChild(node);
            }

            // Setup pretty print options
            TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Return pretty print xml string
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document),
                new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
            // "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch(TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder
                .parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String fileName;
        if(args.length > 0) {
            fileName = args[0];
        } else {
            fileName = TEST_FILE;
        }
        System.out.println(fileName);
        System.out.println();
        String xml;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(new File(fileName)));
            xml = convertDocumentToString(doc);
            System.out.println(toPrettyString(xml, INDENT));
        } catch(IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch(ParserConfigurationException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch(SAXException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
