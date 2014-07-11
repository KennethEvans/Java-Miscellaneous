package misc;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created on Sep 2, 2012
 * By Kenneth Evans, Jr.
 */

/**
 * RenameFilesByDate is a class to rename files using a prefix plus the date. Is
 * useful in handling Android downloads with generated names that are not in
 * date order.
 * 
 * @author Kenneth Evans, Jr.
 */
public class RenameFilesByDate
{
    public static final boolean DRY_RUN = true;

    /** Filter for selecting files. This one implements endsWith .jpg. */
    private static final String FILTER = ".*\\.jpg$";

    // private static final String PREFIX = "Aiden-";
    // private static final String PATH = "G:/Pictures/Aiden";

    // private static final String PREFIX = "Ella-Eric-";
    // private static final String PATH = "G:/Pictures/Ella-Eric";

    private static final String PREFIX = "Aiden-";
    private static final String PATH = "C:/Scratch/EVO SD Card Backup/2012-08-23/Pictures/Aiden";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
        "yyyy-MM-dd-HHmmss");

    /**
     * Defines a FilenameFilter to use with File.listFiles(). Currently uses a
     * regex, but could be mofified.
     * 
     * @author Kenneth Evans, Jr.
     */
    public static class Filter implements FilenameFilter
    {
        /** Regex to use in determining which files to accept. */
        private String regex;

        /**
         * Filter constructor.
         * 
         * @param regex The regex to use in matching files.
         */
        Filter(String regex) {
            this.regex = regex;
        }

        public boolean accept(File dir, String name) {
            // Can modify this to be more general, e.g. use regex
            return name.matches(regex);
        }
    }

    /**
     * Renames all files in the given directory satisfying the given filter to
     * be the given prefix followed by the date as formatted by the given format
     * and using the original extension.
     * 
     * @param path The path for the directory containing the files to be
     *            renamed.
     * @param prefix The prefix of the new filename.
     * @param filter Use to filter the files to be renamed. null will rename all
     *            files in the directory.
     * @param format The format to use. null will use "yyyy-MM-dd-HHmmss".
     */
    public static void rename(String path, String prefix,
        FilenameFilter filter, SimpleDateFormat format) {
        if(format == null) {
            format = FORMAT;
        }
        File dir = new File(path);
        if(!dir.exists()) {
            System.out.println("Directory not found: " + dir);
            return;
        }
        // If filter is null, than all files are selected
        File[] files = dir.listFiles(filter);
        long time;
        Date date;
        String newName;
        File newFile;
        for(File file : files) {
            // Don't do directories
            if(!file.isFile()) {
                continue;
            }
            // Do modified time, creation time is not available
            time = file.lastModified();
            date = new Date(time);
            newName = prefix + format.format(date) + "." + getExtension(file);
            newFile = new File(file.getParent(), newName);
            System.out.printf("%-40s %-40s\n", file.getName(),
                newFile.getPath());
            if(!DRY_RUN) {
                file.renameTo(newFile);
            }
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

    /**
     * @param args
     */
    public static void main(String[] args) {
        rename(PATH, PREFIX, new Filter(FILTER), null);
        System.out.println();
        System.out.println("All done");
    }

}
