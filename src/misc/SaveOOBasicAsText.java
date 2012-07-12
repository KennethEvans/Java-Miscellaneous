package misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class backs up an OO extension by converting the .xba XML files to .bas
 * text files and copying any others. The source and destination directories are
 * specified on the command line, but if not the hard-coded defaults are used.
 * The destination files are silently overwritten.<br>
 * <br>
 * All of the methods are static.
 */
public class SaveOOBasicAsText
{
    /** The extension directory where the .xba, .xdl, and .xlb files are. */
    private static final String INPUT_DIR = "C:/Users/evans/AppData/Roaming/"
        + "OpenOffice.org/3/user/uno_packages/cache/uno_packages/" +
        // This may have to be changed
        "A544.tmp_/NCSetup7-1.1.0.oxt/NCSetup7/";
    /**
     * The directory where the converted .xlb file and copies of the other files
     * will be.
     */
    private static final String OUTPUT_DIR = "C:/Users/evans/Documents/Nikon/Spreadsheet 1/Code/";
    /** The input directory to use. */
    private static File inputDir = new File(INPUT_DIR);
    /** The output directory to use. */
    private static File outputDir = new File(OUTPUT_DIR);

    /** A specific XML file to test. */
    private static final String TEST_FILE = "C:/Users/evans/AppData/Roaming/OpenOffice.org/3/user/uno_packages/cache/uno_packages/A544.tmp_/NCSetup7-1.1.0.oxt/NCSetup7/ASetRetr.xba";
    /** A file representing a book. The file is in the project directory. */
    private static final String BOOK_FILE = "book.xml";

    public static void backupExtension() {
        File inputFiles[] = inputDir.listFiles();
        System.out.println("Backing up from:");
        System.out.println(" " + inputDir);
        System.out.println("To:");
        System.out.println(" " + outputDir);
        System.out.println();

        // Create the directory if it doesn't exist
        if(!outputDir.exists()) {
            System.out.println("Creating: ");
            System.out.println(" " + outputDir);
            System.out.println();
            outputDir.mkdir();
        }

        // Prompt that files will be overwritten
        if(outputDir.listFiles().length > 0) {
            System.out.println("Existing files will be silently overwritten!");
            System.out.print("Continue [Y/n]? ");
            try {
                InputStreamReader stdinStreamReader = new InputStreamReader(
                    System.in);
                BufferedReader stdinReader = new BufferedReader(
                    stdinStreamReader);
                String input = stdinReader.readLine();
                input = input.toLowerCase();
                System.out.println();
                if(input.length() > 0 && !input.startsWith("y")) {
                    System.out.println("Aborted");
                    return;
                }
            } catch(IOException ex) {
                System.out.println("Error prompting for confirmation"
                    + " to overwrite files");
                System.out.println();
                System.out.println("Aborted");
                return;
            }
        }

        String content;
        byte[] bytes = null;
        File dst = null;
        OutputStream out = null;
        for(File file : inputFiles) {
            if(getExtension(file).equals("xba")) {
                content = getContent(file);
                if(content == null) {
                    System.out.println("Unable to get content for:");
                    System.out.println("  " + file.getPath());
                    continue;
                } else {
                    try {
                        // Make a new file with the content
                        dst = new File(OUTPUT_DIR
                            + file.getName().replace(".xba", ".bas"));
                        out = new FileOutputStream(dst);
                        System.out.println("Converting " + file.getName()
                            + " to " + dst.getName());
                        bytes = content.getBytes();
                        out.write(bytes);
                    } catch(IOException ex) {
                        System.out.println("Unable to create:");
                        System.out.println("  " + file.getPath());
                        if(dst == null) {
                            System.out.println("Destination is null");
                        } else {
                            System.out.println("To:");
                            System.out.println("  " + dst.getPath());
                        }
                        continue;
                    } finally {
                        try {
                            if(out != null) {
                                out.close();
                            }
                        } catch(IOException ex) {
                            // do nothing
                        }
                    }
                }
            } else {
                // Just copy the file
                try {
                    dst = new File(OUTPUT_DIR + file.getName());
                    System.out.println("Copying " + file.getName());
                    copy(file, dst);
                } catch(IOException ex) {
                    System.out.println("Unable to copy:");
                    System.out.println("  " + file.getPath());
                    if(dst == null) {
                        System.out.println("Destination is null");
                    } else {
                        System.out.println("To:");
                        System.out.println("  " + dst.getPath());
                    }
                    continue;
                }
            }
        }
    }

    /**
     * Gets the text from the root node of the given File. This should be the
     * Basic source file.
     * 
     * @param file
     * @return
     */
    public static String getContent(File file) {
        String content = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            // Fix it to ignore the module.dtd in the !DOCTYPE
            docBuilder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId,
                    String systemId) throws SAXException, IOException {
                    if(systemId.contains("module.dtd")) {
                        return new InputSource(new StringReader(""));
                    } else {
                        return null;
                    }
                }
            });
            Document doc = docBuilder.parse(file);
            Node firstNode = doc.getDocumentElement().getFirstChild();
            content = firstNode.getTextContent();
        } catch(SAXParseException ex) {
            System.out.println("Error parsing line " + ex.getLineNumber()
                + ", uri " + ex.getSystemId());
            ex.printStackTrace();
        } catch(SAXException ex) {
            System.out.println("Got SAX Exception");
            ex.printStackTrace();
            // Get embedded exception
            System.out.println();
            System.out.println("With embedded Exception");
            Exception exx = ex.getException();
            if(exx == null) {
                System.out.println("null");
            } else {
                exx.printStackTrace();
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
        return content;
    }

    /**
     * Gets the text from the root node of the given File and prints it out.
     * This should be the Basic source file.
     * 
     * @param file
     * @return
     */
    public static void getTestContent(File file) {
        System.out.println(file.getPath());
        System.out.println();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            // Fix it to ignore the module.dtd in the !DOCTYPE
            docBuilder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId,
                    String systemId) throws SAXException, IOException {
                    if(systemId.contains("module.dtd")) {
                        return new InputSource(new StringReader(""));
                    } else {
                        return null;
                    }
                }
            });
            Document doc = docBuilder.parse(file);

            // normalize text representation doc.getDocumentElement ().normalize
            // ();
            System.out.println("Root element of the doc is "
                + doc.getDocumentElement().getNodeName());

            Node firstNode = doc.getDocumentElement().getFirstChild();
            String content = firstNode.getTextContent();
            System.out.println(content);
        } catch(SAXParseException ex) {
            System.out.println("Error parsing line " + ex.getLineNumber()
                + ", uri " + ex.getSystemId());
            ex.printStackTrace();
        } catch(SAXException ex) {
            System.out.println("Got SAX Exception");
            ex.printStackTrace();
            // Get embedded exception
            System.out.println();
            System.out.println("With embedded Exception");
            Exception exx = ex.getException();
            if(exx == null) {
                System.out.println("null");
            } else {
                exx.printStackTrace();
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Parses a file representing a book. Is an example of simple XML parsing.
     * 
     * @param file
     */
    public static void getBookContent(File file) {
        System.out.println(file.getPath());
        System.out.println(file.getAbsolutePath());
        System.out.println();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            // normalize text representation doc.getDocumentElement ().normalize
            // ();
            System.out.println("Root element of the doc is "
                + doc.getDocumentElement().getNodeName());

            NodeList listOfPersons = doc.getElementsByTagName("person");
            int totalPersons = listOfPersons.getLength();
            System.out.println("Total no of people : " + totalPersons);

            for(int s = 0; s < listOfPersons.getLength(); s++) {
                Node firstPersonNode = listOfPersons.item(s);
                if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element firstPersonElement = (Element)firstPersonNode;

                    NodeList firstNameList = firstPersonElement
                        .getElementsByTagName("first");
                    Element firstNameElement = (Element)firstNameList.item(0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    System.out.println("First Name : "
                        + ((Node)textFNList.item(0)).getNodeValue().trim());

                    NodeList lastNameList = firstPersonElement
                        .getElementsByTagName("last");
                    Element lastNameElement = (Element)lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    System.out.println("Last Name : "
                        + ((Node)textLNList.item(0)).getNodeValue().trim());

                    NodeList ageList = firstPersonElement
                        .getElementsByTagName("age");
                    Element ageElement = (Element)ageList.item(0);

                    NodeList textAgeList = ageElement.getChildNodes();
                    System.out.println("Age : "
                        + ((Node)textAgeList.item(0)).getNodeValue().trim());

                    // ------
                }
            }
        } catch(SAXParseException err) {
            System.out.println("** Parsing error" + ", line "
                + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch(SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Get the extension of a file.
     * 
     * @param file
     * @return
     */
    public static String getExtension(File file) {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');
        if(i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Copies the file src to the file dest.
     * 
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copy(File src, File dest) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Parses the arguments on the command line.
     * 
     * @param args
     * @return
     */
    protected static boolean parseCommand(String[] args) {
        int i;

        int nDirectoriesSpecified = 0;
        for(i = 0; i < args.length; i++) {
            if(args[i].startsWith("-")) {
                switch(args[i].charAt(1)) {
                case 'h':
                    usage();
                    System.exit(0);
                default:
                    System.err.println("\n\nInvalid option: " + args[i]);
                    usage();
                    return false;
                }
            } else {
                if(nDirectoriesSpecified == 0) {
                    inputDir = new File(args[i]);
                    nDirectoriesSpecified++;
                } else if(nDirectoriesSpecified == 1) {
                    outputDir = new File(args[i]);
                    nDirectoriesSpecified++;
                } else {
                    System.err.println("\n\nToo many directories specified: "
                        + args[i]);
                    usage();
                    return false;
                }
            }
        }
        if(nDirectoriesSpecified == 0) {
            System.err.println("Warning: Hard-coded directories will be used");
            System.err.println();
        }
        return true;
    }

    /**
     * Prints usage
     */
    protected static void usage() {
        System.out
            .println("\nUsage: java "
                + SaveOOBasicAsText.class.getName()
                + " [Options] [input-directory output-directory]\n"
                + "  input-directory is typically in:\n"
                + "     AppData/Roaming/OpenOffice.org/3/user/uno_packages/cache/uno_packages/xxxx.tmp_/\n"
                + "  output-directory is your choice.\n"
                + "  Directory names should end in /.\n"
                + "  Either / or \\ may be used as the path separator\n"
                + "  If directories are not specified, hard-coded defaults are used.\n"
                + "  Options:\n" + "    -h        Help (This message)\n" + "");
    }

    /**
     * Main method.
     * 
     * @param argv
     */
    public static void main(String argv[]) {
        if(!parseCommand(argv)) {
            System.exit(1);
        }
        File file;
        // Mode 0 is the real implementation. The others are tests.
        int mode = 0;
        switch(mode) {
        case 0:
            backupExtension();
            break;
        case 1:
            // Test read a book XML file
            file = new File(BOOK_FILE);
            getBookContent(file);
            break;
        case 2:
            // Test reading a specific .xba file
            file = new File(TEST_FILE);
            getTestContent(file);
            break;
        default:
            System.out.println("invalid mode: " + mode);
            break;
        }

        System.out.println();
        System.out.println("All done");
    }
}
