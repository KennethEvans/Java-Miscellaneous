package misc;

import java.io.File;

public class Countm4a
{
    public static final String LS = System.getProperty("line.separator");
    int nFiles = 0;
    int nConverted = 0;
    private static final String dir1 = "C:/Scratch/AAA";

    /**
     * Renames the .m4a file to .mp3.
     */
    private void renameFiles(File musicDir) {
        boolean countOnly = true;
        nFiles = 0;
        nConverted = 0;
        String msg;
        if(!musicDir.exists()) {
            msg = "Directory does not exist:\n" + musicDir.getPath();
            System.out.println(msg);
            return;
        }

        // Process the files
        process(musicDir, countOnly);

        // Show results
        if(countOnly) {
            msg = "There are " + nConverted + " .m4a files of " + nFiles
                + " files total in " + musicDir.getPath() + ".";
        } else {
            msg = "Converted " + nConverted + " of " + nFiles + " files in "
                + musicDir.getPath() + ".";
        }
        System.out.println(msg);
    }

    /**
     * Iteratively process directories and files, renaming those that have
     * extension .m4a.
     * 
     * @param file
     * @return
     */
    private boolean process(File file, boolean countOnly) {
        String newName = null;
        File newFile = null;
        int len = 0;
        boolean ok = false;
        if(file.isDirectory()) {
            // Is a directory, process the files in it
            File[] files = file.listFiles();
            for(File file1 : files) {
                if(!process(file1, countOnly)) {
                    return false;
                }
                ;
            }
            // No files
            return true;
        } else {
            // Is a file
            nFiles++;
            String ext = getExtension(file);
            if(ext.equals("m4a")) {
                if(countOnly) {
                    nConverted++;
                    return true;
                }
                newName = file.getPath();
                len = newName.length();
                newFile = new File(newName.substring(0, len - 3) + "mp3");
                // Set ok = true here instead of rename to test
                ok = file.renameTo(newFile);
                if(!ok) {
                    String msg = "Could not rename " + file.getPath();
                    System.out.println(msg + "\n");
                } else {
                    nConverted++;
                }
                return ok;
            }
            return true;
        }
    }

    /**
     * Get the extension of a file.
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

    public static void main(String[] args) {
        Countm4a app = new Countm4a();
        File topDir = new File(dir1);
        File[] files = topDir.listFiles();
        for(File file : files) {
            if(file.isDirectory()) {
                System.out.println(file.getPath());
                app.renameFiles(file);
            }
        }
        System.out.println(LS + "All done");
    }

}
