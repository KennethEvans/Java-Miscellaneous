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
 * Check LibreOffice ODT files (which are ZIP files).
 * 
 */
public class CheckODTFiles
{
    private static String SUFFIX = ".odt";
    // private static final String ZIP_DIR =
    // "C:\\Users\\evans\\Documents\\Memoirs-2020-06-07";
    private static final String ZIP_DIR = "C:\\Users\\evans\\Documents\\Memoirs";
    private static int nFiles = 0;
    private static int nPicturesDirectories = 0;
    private static int nTotalPictureFiles = 0;

    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    private static void processDir() {
        File zipDir = new File(ZIP_DIR);
        File[] zipFiles = zipDir.listFiles(new FilenameFilter() {
            public boolean accept(File file, String name) {
                String lcName = name.toLowerCase();
                if(lcName.endsWith(SUFFIX)) return true;
                return false;
            }
        });
        int nProcessed = 0;
        int nFailed = 0;
        for(File file : zipFiles) {
            nFiles++;
            try {
                unzip(file.getPath());
                nProcessed++;
            } catch(Exception ex) {
                ex.printStackTrace();
                nFailed++;
            }
        }
        System.out.println(
            "Processed " + nProcessed + " files, " + nFailed + " failed");
        System.out.println(nFiles + " " + SUFFIX + " files");
        System.out.println(nPicturesDirectories + " with Picture directories");
        System.out.println(
            "Total number of embedded image files is " + nTotalPictureFiles);
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified
     * by destDirectory (will be created if does not exists)
     * 
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public static void unzip(String zipFilePath) throws IOException {
        System.out.println(zipFilePath);
        ZipInputStream zipIn = new ZipInputStream(
            new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // Iterates over entries in the ZIP file
        int nPictureFiles = 0;
        boolean picturesFound = false;
        while(entry != null) {
            // System.out.println(entry.getName());
            if(entry.getName().startsWith("Pictures/")) {
                picturesFound = true;
                nPictureFiles++;
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        if(picturesFound) {
            nPicturesDirectories++;
            nTotalPictureFiles += nPictureFiles;
            System.out.println(
                "  There are " + nPictureFiles + " images in Pictures");
        } else {
            System.out.println("  There is no Pictures directory");
        }
    }

    public static void extractFile(ZipInputStream zipIn, String filePath)
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
        System.out.println("Processing " + SUFFIX + " files in " + ZIP_DIR);
        processDir();
        System.out.println();
        System.out.println("All Done");
    }

}
