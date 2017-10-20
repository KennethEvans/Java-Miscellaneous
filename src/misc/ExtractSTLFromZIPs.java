package misc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*
 * Created on Oct 20, 2017
 * By Kenneth Evans, Jr.
 * 
 * ZIP utilities from http://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java
 */

/**
 * ExtractSTLFromZIPs extracts all the files and directories in all the ZIP
 * files in the hard-coded ZIP_DIR to the hard-coded OUT_DIR.
 * 
 * Created to extract STL tracks from multiple downloads.
 * 
 */
public class ExtractSTLFromZIPs
{
    private static final String ZIP_DIR = "C:/Scratch/AAA/STL Download/ZIP";
    private static final String OUT_DIR = "C:/Scratch/AAA/STL Download/GPX";

    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    private static void processDir() {
        File zipDir = new File(ZIP_DIR);
        File[] zipFiles = zipDir.listFiles(new FilenameFilter() {
            public boolean accept(File file, String name) {
                String lcName = name.toLowerCase();
                if(lcName.endsWith(".zip")) return true;
                return false;
            }
        });
        int nProcessed = 0;
        int nFailed = 0;
        for(File file : zipFiles) {
            try {
                unzip(file.getPath(), OUT_DIR);
                nProcessed++;
            } catch(Exception ex) {
                ex.printStackTrace();
                nFailed++;
            }
        }
        System.out.println(
            "Processed " + nProcessed + " files, " + nFailed + " failed");
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified
     * by destDirectory (will be created if does not exists)
     * 
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String destDirectory)
        throws IOException {
        File destDir = new File(destDirectory);
        if(!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(
            new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while(entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if(!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath)
        throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(
            new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /**
     * Extracts a zip entry (file entry)
     * 
     * @param zipIn
     * @param filePath
     * @throws IOException
     */

    public static void main(String[] args) {
        System.out.println("Processing ZIP files in " + OUT_DIR);
        processDir();
        System.out.println();
        System.out.println("All Done");
    }

}
