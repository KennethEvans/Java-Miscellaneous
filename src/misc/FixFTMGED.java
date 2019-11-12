package misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * Created on Nov 9, 2019
 * By Kenneth Evans, Jr.
 */

public class FixFTMGED
{
    public static final String LS = System.getProperty("line.separator");
    private static ArrayList<Ged> list = new ArrayList<Ged>();
    private static ArrayList<Addition> additions = new ArrayList<Addition>();

    private static final String INPUT_DIR = "C:/Users/evans/Documents/Family Tree Maker";
    private static final String DEST_DIR = INPUT_DIR;
    private static String currentDir = INPUT_DIR;
    private static File inputFile;
    private static File outputFile;

    private static void parseFile(File file) {
        System.out.println("Processing: " + file.getPath());
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null) {
                if(line.isEmpty()) continue;
                list.add(new Ged(line));
            }
            in.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void processList() {
        Pattern pattern = Pattern.compile("[0-9][0-9][0-9][0-9]");
        Ged prev = null;
        for(Ged ged : list) {
            if(ged.tag.equals("CONC")) {
                // // Debug
                // if(ged.value.contains("Paul is a farmer")) {
                // System.out.println(list.indexOf(ged));
                // }
                String prevValue = prev.value;
                int nBlanks = 0;
                for(int i = prevValue.length() - 1; i >= 0; i--) {
                    if(prevValue.charAt(i) == ' ') {
                        nBlanks++;
                    } else {
                        if(prevValue.charAt(i) != ' ') {
                            // We are going to break the line before here so be
                            // sure the character before the non-blank one is
                            // also non-blank
                            if(prevValue.charAt(i - 1) != ' ') {
                                break;
                            } else {
                                nBlanks++;
                            }
                        }
                    }
                }
                if(nBlanks > 0) {
                    int len1 = prevValue.length() - nBlanks - 1;
                    String prefix = prevValue.substring(len1);
                    prev.value = prevValue.substring(0, len1);
                    ged.value = prefix + ged.value;
                }
            } else if(ged.tag.equals("DATE")) {
                // See if there is a year
                Matcher matcher = pattern.matcher(ged.value);
                if(!matcher.find()) {
                    String newValue = "(" + ged.value + ")";
                    System.out.println(list.indexOf(ged) + " Changing DATE="
                        + ged.value + " to phrase " + newValue
                        + " [DATE must have year specified]");
                    ged.value = newValue;
                }
            } else if(ged.tag.equals("CHAR")) {
                if(ged.value.equals("ANSI")) {
                    ged.value = "ASCII";
                }
            } else if(ged.tag.equals("VERS")) {
                if(ged.value.equals("5.5")) {
                    ged.value = "5.5.1";
                }
            } else if(ged.value.equals("SUBM")) {
                if(ged.tag.startsWith("@")) {
                    // Check SUBM with no subordinate tags
                    int index = list.indexOf(ged);
                    Ged next = list.get(index + 1);
                    if(next.level != ged.level + 1) {
                        try {
                            Ged newGed = new Ged(Integer.toString(ged.level + 1)
                                + " NAME Unknown");
                            additions.add(new Addition(ged, newGed));
                            System.out.println(list.indexOf(ged)
                                + " Adding NAME=Unknown subtag to SUBM");
                        } catch(IOException ex) {
                            Utils.excMsg(list.indexOf(ged)
                                + " Error converting index to string for SUBN",
                                ex);
                        }
                    }
                }
            } else if(ged.tag.equals("BIRT")) {
                if(ged.value.isEmpty()) {
                    // Check BIRT with no subordinate tags
                    int index = list.indexOf(ged);
                    Ged next = list.get(index + 1);
                    if(next.level != ged.level + 1) {
                        ged.value = "Y";
                        System.out.println(list.indexOf(ged)
                            + " Changing BIRT with no value and no "
                            + "subtags to value=Y");
                    }
                }
            } else if(ged.tag.equals("MARR")) {
                if(ged.value.isEmpty()) {
                    // Check MARR with no subordinate tags
                    int index = list.indexOf(ged);
                    Ged next = list.get(index + 1);
                    if(next.level != ged.level + 1) {
                        ged.value = "Y";
                        System.out.println(list.indexOf(ged)
                            + " Changing MARR with no value and no "
                            + "subtags to value=Y");
                    }
                }
            } else if(ged.tag.equals("ALIA")) {
                ged.tag = "NAME";
            } else if(ged.tag.equals("EMAIL") || ged.tag.equals("ADDR")
                || ged.tag.equals("PHON")) {
                if(ged.value.isEmpty()) {
                    ged.value = "Unknown";
                }
            }
            prev = ged;
        }
    }

    private static void processAdditions() {
        for(Addition addition : additions) {
            int index = list.indexOf(addition.gedParent);
            list.add(index + 1, addition.ged);
        }
    }

    public static void writeOutput(File file) {
        PrintWriter out = null;
        int lineNum = 0;
        try {
            out = new PrintWriter(new FileWriter(file));
            for(Ged ged : list) {
                lineNum++;
                if(ged.value.isEmpty()) {
                    out.println(ged.level + " " + ged.tag);
                } else {
                    out.println(ged.level + " " + ged.tag + " " + ged.value);
                }
            }
            out.close();
            System.out.println("Output: " + file.getPath());
        } catch(Exception ex) {
            Utils.excMsg("Error at line " + lineNum, ex);
        }
    }

    public static void printGedList() {
        for(Ged ged : list) {
            System.out.println(ged.level + " " + ged.tag + " " + ged.value);
        }
    }

    /**
     * Prompts for a SWT CSV file with dates and weights.
     * 
     * @return If successful or not.
     */
    private static boolean openFile() {
        // Prompt for file
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("GED",
            "ged");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Open GED File");
        if(currentDir != null) {
            File file = new File(currentDir);
            if(file != null && file.exists()) {
                chooser.setCurrentDirectory(file);
            }
        }
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            inputFile = file;
            // Save the selected path for next time
            currentDir = chooser.getSelectedFile().getPath();
            parseFile(file);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prompts for saving a file.
     * 
     * @return If successful or not.
     * @throws IOException
     */
    private static boolean saveFile() throws IOException {
        // Prompt for file
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("GED",
            "ged");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Save Modified GED File");
        if(outputFile != null) {
            chooser.setCurrentDirectory(outputFile.getParentFile());
            chooser.setSelectedFile(outputFile);
        } else if(currentDir != null) {
            File file = new File(currentDir);
            if(file != null && file.exists()) {
                chooser.setCurrentDirectory(file);
            }
        }
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            // Save the selected path for next time
            currentDir = chooser.getSelectedFile().getParentFile().getPath();
            if(file.exists()) {
                int res = JOptionPane.showConfirmDialog(null,
                    "File exists:" + LS + file.getPath() + LS
                        + "OK to overwrite?",
                    "File Exists", JOptionPane.OK_CANCEL_OPTION);
                if(res != JOptionPane.OK_OPTION) {
                    return false;
                }
            }
            writeOutput(file);
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            // Set window decorations
            JFrame.setDefaultLookAndFeelDecorated(true);
            // Set the native look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Throwable t) {
            t.printStackTrace();
        }

        // Open a file
        boolean res = openFile();
        if(!res) {
            System.out.println();
            System.out.println("Aborted");
            return;
        }

        // Process
        processList();
        processAdditions();

        // Save the output
        try {
            String outName = inputFile.getName().replaceAll("\\.ged$",
                ".modified.ged");
            if(outName.equals(inputFile.getName())) {
                System.out.println("Error defining output file");
                System.out.println();
                System.out.println("Aborted");
                return;
            }
            outputFile = new File(DEST_DIR, outName);
            res = saveFile();
            if(!res) {
                System.out.println();
                System.out.println("Aborted");
                return;
            }
        } catch(Exception ex) {
            System.out.println("Failed to save output file");
            ex.printStackTrace();
            System.out.println();
            System.out.println("Aborted");
            return;
        }

        System.out.println();
        System.out.println("All Done");
    }

    private static class Ged
    {
        public int level;
        public String tag;
        public String value;

        Ged(String line) throws IOException {
            int nLines = 0;
            int pos = 0;
            String tokens[] = line.split(" ");
            if(tokens.length < 2) {
                System.out.println("Error at line " + nLines + LS + line);
                throw (new IOException("Not enough parameters"));
            }
            level = Integer.parseInt(tokens[0]);
            pos += tokens[0].length() + 1;
            tag = tokens[1];
            if(tokens.length > 2) {
                pos += tokens[1].length() + 1;
                value = line.substring(pos);
            } else {
                value = "";
            }
        }
    }

    private static class Addition
    {
        public Ged gedParent;
        public Ged ged;

        Addition(Ged gedParent, Ged ged) {
            this.gedParent = gedParent;
            this.ged = ged;
        }
    }
}
