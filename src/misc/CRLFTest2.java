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
public class CRLFTest2
{
  private final static String fileName = "c:\\Scratch\\CRLFTest.txt";

  /**
   * @param args
   */
  public static void main(String[] args) {
    // Write to a file
    try {
      PrintWriter out = new PrintWriter(new FileWriter(fileName));
      out.printf("Line 1\nLine 2\n");
      out.close();
    } catch(Exception ex) {
      System.err.println("Error writing " + fileName);
    }
  }

}
