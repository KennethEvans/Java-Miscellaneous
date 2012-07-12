package misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * FixRGBText converts rgb.txt to Java code for PlotColors.
 * 
 * @author Kenneth Evans, Jr.
 */
public class FixRGBText
{
  private static final String inName = "c:/Scratch/rgb.txt";
  private static final String outName = "c:/Scratch/rgb.java";

  /**
   * @param args
   */
  public static void main(String[] args) {
    fix();
  }

  public static void fix() {
    int lineNum = 0;
    BufferedReader in = null;
    PrintWriter out = null;
    double r, g, b;
    String name = null;
    String first = null;
    try {
      in = new BufferedReader(new FileReader(inName));
      out = new PrintWriter(new FileWriter(outName));
      String line;
      while((line = in.readLine()) != null) {
        lineNum++;
        if(line.length() < 10) continue;
        name = line.substring(13);
        name = name.trim();
        if(name.indexOf(" ") != -1) continue;
        if(name.indexOf("grey") != -1) continue;
        if(name.indexOf("Grey") != -1) continue;
        first = name.substring(0, 1);
        first = first.toUpperCase();
        name = first + name.substring(1);
        
        r = Double.parseDouble(line.substring(0, 3));
        g = Double.parseDouble(line.substring(4, 7));
        b = Double.parseDouble(line.substring(8, 11));
        r /= 255;
        g /= 255;
        b /= 255;
        
        String outString = "  public final float[] " + name + " = {"
          + String.format("%.3ff", r) + ", " + String.format("%.3ff", g)+ ", " 
          + String.format("%.3ff", b) + "};";
        out.println(outString);
      }
      in.close();
      out.close();
      System.out.println("Input: " + inName);
      System.out.println("Output: " + outName);
      System.out.println("Lines processed: " + lineNum);
    } catch(Exception ex) {
      System.err.println("Error at line " + lineNum);
      ex.printStackTrace();
    }
  }

}
