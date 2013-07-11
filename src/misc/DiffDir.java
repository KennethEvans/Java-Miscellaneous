package misc;

import java.io.File;

import runutils.Run;

public class DiffDir
{
    public static final String LS = System.getProperty("line.separator");
    private static final String dir2 = "V:/Archive/PIXCEL LLC/14003";
    // private static final String dir1 = "L:/PIXCEL LLC/14003";
    private static final String dir1 = "W:/PIXCEL LLC/14003";
    private static String cmd = "";

    public static void diff() {
        int nFailed = 0;
        File dir = new File(dir1);
        File[] files1 = dir.listFiles();
        int nFiles = files1.length;
        String fileName = null;
        File file1, file2;
        String out = "";
        int n = 1;
        for(File file : files1) {
            fileName = file.getName();
            file1 = new File(dir1, fileName);
            file2 = new File(dir2, fileName);
            cmd = "diff \"" + file1.getPath() + "\" \"" + file2.getPath()
                + "\"";
            // cmd = "ls \"" + file1.getPath() + "\"";
            System.out.println("--- " + n++ + " / " + nFiles + " -----");
            System.out.println(cmd);
            System.out.println("    Sizes: " + file1.length() + ", "
                + file2.length());
            out = run(cmd);
            System.out.print(out);
            if(out.contains("The return code was: ")) {
                nFailed++;
            } else {
                System.out.println("Successful");
            }
        }
        System.out.println(LS + nFailed + " of " + nFiles + " failed");
    }

    public static String run(String cmd) {
        String out = "";
        Run run = new Run();
        run.setLineTerminator(LS);
        int retCode = run.exec(cmd);

        if(retCode != 0) {
            out += "The return code was: " + retCode + LS;
        }

        // Print output
        String output = run.getOutput();
        if(output != null) {
            out += output;
        }

        // Print error output
        String errOutput = run.getErrOutput();
        if(errOutput != null && errOutput.length() > 0) {
            out += "Error output: + LS";
            out += errOutput;
        }

        // Print error message
        String errMsg = run.getErrMsg();
        if(errMsg != null && errMsg.length() > 0) {
            out += "Error message:" + LS;
            out += errMsg;
        }

        // Print error stackTrace
        String stackTrace = run.getStackTrace();
        if(stackTrace != null && stackTrace.length() > 0) {
            out += "Stack trace:" + LS;
            out += stackTrace;
        }

        return out;
    }

    public static void main(String[] args) {
        diff();
        System.out.println(LS + "All done");
    }

}
