package misc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import run.Run;

/**
 * Converts a SVG file in Inkscape to PNG files of differing resolutions.
 * Overwrites files in the destination.
 * 
 * Using inkscape.exe, it gets no output, and the process has to be killed after
 * exit with 0.
 * 
 * Using inkscape.com, it gets no output, the process doesn't exit, and
 * proc.destroy() doesn't work. Because of the timeout, it takes longer.
 * 
 * Currently using inkscape.exe and proc.destroy() after. This seems to work.
 * 
 * Another approach would be to use --shell. This would give one Inkscape
 * process with each conversion sent as a one line input. Could probably send
 * quit. Couldn't use the Run package.
 * 
 * @author Kenneth Evans, Jr.
 */
public class MakePNGFromInkscape
{
    public static final int[] SIZES_WINDOWS = {16, 32, 48, 256};
    public static final int[] SIZES_WINDOWS_LARGE = {16, 32, 48, 64, 96, 256};
    public static final int[] SIZES_ANDROID = {48, 72, 96, 144, 192};

    private static final String SVG_NAME = "RepositoryManager.svg";
    private static final String DEST_BASE_NAME = "RepositoryManager";
    private static final int[] SIZES = SIZES_WINDOWS;

    private static final String SVG_PATH = "C:/Users/evans/Documents/Inkscape";
    private static final String DEST_PATH = "C:/Scratch/Icons/Test";
    private static final String EXE_PATH = "C:/Program Files/Inkscape/inkscape.exe";

    private static final String LS = Utils.LS;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
        "MM/dd/yyyy h:mm:ss a");

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

        // Inkscape remains running so do this
        Process proc = run.getProc();
        if(proc != null) {
            proc.destroy();
        }

        return out;
    }

    private static void processExport(int size) {
        ArrayList<String> argsList = new ArrayList<String>();
        File svgFile;
        svgFile = new File(SVG_PATH, SVG_NAME);
        argsList.add(EXE_PATH);
        argsList.add("--without-gui");
        argsList.add("--file=" + svgFile.getPath());
        argsList.add("--export-png=" + DEST_BASE_NAME + "." + size + "x" + size
            + ".png");
        argsList.add("--export-width=" + size);
        argsList.add("--export-height=" + size);
        // These just make it quit without doing anything
        // argsList.add("--verb=FileClose");
        // argsList.add("--verb=FileQuit");

        String[] cmds = argsList.toArray(new String[argsList.size()]);

        System.out.println("Running:");
        for(String line : cmds) {
            System.out.println(line);
        }
        System.out.println();

        try {
            run(cmds, new File(DEST_PATH));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Lists the files in the given path.
     */
    private static void listDir() {
        // Get the current files in the directory
        System.out.println("Currently in " + DEST_PATH + ":");
        File outDir = new File(DEST_PATH);
        File[] dirs = outDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if(file.getName().startsWith(DEST_BASE_NAME)) {
                    return true;
                }
                return false;
            }
        });
        Arrays.sort(dirs, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long mod1 = f1.lastModified();
                long mod2 = f2.lastModified();
                if(mod1 > mod2) return 1;
                if(mod1 < mod2) return -1;
                return 0;
            }
        });
        for(File file : dirs) {
            System.out.printf("%5.0f KB  %s  %s" + LS, file.length() / 1024.,
                dateFormat.format(new Date(file.lastModified())),
                file.getName());
        }

        String[] cmds = new String[] {"ls"};
        try {
            run(cmds, new File(DEST_PATH));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for(int size : SIZES) {
            processExport(size);
        }

        // Get the current files in the directory
        listDir();

        System.out.println();
        System.out.println("All Done");
        System.exit(0);
    }

}
