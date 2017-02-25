package misc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * My version of a Java example. Sets the fonts size and uses rainbow COLORS.
 * TestFrameIconSize
 * 
 * @author Kenneth Evans, Jr.
 */
public class TestFrameIconSize
{
    private static boolean useArray = false;
    private static final int[] SIZE_SPECIFIC = {16, 32, 48, 64, 96, 256};
    // private static final int[] SIZE_SPECIFIC = {16, 32, 48, 256};
    private static final int SIZE_MAX = 256;
    private static final int SIZE_MIN = 16;

    private static final String FONT_NAME = "Arial";
    private static final int PADDING = 5;
    // private static final Color[] COLORS = {Color.GREEN, Color.RED,
    // Color.YELLOW,
    // Color.WHITE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.ORANGE};

    /**
     * Makes the BufferedImage with the specified size and Color, writing the
     * size in the center.
     * 
     * @param size
     * @param color
     * @return
     */
    public static BufferedImage getImage(int size, Color color) {
        BufferedImage bi = new BufferedImage(size, size,
            BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, size - 1, size - 1);
        String text = "" + size;
        int fontSize = 10;
        Font font = null;
        FontMetrics fm;
        int width;
        // Find a size that just fits horizontally
        for(int i = 1; i < 200; i++) {
            font = new Font(FONT_NAME, Font.BOLD, i);
            g.setFont(font);
            fm = g.getFontMetrics();
            width = fm.stringWidth(text);
            if(width > size - 2 * PADDING) {
                fontSize = i - 1;
                break;
            }
        }
        // // DEBUG
        // System.out.println("fontSize=" + fontSize);

        font = new Font(FONT_NAME, Font.BOLD, fontSize);
        g.setFont(font);
        fm = g.getFontMetrics();
        int x = ((size - fm.stringWidth(text)) / 2);
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, x, y);
        g.dispose();

        return bi;
    }

    private static int[] makeSizeArray() {
        int[] sizes = null;
        if(useArray) {
            int nSizes = (SIZE_MAX - SIZE_MIN + 2) / 2;
            sizes = new int[nSizes];
            for(int ii = 0; ii < sizes.length; ii++) {
                sizes[ii] = SIZE_MIN + (ii * 2);
            }
        } else {
            sizes = SIZE_SPECIFIC;
        }
        return sizes;
    }

    public static void main(String[] args) {
        final int[] sizes = makeSizeArray();
        final Color[] colors = new RainbowColorScheme(sizes.length)
            .defineColors();

        Runnable r = new Runnable() {
            public void run() {
                // the GUI as seen by the user (without frame)
                JPanel gui = new JPanel(new BorderLayout());
                gui.setBorder(new EmptyBorder(2, 3, 2, 3));
                gui.setBackground(Color.WHITE);

                ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
                Vector<ImageIcon> icons = new Vector<ImageIcon>();
                for(int ii = 0; ii < sizes.length; ii++) {
                    BufferedImage bi = getImage(sizes[ii],
                        colors[ii % colors.length]);
                    images.add(bi);
                    ImageIcon imi = new ImageIcon(bi);
                    icons.add(imi);
                }
                JList<ImageIcon> list = new JList<ImageIcon>(icons);
                list.setVisibleRowCount(6);
                gui.add(new JScrollPane(list));

                JFrame f = new JFrame("Test Frame Icon Sizes");
                f.setIconImages(images);
                f.add(gui);
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // See http://stackoverflow.com/a/7143398/418556 for demo.
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // should be done last, to avoid flickering, moving,
                // resizing artifacts.
                f.setVisible(true);
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        SwingUtilities.invokeLater(r);
    }
}