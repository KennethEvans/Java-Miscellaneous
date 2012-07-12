package misc;

import java.awt.Color;

public class MakeMatlabRainbowMap
{
  /**
   * @param args
   */
  public static void main(String[] args) {
    RainbowColorScheme cs = new RainbowColorScheme();
    Color[] colors = cs.defineColors();
    Color color;
    System.out.printf("rainbowMap = [...\n");
    for(int i = 0; i < 256; i++) {
      color = colors[i];
        System.out.printf("    %5.3f %5.3f %5.3f;\n",
          color.getRed()/256., color.getGreen()/256., color.getBlue()/256.);
    }
    System.out.printf("    ];\n");
  }

}
