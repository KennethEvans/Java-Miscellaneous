package misc;

import java.awt.Color;

public class MakeSVGRainbowImage
{
    private static final String[] PRE = {
        "<?xml version=\"1.0\" standalone=\"no\"?>",
        "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"",
        "  \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">",
        "<svg width=\"8cm\" height=\"4cm\" viewBox=\"0 0 800 400\" version=\"1.1\"",
        "     xmlns=\"http://www.w3.org/2000/svg\">",
        "  <desc>Rainbow Gradient</desc>", "  <g>", "    <defs>",
        "      <linearGradient id=\"MyGradient\">",
        // End
    };
    private static final String[] POST = {"      </linearGradient>",
        "    </defs>", "", "    <!-- Outline the drawing area in black -->",
        "    <rect fill=\"none\" stroke=\"black\" ",
        "          x=\"1\" y=\"1\" width=\"798\" height=\"398\"/>", "",

        "    <!-- The rectangle is filled using a linear gradient paint server -->",
        "    <rect fill=\"url(#MyGradient)\" stroke=\"black\" stroke-width=\"5\"  ",
        "          x=\"100\" y=\"100\" width=\"600\" height=\"200\"/>",
        "  </g>", "</svg>",
        // End
    };

    /**
     * @param args
     */
    public static void main(String[] args) {
        RainbowColorScheme cs = new RainbowColorScheme();
        Color[] colors = cs.defineColors();

        // Start
        for(String line : PRE) {
            System.out.println(line);
        }

        // Colors
        int nColors = colors.length;
        String offset;
        Color color;
        for(int i = 0; i < nColors; i++) {
            color = colors[i];
            offset = String.format("%5.3f", (double)i / (nColors - 1));
            System.out
                .println("        <stop offset=\"" + offset + "\" stop-color=\""
                    + RainbowColorScheme.toColorHexString(color) + "\" />");
        }

        // Start
        for(String line : POST) {
            System.out.println(line);
        }
    }

}
