package misc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * Created on Mar 8, 2017
 * By Kenneth Evans, Jr.
 */

/**
 * Class to find matching pictures in one directory in all the subdirectories of
 * another directory. FindMatchingPictures
 * 
 * @author Kenneth Evans, Jr.
 */
/**
 * FindMatchingPictures
 * 
 * @author Kenneth Evans, Jr.
 */
public class FindMatchingPictures
{
    private static final String SRC_DIR = "M:/Android Backup/SM-G930V/2017-03-08/internal/Pictures";
    private static final String DST_DIR = "C:/Users/evans/Pictures/Android";
    private static final String BACKUP_DIR = "C:/Users/evans/Pictures/Android/Unknown";
    private static final File BACKUP_DIR_FILE = new File(BACKUP_DIR);
    private static final SimpleDateFormat defaultFormatter = new SimpleDateFormat(
        "yyyy-MM-dd");

    private static void run() {
        ArrayList<Tally> tallyList = new ArrayList<Tally>();
        ArrayList<File> dirList = new ArrayList<File>();
        // Find the source files
        File src = new File(SRC_DIR);
        File[] srcFiles = src.listFiles();
        for(File file : srcFiles) {
            if(file.isDirectory()) continue;
            tallyList.add(new Tally(file));
        }

        // Sort them
        Collections.sort(tallyList, new Comparator<Tally>() {
            public int compare(Tally t1, Tally t2) {
                return Long.compare(t2.File.lastModified(),
                    t1.File.lastModified());
            }
        });

        // Make a list of the dst directories
        File dst = new File(DST_DIR);
        addFiles(dst, dirList);

        // Search for matches to the src files over the dst directories
        System.out.println("Searching...");
        for(File dir : dirList) {
            System.out.println(dir.getPath());
            File[] files = dir.listFiles(new FileFilter() {
                public boolean accept(File dir) {
                    return dir.isFile();
                }
            });
            for(File file : files) {
                for(Tally tally : tallyList) {
                    if(tally.File.getName().equals(file.getName())) {
                        tally.Name = true;
                        if(tally.File.length() == file.length()) {
                            tally.Size = true;
                        }
                        tally.FoundList.add(file);
                    }
                }
            }
        }

        // Print the ones found
        String date, size;
        System.out.println();
        System.out.println("Found:");
        int nFound = 0;
        for(Tally tally : tallyList) {
            if(tally.Name) {
                nFound++;
                date = defaultFormatter.format(tally.File.lastModified());
                size = tally.Size ? "false " : "true  ";
                System.out.println(date + " " + size + tally.File.getPath());
                for(File file : tally.FoundList) {
                    System.out.println("    " + file.getPath());
                }
            }
        }
        System.out.println();
        System.out
            .println("Total found: " + nFound + " of " + tallyList.size());

        // Print the ones not found
        System.out.println();
        System.out.println("Not found:");
        int nNotFound = 0;
        File dstFile;
        Path dstPath;
        for(Tally tally : tallyList) {
            if(!tally.Name) {
                nNotFound++;
                date = defaultFormatter.format(tally.File.lastModified());
                dstFile = new File(BACKUP_DIR, tally.File.getName());
                dstPath = dstFile.toPath();
                System.out.println(date + " " + tally.File.getPath());
                // Copy to the backup location
                if(dstFile.exists()) {
                    // System.out.println(" Already exists: " + dstPath);
                } else {
                    try {
                        Files.copy(tally.File.toPath(), dstPath);
                    } catch(IOException ex) {
                        System.out.println(" Failed to copy to " + dstPath);
                    }
                }
            }
        }
        System.out.println();
        System.out.println(
            "Total not found: " + nNotFound + " of " + tallyList.size());
        System.out.println("Copied to or already in " + BACKUP_DIR);
    }

    /**
     * Recursively add directories to the list.
     * 
     * @param dir
     * @param dirList
     */
    private static void addFiles(File dir, ArrayList<File> dirList) {
        dirList.add(dir);
        File[] dirs = dir.listFiles(new FileFilter() {
            public boolean accept(File dir) {
                // Don't include the backup
                if(dir.getPath().toLowerCase()
                    .equals(BACKUP_DIR_FILE.getPath().toLowerCase())) {
                    return false;
                }
                return dir.isDirectory();
            }
        });
        if(dirs == null || dirs.length == 0) return;
        for(File dir1 : dirs) {
            addFiles(dir1, dirList);
        }
    }

    public static void main(String[] args) {
        run();
        System.out.println();
        System.out.println("All Done");

    }

    /**
     * Tally is a class to keep track of whether a file is matched.
     * 
     * @author Kenneth Evans, Jr.
     */
    private static class Tally
    {
        public File File;
        public boolean Name;
        public boolean Size;
        public ArrayList<File> FoundList = new ArrayList<File>();

        public Tally(File fileName) {
            File = fileName;
            Name = false;
            Size = false;
        }
    }

}
