package misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * BackupStatus is a class to show the status of backups in the directory given
 * on the command line or the hard-coded directory if none given. It assumes
 * entries have been written to logs located in that directory using something
 * like:
 * 
 * <pre>
 * echo %date% %time% FROM %computername% >> \Logs\Xxx.log
 * </pre>
 * 
 * The times are output to millisec accuracy by echo. It prints a list of each
 * TO line from the logs and the last FROM line. It then prints a history in
 * chronological order.<br>
 * <br>
 * 
 * This was originally implemented with a TreeSet, but the TreeSet did not keep
 * a new item if the comparison was 0. That is, it threw away any additional
 * items that had the same time. It now uses an ArrayList and sorts afterward.
 * 
 * @author Kenneth Evans, Jr.
 */
public class BackupStatus
{
    /** Default directory if none entered on the command line */
    private String directory = "J:/logs";
    /** Date formatter that duplicates the results of echo. */
    private static final SimpleDateFormat defaultFormatter = new SimpleDateFormat(
        "EEE MM/dd/yyyy HH:mm:ss.SSS");
    /** ArrayList to accumulate the history. */
    private ArrayList<Data> results = new ArrayList<Data>();

    /**
     * Data Internal class to hold a File and the size
     * 
     * @author Kenneth Evans, Jr.
     */
    class Data implements Comparable<Data>
    {
        private String name = "Unknown";
        private String computer = "Unknown";
        private Date date = null;

        Data(String name, String line) {
            this.name = name;
            int fromStart = line.indexOf(" FROM");
            if(fromStart < 0) {
                date = new Date(0);
            } else {
                computer = line.substring(fromStart + 6);
                String dateString = line.substring(0, fromStart);
                try {
                    date = defaultFormatter.parse(dateString);
                } catch(ParseException ex) {
                    date = new Date(0);
                }
            }
        }

        /**
         * @return The value of name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return The value of date.
         */
        public Date getDate() {
            if(date == null) return new Date(0);
            return date;
        }

        /**
         * @return The value of computer.
         */
        public String getComputer() {
            return computer;
        }

        public int compareTo(Data data) {
            Date dataDate = data.getDate();
            if(date == null && dataDate == null) return 0;
            if(date == null) return -1;
            if(dataDate == null) return 1;
            return date.compareTo(data.getDate());
        }

    }

    public void process() {
        if(directory == null) {
            System.err.println("No directory specified");
            System.exit(1);
        }
        File top = new File(directory);
        if(!top.isDirectory()) {
            System.err.println("Not a directory: " + top.getPath());
            System.exit(1);
        }
        File directoryList[] = top.listFiles();
        for(File log : directoryList) {
            String ext = getExtension(log);
            if(ext == null || !ext.equals("log")) continue;
            processLog(log);
        }
    }

    private void processLog(File log) {
        // int lineNum = 0;
        BufferedReader in = null;
        ArrayList<String> list = new ArrayList<String>();
        String lastFrom = "";
        try {
            in = new BufferedReader(new FileReader(log));
            String line;
            while((line = in.readLine()) != null) {
                // lineNum++;
                if(line.contains("FROM")) {
                    // Start over at each FROM, leaving the last one and what
                    // follows
                    list.clear();
                    list.add(line);
                    lastFrom = line;
                } else if(line.contains("TO")) {
                    list.add(line);
                }
            }
            in.close();
            Data data = new Data(log.getName(), lastFrom);
            results.add(data);
            System.out.printf("%s\n", log.getName());
            for(String item : list) {
                System.out.printf("  %s\n", item);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Prints out the results
     */
    public void printResults() {
        System.out.printf("\nHistory\n\n");
        String format = "%-25s %s from %s\n";
        Collections.sort(results);
        for(Data data : results) {
            System.out.printf(format, data.getName(),
                defaultFormatter.format(data.getDate()), data.getComputer());
        }
    }

    /**
     * Get the extension of a file (without the dot).
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

    protected boolean parseCommand(String[] args) {
        int i;

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
                directory = args[i];
            }
        }
        return true;
    }

    protected void usage() {
        System.out.println("\nUsage: java " + this.getClass().getName()
            + " [Options] directory\n"
            + "  Print the last entries for USB drive logs in directory\n"
            + "    -h        Help (This message)\n" + "");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        BackupStatus app = new BackupStatus();
        if(!app.parseCommand(args)) {
            System.exit(1);
        }
        app.process();
        app.printResults();
        System.out.println();
        System.out.println("All done");
    }
}
