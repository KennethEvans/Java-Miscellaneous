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

/*
 * Created on Nov 9, 2019
 * By Kenneth Evans, Jr.
 */

public class FixFTMGED
{
    public static final String LS = System.getProperty("line.separator");
    private static final String FILE_NAME = "C:/Users/evans/Documents/Family Tree Maker/evans_2019-11-09.7.ged";
    private static ArrayList<Ged> list = new ArrayList<Ged>();
    private static ArrayList<Addition> additions = new ArrayList<Addition>();

    private static void readFile(String fileName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(fileName));
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
                            additions.add(new Addition(index + 1, newGed));
                            System.out.println(list.indexOf(ged)
                                + " Adding NAME=Unknown subtag to SUBM");
                        } catch(IOException ex) {
                            System.out.println(list.indexOf(ged)
                                + " Error converting index to string for SUBN");
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
            list.add(addition.index, addition.ged);
        }
    }

    public static void writeOutput() {
        String outName = FILE_NAME.replaceAll("\\.ged$", ".modified.ged");
        if(FILE_NAME.equals(outName)) {
            System.out.println("Error defining output file");
            return;
        }
        // File file = new File(outName);
        // if(file.exists()) {
        // System.out.println("Already exists: " + file.getPath());
        // }
        PrintWriter out = null;
        int lineNum = 0;
        try {
            out = new PrintWriter(new FileWriter(outName));
            for(Ged ged : list) {
                lineNum++;
                if(ged.value.isEmpty()) {
                    out.println(ged.level + " " + ged.tag);
                } else {
                    out.println(ged.level + " " + ged.tag + " " + ged.value);
                }
            }
            out.close();
            System.out.println("Output: " + outName);
        } catch(Exception ex) {
            System.err.println("Error at line " + lineNum);
            ex.printStackTrace();
        }
    }

    public static void printGedList() {
        for(Ged ged : list) {
            System.out.println(ged.level + " " + ged.tag + " " + ged.value);
        }
    }

    public static void main(String[] args) {
        String fileName = FILE_NAME;
        System.out.println(fileName);
        System.out.println();
        // Check if a GED file
        if(!Utils.getExtension(new File(fileName)).equals("ged")) {
            System.out.println("Not a GED file: " + fileName);
            System.out.println();
            System.out.println("Aborted");
            System.exit(-1);
        }
        readFile(fileName);
        processList();
        processAdditions();
        // printGedList();
        writeOutput();
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
        public int index;
        public Ged ged;

        Addition(int index, Ged ged) {
            this.index = index;
            this.ged = ged;
        }
    }
}
