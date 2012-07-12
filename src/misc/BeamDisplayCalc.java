package misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class BeamDisplayCalc
{
  private final static String inName = "n:\\scratch\\beamDisplayData.txt";
  private final static String outName = "n:\\scratch\\beamDisplayData.new.txt";

  /**
   * Fixes the data file by getting the first time value and adding 60 sec to
   * it for each succeeding value.  Resequences the time values, but they may
   * not correspond to the right times for the beam data.  In particular, if
   * there are missing data, the times can extend into the future.
   */
  public void fix() {
    int lineNum = 0;
    int val0 = 0;
    int val1 = 0;
    BufferedReader in = null;
    PrintWriter out = null;
    try {
      in = new BufferedReader(new FileReader(inName));
      out = new PrintWriter(new FileWriter(outName));
      String line;
      while((line = in.readLine()) != null) {
        if(lineNum == 0) {
          String valString = line.substring(0, 8);
          val0 = Integer.parseInt(valString);
        }
        val1 = val0 + 60 * lineNum;
        lineNum++;
        // Assume the number of digits stays the same so 0:8 -> 0:8
        String outString = val1 + line.substring(9, line.length());
        out.println(outString);
      }
      in.close();
      out.close();
      System.out.println("Input: " + inName);
      System.out.println("Output: " + outName);
      System.out.println("Lines processed: " + lineNum);
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    BeamDisplayCalc bdc = new BeamDisplayCalc();
    bdc.fix();
    System.out.println("All Done");
  }

}
