package misc;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * CRLFTest
 * Tests printing a string with "\n" in it using println().
 * Note that println() uses line.separator, not necessarily "\n".
 * To get around this, use line.separator in strings or use print(), not
 * println(), to write them.
 * @author Kenneth Evans, Jr.
 */
public class CRLFTest
{
  private final static String fileName = "c:\\Scratch\\CRLFTest.txt";

  /**
   * @param args
   */
  public static void main(String[] args) {
    String string = "";
    if(false) {
      // Use line.separator
      String ls = System.getProperty("line.separator");
      if(ls == null) {
        System.err.println("line.separator not found");
        System.exit(1);
      }
      string += "Line 1" + ls;
      string += "Line 2" + ls;
    } else {
      // Use "\n"
      string += "Line 1" + "\n";
      string += "Line 2" + "\n";
    }
    System.out.println(string);
 
    // Write to a file
    try {
      PrintWriter out = new PrintWriter(new FileWriter(fileName));
      if(true) {
        out.println(string);
      } else {
        out.printf(string);
      }
      out.close();
    } catch(Exception ex) {
      System.err.println("Error writing " + fileName);
    }
  }

}
