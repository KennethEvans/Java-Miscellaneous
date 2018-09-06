package misc;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class TestCopyImage
{
//  private static String DEFAULT_DIRECTORY = "";
  private static String DEFAULT_DIRECTORY = "c:/Scratch/";
  private static String DEFAULT_FILE_NAME = "DSC_5569.jpg";
//  private static String DEFAULT_FILE_NAME = "DSC_5569a.jpg";
//  private static String DEFAULT_FILE_NAME = "DSC_5569b.jpg";
//  private static String DEFAULT_FILE_NAME = "AirCompressor.jpg";
  public static final String LS = System.getProperty("line.separator");
  public static final SimpleDateFormat defaultFormatter =
    new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SSS");
  private static Date prev = new Date();
  private static final int type = BufferedImage.TYPE_INT_RGB;

  /**
   * TestCopyImage constructor
   * @param fileName
   */
  TestCopyImage(String fileName) {
    File file = null;
    try {
      file = new File(fileName);
      if(file == null) {
        System.err.println("Invalid file: " + fileName);
        return;
      }
      System.out.println("Reading: " + fileName);
      BufferedImage image1 = ImageIO.read(file);
      System.out.println("Image read: " + timeStamp());
      System.out.println("  " + image1.getWidth() + " x " + image1.getHeight());
      BufferedImage image2 = copyImage(image1, type);
      System.out.println("Image copied once: " + timeStamp());
      BufferedImage image3 = copyImage(image2, type);
      System.out.println("Copied image copied: " + timeStamp());
      if(true) {
        System.out.println("Info for Image 1:");
        System.out.println(getInfo(image1));
        System.out.println("Info for Image 2:");
        System.out.println(getInfo(image2));
        System.out.println("Info for Image 3:");
        System.out.println(getInfo(image3));
      }
    } catch(Exception ex) {
      System.err.println("Error processing file:" + LS + file.getName() + LS
        + ex + LS + ex.getMessage());
    }
  }
  
  /**
   * Makes a copy of an image with a specified image type.
   * 
   * @param image Source image.
   * @param type Typically BufferedImage.TYPE_INT_ARGB, etc. Use image.getType()
   *          to use the same type as the source.
   * @return The new image.
   */
  public static BufferedImage copyImage(BufferedImage image, int type) {
    // Is this the best way to copy an image
    // Note that TYPE_INT_ARGB supports transparency, RGB does not
    BufferedImage newImage = new BufferedImage(image.getWidth(), image
      .getHeight(), type);
    // Use "null" as the ImageObserver since no asynchronism is involved
    // For drawing the first time, using "this" is appropriate (Component
    // implements ImageObserver)
    Graphics2D gc = newImage.createGraphics();
    // Do this to insure transparency propagates
    gc.setComposite(AlphaComposite.Src);
    gc.drawImage(image, 0, 0, null);
    gc.dispose();
    return newImage;
  }
  
  /**
   * Generates a timestamp
   * @return String timestamp with the current time
   */
  public static String timeStamp() {
    Date cur = new Date();
    long elapsed = cur.getTime() - prev.getTime();
    prev = cur;
    return defaultFormatter.format(cur) + " [" + (elapsed / 1000) + " sec]";
  }

  public String getInfo(BufferedImage image) {
    String info = "";
    if(image == null) {
      info += "No image";
      return info;
    }
    info += image.getWidth() + " x " + image.getHeight() + LS;
    Map<String, String> types = new HashMap<String, String>();
    types.put("5", "TYPE_3BYTE_BGR");
    types.put("6", "TYPE_4BYTE_ABGR");
    types.put("7", "TYPE_4BYTE_ABGR_PRE");
    types.put("12", "TYPE_BYTE_BINARY");
    types.put("10", "TYPE_BYTE_GRAY");
    types.put("13", "TYPE_BYTE_INDEXED");
    types.put("0", "TYPE_CUSTOM");
    types.put("2", "TYPE_INT_ARGB");
    types.put("3", "TYPE_INT_ARGB_PRE");
    types.put("4", "TYPE_INT_BGR");
    types.put("1", "TYPE_INT_RGB");
    types.put("9", "TYPE_USHORT_555_RGB");
    types.put("8", "TYPE_USHORT_565_RGB");
    types.put("11", "TYPE_USHORT_GRAY");
    Integer type = Integer.valueOf(image.getType());
    String stringType = types.get(type.toString());
    if(stringType == null) stringType = "Unknown";
    info += "Type: " + stringType + " [" + type + "]" + LS;
    info += "Properties:" + LS;
    String[] props = image.getPropertyNames();
    if(props == null) {
      info += "  No properties found" + LS;
    } else {
      for(int i = 0; i < props.length; i++) {
        info += "  " + props[i] + ": " + image.getProperty(props[i]) + LS;
      }
    }
    info += "ColorModel:" + LS;
    // The following assumes a particular format for toString()
    String colorModel = image.getColorModel().toString();
    String[] tokens = colorModel.split(" ");
    String colorModelName = tokens[0];
    info += "  " + colorModelName + LS;
    info += "  ";
    for(int i = 1; i < tokens.length; i++) {
      String token = tokens[i];
      if(token.equals("=")) {
        i++;
        info += "= " + tokens[i] + LS + "  ";
      } else {
        info += token + " ";
      }
    }
    return info;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("Starting: " + timeStamp());
    new TestCopyImage(DEFAULT_DIRECTORY + DEFAULT_FILE_NAME);
    System.out.println("All done: " + timeStamp());
  }

}
