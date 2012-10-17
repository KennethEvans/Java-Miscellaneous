package misc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/*
 * Created on Oct 13, 2012
 * By Kenneth Evans, Jr.
 */

public class Mandelbrodt extends JFrame
{
    private static final long serialVersionUID = 1L;
    private static final int MAX_ITER = 570;
    private int x0 = 500;
    private int y0 = 300;
    private double rMax = 8;
    private double ZOOM = 250;
    private BufferedImage image;
    private double zx, zy, cX, cY, tmp;

    // private RainbowColorScheme colors;

    public Mandelbrodt() {
        super("Mandelbrodt");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        image = new BufferedImage(getWidth(), getHeight(),
            BufferedImage.TYPE_INT_RGB);
        // colors = new RainbowColorScheme(MAX_ITER);
        int iter;
        int color;
        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                zx = zy = 0;
                cX = (x - x0) / ZOOM;
                cY = (y - y0) / ZOOM;
                iter = MAX_ITER;
                while(zx * zx + zy * zy < rMax && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                // color = iter == 0 ? 0 : RainbowColorScheme.toColorInt(colors
                // .getStoredColor((double)iter / MAX_ITER));
                color = iter == 0 ? 0 : (int)((double)iter / MAX_ITER * 256
                    * 256 * 256);
                image.setRGB(x, y, color);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Mandelbrodt().setVisible(true);
    }

}
