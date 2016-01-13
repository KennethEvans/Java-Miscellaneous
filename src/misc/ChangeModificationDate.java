package misc;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/*
 * Created on Jan 12, 2016
 * By Kenneth Evans, Jr.
 */

/**
 * ChangeModificationDate is a class to change the last modified date of a file.
 * It is currently set up to change the date based on a date string in the
 * filename, but could be generalized.
 * 
 * @author Kenneth Evans, Jr.
 */
public class ChangeModificationDate
{
    private static final boolean DRYRUN = true;
    private static final String currentDirectory = "C:/Users/evans/Documents/PDF/Real Estate";
    private static final String prefix = "Castle-";

    public static void main(String[] args) {
        int year, month, day;
        try {
            File dir = new File(currentDirectory);
            File[] files = dir.listFiles();
            String fileName = null;
            int start = prefix.length();
            long lastModifiedTime;
            Calendar cal = null;
            boolean res = true;
            for(File file : files) {
                fileName = file.getName();
                if(!fileName.startsWith("Castle")) {
                    continue;
                }
                start = prefix.length();
                // Parse the name to get the new date
                year = Integer.parseInt(fileName.substring(start, start + 4));
                start += 5;
                month = Integer.parseInt(fileName.substring(start, start + 2));
                start += 3;
                day = Integer.parseInt(fileName.substring(start, start + 2));
                cal = Calendar.getInstance();
                // Set the old time
                lastModifiedTime = file.lastModified();
                cal.setTimeInMillis(lastModifiedTime);
                // Then change the date
                cal.set(year, month - 1, day);

                // Print info
                System.out.println("File: " + file.getPath());
                System.out.println("Old date: " + new Date(lastModifiedTime));
                System.out
                    .println("New date: " + new Date(cal.getTimeInMillis()));
                if(!DRYRUN) {
                    res = file.setLastModified(cal.getTimeInMillis());
                    if(res) {
                        System.out.println((DRYRUN ? "Dry Run " : "")
                            + "Succeeded: " + new Date(file.lastModified()));
                    } else {
                        System.out.println("Failed ");
                    }
                }
                if(file.lastModified() == lastModifiedTime) {
                    System.out.println("(No change)");
                }
            }
            System.out.println("All done");
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println();
            System.out.println("Aborted");
        }
    }

}
