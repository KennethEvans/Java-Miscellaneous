package misc;

import java.awt.Color;

public class MakeMatplotlibRainbowMap
{
  /**
   * @param args
   */
  public static void main(String[] args) {
    RainbowColorScheme cs = new RainbowColorScheme();
    Color[] colors = cs.defineColors();
    Color color;
    System.out.printf("cdict = {\n");
    System.out.printf("  'red': (\n");
    for(int i = 0; i < 256; i++) {
      color = colors[i];
      if(i == 0) {
        System.out.printf("    (%5.3f, %5.3f, %5.3f),\n",
          0., color.getRed()/256., color.getRed()/256.);
      }
      System.out.printf("    (%5.3f, %5.3f, %5.3f),\n",
        (i + 1) /256., color.getRed()/256., color.getRed()/256.);
    }
    System.out.printf("    ),\n");
    System.out.printf("  'green': (\n");
    for(int i = 0; i < 256; i++) {
      color = colors[i];
      if(i == 0) {
        System.out.printf("    (%5.3f, %5.3f, %5.3f),\n",
          0., color.getGreen()/256., color.getGreen()/256.);
      }
      System.out.printf("    (%5.3f, %5.3f, %5.3f),\n",
        (i + 1) /256., color.getGreen()/256., color.getGreen()/256.);
    }
    System.out.printf("    ),\n");
    System.out.printf("  'blue': (\n");
    for(int i = 0; i < 256; i++) {
      color = colors[i];
      if(i == 0) {
        System.out.printf("    (%5.3f, %5.3f, %5.3f),\n",
          0., color.getBlue()/256., color.getBlue()/256.);
      }
      System.out.printf("    (%5.3f, %5.3f, %5.3f),\n",
        (i + 1) /256., color.getBlue()/256., color.getBlue()/256.);
    }
    System.out.printf("    ),\n");
    System.out.printf("  }\n");
  }

}
