package misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Created on Aug 16, 2016
 * By Kenneth Evans, Jr.
 */

public class ParseSTLCSVFile
{
    private static final String FILE_NAME = "C:/Users/evans/Documents/GPSLink/STL/KennethEvans.0.csv";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
        "yyy-MM-dd hh:mm:ss");
    public static final String LS = System.getProperty("line.separator");
    public static final String[] MONTHS = {"January", "February", "March",
        "April", "May", "June", "July", "August", "September", "October",
        "November", "December"};
    private static ArrayList<Data> list = new ArrayList<Data>();

    private static void readFile(String fileName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(fileName));
            String line;
            String[] tokens;
            int lineNum = 0;
            while((line = in.readLine()) != null) {
                lineNum++;
                tokens = line.split(",");
                if(lineNum > 1) {
                    list.add(new Data(tokens[1], tokens[5], tokens[6],
                        tokens[8], tokens[10], tokens[11]));
                }
            }
            in.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void writeOutput() {
        for(Data data : list) {
            System.out.format("%20s %6d % 4d %10s %8.1f %8.1f" + LS,
                formatter.format(data.getDate()), data.getYear(),
                data.getMonth(), data.getType(), data.getDistance(),
                data.getHours());
        }
    }

    private static void writeSummary() {
        // Find the date range
        int minYear = Integer.MAX_VALUE;
        int maxYear = Integer.MIN_VALUE;
        int year;
        for(Data data : list) {
            year = data.getYear();
            if(year > maxYear) maxYear = year;
            if(year < minYear) minYear = year;
        }
        int nYears = maxYear - minYear + 1;
        double[][] distanceWalking = new double[nYears][12];
        double[][] hoursWalking = new double[nYears][12];
        double[][] distanceCycling = new double[nYears][12];
        double[][] hoursCycling = new double[nYears][12];
        double[][] hoursWorkout = new double[nYears][12];
        // Initialize
        for(int i = 0; i < nYears; i++) {
            for(int j = 0; j < 12; j++) {
                distanceWalking[i][j] = 0;
                hoursWalking[i][j] = 0;
                distanceCycling[i][j] = 0;
                hoursCycling[i][j] = 0;
                hoursWorkout[i][j] = 0;
            }
        }

        int year0, month0;
        for(Data data : list) {
            if(data.getType().equals("Walking")) {
                year0 = data.getYear() - minYear;
                month0 = data.getMonth();
                distanceWalking[year0][month0] += data.getDistance();
                hoursWalking[year0][month0] += data.getHours();
            } else if(data.getType().equals("Cycling")) {
                year0 = data.getYear() - minYear;
                month0 = data.getMonth();
                distanceCycling[year0][month0] += data.getDistance();
                hoursCycling[year0][month0] += data.getHours();
            } else if(data.getType().equals("Workout")) {
                year0 = data.getYear() - minYear;
                month0 = data.getMonth();
                hoursWorkout[year0][month0] += data.getHours();
            }
        }

        double[] distanceWalkingTotal = new double[nYears];
        double[] hoursWalkingTotal = new double[nYears];
        double[] distanceCyclingTotal = new double[nYears];
        double[] hoursCyclingTotal = new double[nYears];
        double[] hoursWorkoutTotal = new double[nYears];
        System.out.println(
            "Year     Month            Walking           Cycling      Workout        Total");
        System.out.println(
            "                        mi       hr       mi       hr       hr       mi       hr");
        for(int i = 0; i < nYears; i++) {
            distanceWalkingTotal[i] = 0;
            hoursWalkingTotal[i] = 0;
            distanceCyclingTotal[i] = 0;
            hoursCyclingTotal[i] = 0;
            hoursWorkoutTotal[i] = 0;
            for(int j = 0; j < 12; j++) {
                // Skip months with no events
                if(distanceWalking[i][j] == 0 && hoursWalking[i][j] == 0
                    && distanceCycling[i][j] == 0 && hoursCycling[i][j] == 0
                    && hoursWorkout[i][j] == 0) {
                    continue;
                }
                distanceWalkingTotal[i] += distanceWalking[i][j];
                hoursWalkingTotal[i] += hoursWalking[i][j];
                distanceCyclingTotal[i] += distanceCycling[i][j];
                hoursCyclingTotal[i] += hoursCycling[i][j];
                hoursWorkoutTotal[i] += hoursWorkout[i][j];
                System.out.format(
                    "%-6d %-10s %8.1f %8.1f %8.1f %8.1f %8.1f %8.1f %8.1f" + LS,
                    minYear + i, MONTHS[j], distanceWalking[i][j],
                    hoursWalking[i][j], distanceCycling[i][j],
                    hoursCycling[i][j], hoursWorkout[i][j],
                    distanceWalking[i][j] + distanceCycling[i][j],
                    hoursWalking[i][j] + hoursCycling[i][j]
                        + hoursWorkout[i][j]);
            }
            System.out.format(
                "%-6d %-10s %8.1f %8.1f %8.1f %8.1f %8.1f %8.1f %8.1f" + LS,
                minYear + i, "  Total", distanceWalkingTotal[i],
                hoursWalkingTotal[i], distanceCyclingTotal[i],
                hoursCyclingTotal[i], hoursWorkoutTotal[i],
                distanceWalkingTotal[i] + distanceCyclingTotal[i],
                hoursWalkingTotal[i] + hoursCyclingTotal[i]
                    + hoursWorkoutTotal[i]);
        }
    }

    public static void main(String[] args) {
        String fileName = FILE_NAME;
        System.out.println(fileName);
        System.out.println();
        readFile(fileName);
        // writeOutput();
        // System.out.println();
        writeSummary();
        System.out.println();
        System.out.println("All Done");

    }

    private static class Data
    {
        private String type;
        private double distance;
        private double hours;
        private int year;
        private int month;
        private Date date;

        public Data(String typeStr, String yearStr, String monthStr,
            String dateStr, String distanceStr, String durationStr)
            throws ParseException, NumberFormatException {
            this.type = typeStr;
            this.distance = Double.parseDouble(distanceStr);
            this.year = Integer.parseInt(yearStr);
            this.month = Integer.parseInt(monthStr);
            String[] tokens;
            tokens = durationStr.split("\\s");
            int len;
            for(String strVal : tokens) {
                len = strVal.length();
                if(len < 2) {
                    continue;
                }
                if(strVal.endsWith("d")) {
                    hours += Double.parseDouble(strVal.substring(0, len - 1))
                        * 24;
                } else if(strVal.endsWith("h")) {
                    hours += Double.parseDouble(strVal.substring(0, len - 1));
                } else if(strVal.endsWith("m")) {
                    hours += Double.parseDouble(strVal.substring(0, len - 1))
                        / 60;
                } else if(strVal.endsWith("s")) {
                    hours += Double.parseDouble(strVal.substring(0, len - 1))
                        / 3600;
                }
                date = formatter.parse(dateStr);
            }
        }

        /**
         * @return The value of year.
         */
        public int getYear() {
            return year;
        }

        /**
         * @return The value of month.
         */
        public int getMonth() {
            return month;
        }

        /**
         * @return The value of type.
         */
        public String getType() {
            return type;
        }

        /**
         * @return The value of distance.
         */
        public double getDistance() {
            return distance;
        }

        /**
         * @return The value of hours.
         */
        public double getHours() {
            return hours;
        }

        /**
         * @return The value of date.
         */
        public Date getDate() {
            return date;
        }

    }

}
