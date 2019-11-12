package misc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * Created on Aug 27, 2019
 * By Kenneth Evans, Jr.
 */

public class MakeWeightChartImage
{
    private static final String INPUT_DIR = "C:/Scratch/Weight Charts/Simple Weight Tracker";
    private static final String DEST_DIR = "C:/Scratch/Weight Charts/Simple Weight Tracker/Charts";
    private static List<Data> weights = new ArrayList<Data>();
    private static BufferedImage bi;
    private static int processingQuarter = 0;
    private static final String SAVE_TEMPLATE = "Weight Chart %d (through %s).png";
    private static final SimpleDateFormat lastDateFormatter = new SimpleDateFormat(
        "yyyy-MM-dd");

    private static int YEAR = 2019;
    private static int[] XVALS = {0, 5, 10, 15, 20, 25, 30, 5, 10, 15, 20, 25,
        30, 5, 10, 15, 20, 25, 30};
    private static int[] YVALS = {130, 135, 140, 145, 150};
    private static final int N_H_BLOCKS = XVALS.length / 2;
    private static final int N_V_BLOCKS = YVALS.length - 1;
    private static final String[] MONTH_NAMES = {"January", "February", "March",
        "April", "May", "June", "July", "August", "September", "October",
        "November", "December"};

    // Set these to the desired index in the YVALS
    private static final int Y_UPPER_INDEX = 1;
    private static final int Y_MID_INDEX = 2;
    private static final int Y_LOWER_INDEX = 3;

    private static final double PAPER_WIDTH = 11.0;
    private static final double PAPER_HEIGHT = 8.5;
    private static final double DPI = 300;
    private static final double MARGIN_TOP = 1.375;
    private static final double MARGIN_LEFT = 1.375;
    private static final double MARGIN_RIGHT = 1.25;

    private static final int WIDTH = round(PAPER_WIDTH * DPI);
    private static final int HEIGHT = round(PAPER_HEIGHT * DPI);

    private static final int GRAPH_X = round(MARGIN_LEFT * DPI);
    private static final int GRAPH_Y = round(MARGIN_TOP * DPI);
    private static final int BLOCK_WIDTH = round(
        DPI * (PAPER_WIDTH - MARGIN_LEFT - MARGIN_RIGHT) / N_H_BLOCKS);
    private static final int GRAPH_WIDTH = N_H_BLOCKS * BLOCK_WIDTH;
    private static final int GRAPH_HEIGHT = N_V_BLOCKS * BLOCK_WIDTH;
    private static final int TITLE_Y = round(.7 * DPI * MARGIN_TOP);
    private static final int XLABELS_Y = GRAPH_Y + GRAPH_HEIGHT
        + round(.2 * BLOCK_WIDTH);
    private static final int XTITLE_Y = GRAPH_Y + GRAPH_HEIGHT
        + round(.5 * BLOCK_WIDTH);
    private static final int MONTHS_Y = GRAPH_Y + GRAPH_HEIGHT + BLOCK_WIDTH;

    private static final String IMAGE_TYPE = "png";
    private static Color MAJOR_COLOR = new Color(0, 0, 0);
    private static Color MINOR_COLOR = new Color(0, 0, 0);
    private static Color CURVE_COLOR1 = new Color(196, 8, 1);
    private static Color CURVE_COLOR2 = new Color(1, 125, 196);
    private static Color CURVE_COLOR3 = new Color(8, 196, 1);
    private static Color CURVE_COLOR4 = new Color(130, 1, 196);
    private static Color UPPER_COLOR = new Color(255, 0, 0);
    private static Color MID_COLOR = new Color(0, 0, 255);
    private static Color LOWER_COLOR = new Color(51, 153, 102);
    private static float MINOR_WIDTH = 1f;
    private static float MAJOR_WIDTH = 8f;
    private static float CURVE_WIDTH = 8f;

    private static Font FONT_TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 72);
    private static Font FONT_XTITLE = new Font(Font.SANS_SERIF, Font.BOLD, 54);
    private static Font FONT_LABELS = new Font(Font.SANS_SERIF, Font.PLAIN, 54);
    private static Font FONT_MONTHS = new Font(Font.SANS_SERIF, Font.BOLD, 36);

    private static String currentDir = INPUT_DIR;
    private static String lastDate = "error";
    private static File outputFile;
    public static final String LS = System.getProperty("line.separator");

    private static void parseFile(File file) {
        BufferedReader in = null;
        String inputLine;
        String[] tokens;
        boolean first = true;
        try {
            in = new BufferedReader(new FileReader(file));
            while((inputLine = in.readLine()) != null) {
                if(first) {
                    first = false;
                    continue;
                }
                tokens = inputLine.split(",");
                // // DEBUG
                // System.out
                // .println("Date=" + tokens[0] + " Weight=" + tokens[1]);
                weights.add(new Data(tokens[0], tokens[1]));
            }
            in.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private static BufferedImage createImage() {
        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the minor grid lines
        g2d.setStroke(new BasicStroke(MINOR_WIDTH));
        g2d.setPaint(MINOR_COLOR);
        int x, y;
        double delta = BLOCK_WIDTH;
        for(int i = 0; i < N_H_BLOCKS * 10; i++) {
            x = round(i * delta / 10);
            g2d.drawLine(GRAPH_X + x, GRAPH_Y, GRAPH_X + x,
                GRAPH_Y + GRAPH_HEIGHT);
        }
        for(int i = 0; i < N_V_BLOCKS * 5; i++) {
            y = round(i * delta / 5);
            g2d.drawLine(GRAPH_X, GRAPH_Y + y, GRAPH_X + GRAPH_WIDTH,
                GRAPH_Y + y);
        }

        // Draw the large grid lines
        g2d.setStroke(new BasicStroke(MAJOR_WIDTH));
        g2d.setPaint(MAJOR_COLOR);
        for(int i = 0; i < N_H_BLOCKS + 1; i++) {
            x = round(i * delta);
            g2d.drawLine(GRAPH_X + x, GRAPH_Y, GRAPH_X + x,
                GRAPH_Y + GRAPH_HEIGHT);
        }
        for(int i = 0; i < N_V_BLOCKS + 1; i++) {
            y = round(i * delta);
            g2d.drawLine(GRAPH_X, GRAPH_Y + y, GRAPH_X + GRAPH_WIDTH,
                GRAPH_Y + y);
        }

        // Draw the guide lines
        g2d.setStroke(new BasicStroke(MAJOR_WIDTH));
        g2d.setPaint(UPPER_COLOR);
        y = round(Y_UPPER_INDEX * delta);
        g2d.drawLine(GRAPH_X, GRAPH_Y + y, GRAPH_X + GRAPH_WIDTH, GRAPH_Y + y);

        g2d.setPaint(MID_COLOR);
        y = round(Y_MID_INDEX * delta);
        g2d.drawLine(GRAPH_X, GRAPH_Y + y, GRAPH_X + GRAPH_WIDTH, GRAPH_Y + y);

        g2d.setPaint(LOWER_COLOR);
        y = round(Y_LOWER_INDEX * delta);
        g2d.drawLine(GRAPH_X, GRAPH_Y + y, GRAPH_X + GRAPH_WIDTH, GRAPH_Y + y);

        // Redraw the border
        g2d.setStroke(new BasicStroke(MAJOR_WIDTH));
        g2d.setPaint(MAJOR_COLOR);
        g2d.drawLine(GRAPH_X, GRAPH_Y, GRAPH_X, GRAPH_Y + GRAPH_HEIGHT);
        g2d.drawLine(GRAPH_X + GRAPH_WIDTH, GRAPH_Y, GRAPH_X + GRAPH_WIDTH,
            GRAPH_Y + GRAPH_HEIGHT);
        g2d.drawLine(GRAPH_X, GRAPH_Y, GRAPH_X + GRAPH_WIDTH, GRAPH_Y);
        g2d.drawLine(GRAPH_X, GRAPH_Y + GRAPH_HEIGHT, GRAPH_X + GRAPH_WIDTH,
            GRAPH_Y + GRAPH_HEIGHT);

        // Draw the text
        drawCenteredString(g2d, "Weight History " + YEAR,
            new Rectangle(GRAPH_X, TITLE_Y, GRAPH_WIDTH, 0), FONT_TITLE);
        drawCenteredString(g2d, "Day of Quarter",
            new Rectangle(GRAPH_X, XTITLE_Y, GRAPH_WIDTH, 0), FONT_XTITLE);
        // X labels
        for(int i = 0; i < XVALS.length; i++) {
            drawCenteredString(g2d, Integer.toString(XVALS[i]),
                new Rectangle(round(GRAPH_X + .5 * i * BLOCK_WIDTH), XLABELS_Y,
                    0, 0),
                FONT_LABELS);
        }
        // Y labels
        for(int i = 0; i < YVALS.length; i++) {
            drawRightJustifiedString(g2d, Integer.toString(YVALS[i]) + " ",
                new Rectangle(GRAPH_X,
                    round(GRAPH_Y + GRAPH_HEIGHT - i * BLOCK_WIDTH), 0, 0),
                FONT_LABELS);
        }
        // Month names
        g2d.setPaint(CURVE_COLOR1);
        for(int i = 0; i < 3; i++) {
            drawCenteredString(g2d, MONTH_NAMES[i], new Rectangle(round(
                GRAPH_X + round(.5 * GRAPH_WIDTH) + 3 * (i - 1) * BLOCK_WIDTH),
                MONTHS_Y, 0, 0), FONT_MONTHS);
        }
        g2d.setPaint(CURVE_COLOR2);
        for(int i = 3; i < 6; i++) {
            drawCenteredString(g2d, MONTH_NAMES[i],
                new Rectangle(
                    round(GRAPH_X + round(.5 * GRAPH_WIDTH)
                        + 3 * (i - 4) * BLOCK_WIDTH),
                    round(MONTHS_Y + .2 * BLOCK_WIDTH), 0, 0),
                FONT_MONTHS);
        }
        g2d.setPaint(CURVE_COLOR3);
        for(int i = 6; i < 9; i++) {
            drawCenteredString(g2d, MONTH_NAMES[i],
                new Rectangle(
                    round(GRAPH_X + round(.5 * GRAPH_WIDTH)
                        + 3 * (i - 7) * BLOCK_WIDTH),
                    round(MONTHS_Y + .4 * BLOCK_WIDTH), 0, 0),
                FONT_MONTHS);
        }
        g2d.setPaint(CURVE_COLOR4);
        for(int i = 9; i < 12; i++) {
            drawCenteredString(g2d, MONTH_NAMES[i],
                new Rectangle(
                    round(GRAPH_X + round(.5 * GRAPH_WIDTH)
                        + 3 * (i - 10) * BLOCK_WIDTH),
                    round(MONTHS_Y + .6 * BLOCK_WIDTH), 0, 0),
                FONT_MONTHS);
        }

        return bi;
    }

    private static void drawCurves() {
        Graphics2D g2d = bi.createGraphics();
        g2d.setStroke(new BasicStroke(CURVE_WIDTH));
        double weight;
        int x, y, x0 = GRAPH_X, y0 = GRAPH_Y;
        Color curColor = CURVE_COLOR1;
        for(Data data : weights) {
            Date date = new Date(data.date);
            LocalDate localDate = date.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            if(year < YEAR) continue;
            if(year > YEAR) break;
            // month is 1-12
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();
            int quarter = (month - 1) / 3 + 1;
            if(quarter != processingQuarter) {
                processingQuarter = quarter;
                // // DEBUG
                // System.out.println("processingQuarter=" + processingQuarter);
                switch(quarter) {
                case 1:
                    curColor = CURVE_COLOR1;
                    break;
                case 2:
                    curColor = CURVE_COLOR2;
                    break;
                case 3:
                    curColor = CURVE_COLOR3;
                    break;
                case 4:
                    curColor = CURVE_COLOR4;
                    break;
                }
                g2d.setPaint(curColor);
                weight = data.weight;
                x0 = getXVal(day, month);
                y0 = getYVal(weight);
                // // DEBUG
                // System.out.println("year=" + year + " month=" + month + "
                // day="
                // + day + " quarter=" + quarter + " processingQuarter="
                // + processingQuarter);
                // System.out
                // .println("weight=" + weight + " x0=" + x0 + " y0=" + y0);
                continue;
            }
            // Draw a line
            weight = data.weight;
            x = getXVal(day, month);
            y = getYVal(weight);
            // // DEBUG
            // System.out.println("year=" + year + " month=" + month + " day="
            // + day + " processingQuarter=" + processingQuarter + " weight="
            // + weight + " x0=" + x0 + " y0=" + y0 + " x=" + x + " y=" + y);
            g2d.drawLine(x0, y0, x, y);
            x0 = x;
            y0 = y;
            lastDate = lastDateFormatter.format(date);
        }
    }

    private static int getXVal(int day, int month) {
        int monthInQuarter = (month - 1) % 3;
        int xval;
        if(day == 31) {
            // Put the value for the 31st at 30.5
            xval = round(GRAPH_X + 3 * monthInQuarter * BLOCK_WIDTH
                + .1 * 30.5 * BLOCK_WIDTH);
        } else {
            xval = round(GRAPH_X + 3 * monthInQuarter * BLOCK_WIDTH
                + .1 * day * BLOCK_WIDTH);
        }
        return xval;

    }

    private static int getYVal(double weight) {
        int yval = round(GRAPH_Y + GRAPH_HEIGHT
            * (1 - (weight - YVALS[0]) / (YVALS[YVALS.length - 1] - YVALS[0])));
        return yval;
    }

    /**
     * Prompts for a SWT CSV file with dates and weights.
     * 
     * @return If successful or not.
     */
    private static boolean openFile() {
        // Prompt for file
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV",
            "csv");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Open SWT CSV File");
        if(currentDir != null) {
            File file = new File(currentDir);
            if(file != null && file.exists()) {
                chooser.setCurrentDirectory(file);
            }
        }
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
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
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG",
            "png");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Save Chart as Image");
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
            saveImage(file);
            return true;
        } else {
            return false;
        }
    }

    private static void saveImage(File file) throws IOException {
        ImageIO.write(bi, IMAGE_TYPE, file);
        System.out.println("Wrote " + file.getPath());
    }

    public static void parameterPrint(String name, int param) {
        System.out.println(name + "=" + param + " ("
            + String.format("%.3f", param / DPI) + ")");
    }

    /**
     * Convenience routine for rounding.
     * 
     * @param val
     * @return
     */
    private static int round(double val) {
        return (int)Math.round(val);
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g2d The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    public static void drawCenteredString(Graphics2D g2d, String text,
        Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g2d.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as
        // in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2)
            + metrics.getAscent();
        // Set the font
        g2d.setFont(font);
        // Draw the String
        g2d.drawString(text, x, y);
    }

    /**
     * Draw a String right justified in a Rectangle.
     *
     * @param g2d The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    public static void drawRightJustifiedString(Graphics2D g2d, String text,
        Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g2d.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text));
        // Determine the Y coordinate for the text (note we add the ascent, as
        // in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2)
            + metrics.getAscent();
        // Set the font
        g2d.setFont(font);
        // Draw the String
        g2d.drawString(text, x, y);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Set window decorations
            JFrame.setDefaultLookAndFeelDecorated(true);
            // Set the native look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Throwable t) {
            t.printStackTrace();
        }

        // String inFileName = INPUT_DIR + "/" + INPUT_FILE_NAME_PREFIX +
        // ".csv";
        // File inputFile = new File(inFileName);
        // System.out.println("Processing " + inputFile.getPath());

        // // DEBUG
        // parameterPrint("WIDTH", WIDTH);
        // parameterPrint("HEIGHT", HEIGHT);
        // parameterPrint("GRAPH_WIDTH", GRAPH_WIDTH);
        // parameterPrint("GRAPH_HEIGHT", GRAPH_HEIGHT);
        // parameterPrint("GRAPH_X", GRAPH_X);
        // parameterPrint("GRAPH_Y", GRAPH_Y);
        // parameterPrint("BLOCK_WIDTH", BLOCK_WIDTH);

        boolean res = openFile();
        if(!res) {
            System.out.println();
            System.out.println("Aborted");
            return;
        }
        createImage();
        drawCurves();
        try {
            outputFile = new File(
                DEST_DIR + "/" + String.format(SAVE_TEMPLATE, YEAR, lastDate));
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

}

class Data
{
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd");

    long date;
    double weight;

    public Data(String dateString, String weight) throws ParseException {
        date = dateFormat.parse(dateString).getTime();
        this.weight = Double.parseDouble(weight);
    }
}
