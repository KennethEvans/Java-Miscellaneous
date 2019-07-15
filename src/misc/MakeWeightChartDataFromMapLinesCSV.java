package misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/*
 * Created on Jun 10, 2018
 * By Kenneth Evans, Jr.
 */

/**
 * MakeWeightChartDataFromMapLinesCSV makes a VCSV with data for plotting weight
 * vs. time from a MapLines CSV file created after digitizing the plot. The time
 * is X and the wiight is Y, where x normally corresponds to Longitude and y
 * normally corresponds to Latitude in MapLines.
 * 
 * @author Kenneth Evans, Jr.
 */
public class MakeWeightChartDataFromMapLinesCSV
{
    // Note that the year must be specified
    private static int YEAR = 2019;
    private static final String CSV_FILE_IN = "C:/Scratch/Weight Charts/WeightHistory-2019-FirstQuarter.MapLines.csv";
    private static final String CSV_FILE_OUT = "C:/Scratch/Weight Charts/WeightHistory-2019-FirstQuarter.MapLines.Data.csv";
    // These are generic and don't require changing the Java file
    // But are confusing owing to the copying of files
    // private static final String CSV_FILE_IN = "C:/Scratch/Weight
    // Charts/WeightChart.MapLines.csv";
    // private static final String CSV_FILE_OUT = "C:/Scratch/Weight
    // Charts/WeightChart.Data.csv";

    public static final String LS = System.getProperty("line.separator");
    public static final String COMMA = ",";
    private static final SimpleDateFormat defaultFormatter = new SimpleDateFormat(
        "MM/dd/yyyy");

    private void writeCSV(File maplinesFile, File outputFile) {
        int lineNum = 0;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new FileReader(maplinesFile));
            out = new PrintWriter(new FileWriter(outputFile));
            String line;
            String name;
            GregorianCalendar gCal;
            int offset = 0;
            double weight;
            double dayIn, weightIn;
            int day, day0;
            int month, month0;
            while((line = in.readLine()) != null) {
                lineNum++;
                // Skip the first line and but write the header for the output
                // file
                if(lineNum == 1) {
                    String[] headings = {"Date", "Weight"};
                    boolean first = true;
                    for(String heading : headings) {
                        if(!first) {
                            out.print(COMMA);
                            System.out.print(COMMA);
                        }
                        out.print(heading);
                        System.out.print(heading);
                        first = false;
                    }
                    out.println();
                    System.out.println();
                    continue;
                }
                String[] tokens = line.split(COMMA);
                if(tokens.length != 5) {
                    System.out.println("Line=" + lineNum + ": Got "
                        + tokens.length + "Expected 5");
                    return;
                }
                name = tokens[0];
                if(name.equals("\"Line 1\"")) {
                    offset = 0;
                } else if(name.equals("\"Line 2\"")) {
                    offset = 3;

                } else if(name.equals("\"Line 3\"")) {
                    offset = 6;

                } else if(name.equals("\"Line 4\"")) {
                    offset = 9;
                } else {
                    continue;
                }
                try {
                    dayIn = Double.parseDouble(tokens[1]);
                } catch(NumberFormatException ex) {
                    Utils.excMsg("Error parsing day for line " + lineNum, ex);
                    ex.printStackTrace();
                    return;
                }
                try {
                    weightIn = Double.parseDouble(tokens[2]);
                } catch(NumberFormatException ex) {
                    Utils.excMsg("Error parsing weight for line " + lineNum,
                        ex);
                    ex.printStackTrace();
                    return;
                }
                // Round to nearest .5
                weight = Math.round(weightIn * 2.) / 2.;
                // Get date
                day = day0 = (int)Math.round(dayIn);
                month = month0 = 0;
                if(day <= 30) {
                    day = day0;
                    month = offset;
                } else if(day > 30 && day <= 60) {
                    day = day0 - 30;
                    month = month0 + 1 + offset;
                } else if(day > 60) {
                    day = day0 - 60;
                    month = month0 + 2 + offset;
                }
                gCal = new GregorianCalendar(YEAR, month, day);
                out.println(
                    defaultFormatter.format(gCal.getTime()) + COMMA + weight);
                System.out.println(
                    defaultFormatter.format(gCal.getTime()) + COMMA + weight);
            }
        } catch(Exception ex) {
            Utils.excMsg("Error writing CSV file", ex);
            ex.printStackTrace();
            return;
        } finally {
            if(out != null) {
                out.close();
            }
            if(in != null) {
                try {
                    in.close();
                } catch(Exception ex) {
                    // Do nothing
                }
            }
        }
    }

    public static void main(String[] args) {
        MakeWeightChartDataFromMapLinesCSV app = new MakeWeightChartDataFromMapLinesCSV();
        File csvFileIn = new File(CSV_FILE_IN);
        File csvFileOut = new File(CSV_FILE_OUT);
        System.out.println("WeightChart Data from Maplines CSV" + LS);
        System.out.println("MapLines CSV File: " + csvFileIn.getPath());
        System.out.println("Output CSV File: " + csvFileOut.getPath());
        System.out.println("Year: " + YEAR);
        System.out.println();
        try {
            app.writeCSV(csvFileIn, csvFileOut);
        } catch(Exception ex) {
            Utils.excMsg("Failed to write " + csvFileOut.getPath(), ex);
            ex.printStackTrace();
        }

        System.out.println();
        System.out.println("All Done");
        System.exit(0);
    }

}
