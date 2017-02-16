package misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import run.Run;

/**
 * Renames origin to github in repositories using the CSV output from
 * RepositoryManager.
 * 
 * !!!!!!!!!! Warning: The CSV needs to have \n converted to space first,
 * otherwise the reader won't get the full line.
 * 
 * !!!!!!!!!! Warning: If you rerun after some repositories have been processed,
 * the CSV file will be out of date.
 * 
 * @author Kenneth Evans, Jr.
 */
public class RenameGitRemotes
{
    // !!!!!!!!!! Warning: The CSV needs to have \n converted to space, first,
    // otherwise the reader won't get the full line.
    private static final String CSV_FILE = "C:/Scratch/Git/Repository Manager/Repository Summary.csv";
    /** Set this to limit processing while testing. 2 gets the first line. */
    private static final int LAST_LINE_NUM = 0;
    private static final String FROM_NAME = "origin";
    private static final String TO_NAME = "github";
    private static final String REMOTE_PATTERN = "origin git@github.com:";
    /** Column, zero-based, that is named Remotes in the CSV. */
    private static final int REMOTES_COL = 13;
    private static final String DELIMITER = ",";
    private static final String LS = Utils.LS;

    /**
     * Generic method to run a command in a given directory, and capture the
     * output. Uses the current environment, and default timeout.
     * 
     * @param cmdArray
     * @param dir
     * @return
     * @throws IOException
     */
    public static String run(String[] cmdArray, File dir) throws IOException {
        String out = "";
        Run run = new Run();
        // Need to set useBuffer to get the output
        int retCode = run.run(cmdArray, null, dir, true);

        if(retCode != 0) {
            out += "The return code was: " + retCode + LS;
        }

        // Print output
        String output = run.getOutput();
        if(output != null) {
            out += output;
        }

        // Print error output
        String errOutput = run.getError();
        if(errOutput != null && errOutput.length() > 0) {
            out += "Error output: + LS";
            out += errOutput;
        }

        return out;
    }

    /**
     * Reads the CSV file and checks for the REMOTE_PATTERN in the REMOTES_COL.
     * 
     * @param fileName
     */
    private static void readFile(String fileName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(fileName));
            String line;
            String[] tokens;
            int lineNum = 0;
            while((line = in.readLine()) != null && lineNum < LAST_LINE_NUM) {
                lineNum++;
                tokens = line.split(DELIMITER);
                if(lineNum > 1) {
                    if(tokens[REMOTES_COL].contains(REMOTE_PATTERN)) {
                        rename(tokens[0]);
                    }
                }
            }
            in.close();
        } catch(Exception ex) {
            ex.printStackTrace();
            // Exit here, don't go on and do any more
            System.exit(-1);
        }
    }

    private static boolean rename(String repository) {
        if(repository == null || repository.length() == 0) {
            return false;
        }
        try {
            System.out.println(repository);
            File dir = new File(repository);
            // String[] cmdArray = {"cmd", "/c", "git remote -v & git remote
            // -v"};
            String[] cmdArray = {"cmd", "/c",
                "git remote -v & git remote rename " + FROM_NAME + " " + TO_NAME
                    + " & echo -----------------------------------------------"
                    + " & git remote -v"};
            String out = run(cmdArray, dir);
            System.out.println(out);
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        readFile(CSV_FILE);
        System.out.println();
        System.out.println("All Done");
    }

}
