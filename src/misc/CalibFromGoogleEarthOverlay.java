package misc;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/*
 * Created on May 13, 2016
 * By Kenneth Evans, Jr.
 */

public class CalibFromGoogleEarthOverlay
{
    public static boolean WARNING = true;

    private static final String LS = System.getProperty("line.separator");
    private static final String DIR = "C:/Users/evans/Documents/Biking";
    private static final String BASE_NAME = "Highland Bike Trail Map";
    // private static final String BASE_NAME = "Proud Lake Trails Map";
//    private static final String BASE_NAME = "Proud Lake Hiking-Biking-Bridle Trails Map";
    private static final String zipName = DIR + "/" + BASE_NAME + ".kmz";
//    private static final String calibName = DIR + "/" + BASE_NAME + ".calib";
    private static final String calibName = DIR + "/aaa" + BASE_NAME + ".calib";
    private static final String TEMP_IMAGE_NAME_NO_EXTENSION = DIR + "/"
        + "aaaaTemp.";

    /**
     * @param zipFileName
     */
    private static Data unzip(String zipFileName) {
        Data data = new Data();
        // Buffer for reading and writing data to file
        byte[] buffer = new byte[2048];

        try {
            FileInputStream finput = new FileInputStream(zipFileName);
            ZipInputStream zinput = new ZipInputStream(finput);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipEntry entry = null;

            while((entry = zinput.getNextEntry()) != null) {
                String entryName = entry.getName();
                System.out.println("    " + entryName);
                if(entryName.equals("doc.kml")) {
                    try {
                        int len = 0;
                        while((len = zinput.read(buffer)) > 0) {
                            baos.write(buffer, 0, len);
                        }
                        data.setBox(new LatLonBox(baos.toString()));
                    } catch(Exception ex1) {
                        ex1.printStackTrace();
                    } finally {
                        if(zinput != null) zinput.closeEntry();
                    }
                } else {
                    // Assume any other file is the image
                    try {
                        // Read bytes to make a BufferedImage via ImageIO.read
                        // int len = 0;
                        // int bytesRead = 0;
                        // while((len = zinput.read(buffer)) > 0) {
                        // bytesRead += len;
                        // baos.write(buffer, 0, len);
                        // }
                        // byte[] bufferBytes = baos.toByteArray();
                        // byte[] imageBytes = new byte[bytesRead];
                        // // bufferBytes is the current size of the stream
                        // // Needs to be truncated
                        // for(int i = 0; i < bytesRead; i++) {
                        // imageBytes[i] = bufferBytes[i];
                        // }
                        // System.out.println("imageBytes: bytesRead=" +
                        // bytesRead
                        // + " bufferBytes.length=" + bufferBytes.length);
                        // String[] suffices = ImageIO.getReaderFileSuffixes();
                        // for(String suffix : suffices) {
                        // System.out.println(suffix);
                        // }
                        // InputStream in = new
                        // ByteArrayInputStream(imageBytes);

                        // ImageIo.read fails when reading bytes
                        // Save the file and read it using ImageIO.read
                        // (It may be using the suffix to get the decoder)
                        // Get the extension
                        File tempFile = new File(entryName);
                        String ext = Utils.getExtension(tempFile);
                        // Get the supported extensions
                        String[] suffices = ImageIO.getReaderFileSuffixes();
                        boolean found = false;
                        for(String suffix : suffices) {
                            if(suffix.equals(ext)) {
                                found = true;
                            }
                        }
                        if(!found) {
                            System.out.println(
                                "Not a supported ImageIO extension: " + ext);
                            for(String suffix : suffices) {
                                System.out.println("    " + suffix);
                                if(suffix.equals(ext)) {
                                    found = true;
                                }
                            }
                        } else {
                            System.out.println(
                                ext + " is a supported ImageIO extension");
                            File file = new File(
                                TEMP_IMAGE_NAME_NO_EXTENSION + ext);
                            FileOutputStream fOutput = new FileOutputStream(
                                file);
                            int count = 0;
                            while((count = zinput.read(buffer)) > 0) {
                                // write 'count' bytes to the file output stream
                                fOutput.write(buffer, 0, count);
                            }
                            fOutput.close();
                            BufferedImage image = ImageIO.read(file);
                            if(image != null) {
                                data.setSize(new Point(image.getWidth(),
                                    image.getHeight()));
                            }
                        }
                    } catch(Exception ex1) {
                        ex1.printStackTrace();
                        return null;
                    } finally {
                        if(zinput != null) zinput.closeEntry();
                    }
                }
            }
            zinput.closeEntry();
            if(zinput != null) {
                // Close the last ZipEntry
                zinput.closeEntry();
                zinput.close();
            }
            if(finput != null) finput.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    private static class Data
    {
        private Point size;
        private LatLonBox box;

        /**
         * @return The value of size.
         */
        public Point getSize() {
            return size;
        }

        /**
         * @param size The new value for size.
         */
        public void setSize(Point size) {
            this.size = size;
        }

        /**
         * @return The value of box.
         */
        public LatLonBox getBox() {
            return box;
        }

        /**
         * @param box The new value for box.
         */
        public void setBox(LatLonBox box) {
            this.box = box;
        }

    }

    private static class LatLonBox
    {
        private double north;
        private double south;
        private double east;
        private double west;
        private double rotation;

        public LatLonBox(String kmlString) {
            if(kmlString == null) {
                return;
            }
            // Parse to get the values
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(kmlString));
                Document doc = dBuilder.parse(is);
                doc.getDocumentElement().normalize();
                String string;
                NodeList nList;
                nList = doc.getElementsByTagName("north");
                if(nList.getLength() == 0) {
                    System.out.println("No north element found");
                } else {
                    string = nList.item(0).getTextContent();
                    north = Double.parseDouble(string);
                }
                nList = doc.getElementsByTagName("south");
                if(nList.getLength() == 0) {
                    System.out.println("No south element found");
                } else {
                    string = nList.item(0).getTextContent();
                    south = Double.parseDouble(string);
                }
                nList = doc.getElementsByTagName("east");
                if(nList.getLength() == 0) {
                    System.out.println("No east element found");
                } else {
                    string = nList.item(0).getTextContent();
                    east = Double.parseDouble(string);
                }
                nList = doc.getElementsByTagName("west");
                if(nList.getLength() == 0) {
                    System.out.println("No west element found");
                } else {
                    string = nList.item(0).getTextContent();
                    west = Double.parseDouble(string);
                }
                nList = doc.getElementsByTagName("rotation");
                if(nList.getLength() == 0) {
                    System.out.println("No rotation element found");
                } else {
                    string = nList.item(0).getTextContent();
                    rotation = Double.parseDouble(string);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return String.format(
                "north=%.6f south=%.6f east=%.6f west=%.6f rotation=%.6f",
                north, south, east, west, rotation);
        }

        public void createCalibFile(String fileName, Point size) {
            File file = new File(fileName);
            if(file.exists()) {
                int selection = JOptionPane.showConfirmDialog(null,
                    "File already exists:" + LS + fileName + "\nOK to replace?",
                    "Warning", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if(selection != JOptionPane.OK_OPTION) return;
            }
            double x1, y1;
            double x = .5 * (east - west);
            double y = .5 * (north - south);
            double xcen = .5 * (east + west);
            double ycen = .5 * (north + south);
            double theta = Math.toRadians(rotation);
            // DEBUG
            System.out.printf("N=%10.6f S=%10.6f E=%10.6f W=%10.6f " + LS,
                north, south, east, west);
            System.out.printf("xcen==%10.6f ycen=%10.6f x=%10.6f y=%10.6f " + LS,
                xcen, ycen, x, y);
            PrintWriter out = null;
            try {
                out = new PrintWriter(new FileWriter(file));
                String format = "%5d %5d %12.6f %12.6f %s" + LS;
                // NW
                x1 = -x * Math.cos(theta) - y * Math.sin(theta) + xcen;
                y1 = -x * Math.sin(theta) + y * Math.cos(theta) + ycen;
                out.printf(format, 0, 0, x1, y1, "NW");
                System.out.printf(format, 0, 0, x1, y1, "NW");
                // NE
                x1 = x * Math.cos(theta) - y * Math.sin(theta) + xcen;
                y1 = x * Math.sin(theta) + y * Math.cos(theta) + ycen;
                out.printf(format, size.x - 1, 0, x1, y1, "NE");
                System.out.printf(format, size.x - 1, 0, x1, y1, "NE");
                // SE
                x1 = x * Math.cos(theta) + y * Math.sin(theta) + xcen;
                y1 = x * Math.sin(theta) - y * Math.cos(theta) + ycen;
                out.printf(format, size.x - 1, size.y - 1, x1, y1, "SE");
                System.out.printf(format, size.x - 1, size.y - 1, x1, y1, "SE");
                // SW
                x1 = -x * Math.cos(theta) + y * Math.sin(theta) + xcen;
                y1 = -x * Math.sin(theta) - y * Math.cos(theta) + ycen;
                out.printf(format, 0, size.y - 1, x1, y1, "SW");
                System.out.printf(format, 0, size.y - 1, x1, y1, "SW");
                System.out.println("Wrote " + calibName);
            } catch(Exception ex) {
                ex.printStackTrace();
                return;
            } finally {
                if(out != null) {
                    out.close();
                }
            }
        }
    }

    public static void main(String[] args) {
        if(WARNING) {
            Utils.warnMsg("There is a problem with this calculation." + LS
                + "It is not accurate." + LS + LS
                + "Aborting until it is resolved...");
            return;
        }
        String zipFileName = zipName;
        System.out.println("Reading " + zipFileName);
        Data data = unzip(zipFileName);
        if(data == null) {
            System.out.println("Result of unzip is null");
        } else {
            if(data.getBox() == null) {
                System.out.println("Could not read LatLonBox");
            } else {
                System.out.println(data.getBox());
            }
            if(data.getSize() == null) {
                System.out.println("Could not read image file");
            } else {
                System.out.println("width=" + data.getSize().x + " height="
                    + data.getSize().y);
            }
        }
        if(data != null && data.getBox() != null && data.getSize() != null) {
            System.out.println();
            data.getBox().createCalibFile(calibName, data.getSize());
        }

        System.out.println();
        System.out.println("All Done");
    }

}
