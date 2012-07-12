package misc;

import java.io.File;
import java.util.Date;

/**
 * CopyZIP copies a specified directory structure from a source (typically, a
 * ZIP drive) to a destination drive. The source structure implemented is:
 * 
 * <pre>
 *   I:/Images/R
 *            /Video
 *            
 *   R:/Pictures/R/Images/Images&lt;suffix&gt;         
 *                /Video/Video&lt;suffix&gt;
 * </pre>
 * 
 * @author Kenneth Evans, Jr.
 */
public class CopyZIP
{
  /**
   * If there is already a file in the destination with the target name, then it
   * renames the existing file by appending this String. Use null to delete the
   * file instead.
   */
  private static final String saveSuffix = ".save";
  /**
   * Base source directory, expected to have Images and Video subdirectories.
   */
  private static final String zipDir0 = "I:/Images";
  /**
   * Base destination image directory. Will have /Images<suffix> appended.
   */
  private static final String rImagesDir0 = "R:/Pictures/R/Images";
  /**
   * Base destination video directory. Will have /Video<suffix> appended.
   */
  private static final String rVideoDir0 = "R:/Pictures/R/Video";

  /**
   * Handles copying the files from the source to the destination and doing the
   * proper checking.
   * 
   * @param suffix The suffix to use for the target directories, e.g.
   *          Images<suffix> and Video<suffix).
   */
  public static void copy(String suffix) {
    boolean success = false;
    boolean doVideo = true;
    String imgSrcDir = zipDir0 + "/R";
    String videoSrcDir = zipDir0 + "/Video";
    String imgDstDir = rImagesDir0 + "/Images" + suffix;
    String videoDstDir = rVideoDir0 + "/Video" + suffix;
    File imgSrcDirFile = new File(imgSrcDir);
    File videoSrcDirFile = new File(videoSrcDir);
    File imgDstDirFile = new File(imgDstDir);
    File videoDstDirFile = new File(videoDstDir);

    // Check and create directories
    if(!imgSrcDirFile.isDirectory()) {
      System.err.println("Not a directory: " + imgSrcDirFile.getPath());
      System.exit(1);
    }
    if(!videoSrcDirFile.isDirectory()) {
      doVideo = false;
    }
    if(!imgDstDirFile.isDirectory()) {
      success = imgDstDirFile.mkdir();
      if(!success) {
        System.err.println("Cannot create: " + imgDstDirFile.getPath());
        System.exit(1);
      }
    }
    if(doVideo && !videoDstDirFile.isDirectory()) {
      success = videoDstDirFile.mkdir();
      if(!success) {
        System.err.println("Cannot create: " + videoDstDirFile.getPath());
        System.exit(1);
      }
    }

    // Images
    System.out.println("\nCopying images:");
    System.out.println("From: " + imgSrcDirFile.getPath());
    System.out.println("To: " + imgDstDirFile.getPath());
    success = CopyUtils.copyDirectory(imgSrcDirFile, imgDstDirFile, saveSuffix);
    if(!success) {
      System.err.println("Copy failed:\n  From: " + imgSrcDirFile.getPath()
        + "\n  To: " + imgDstDirFile.getPath() + "\n");
    }

    // Video
    System.out.println("\nCopying video:");
    if(!doVideo) {
      System.out.println("No video found");
      return;
    }
    System.out.println("From: " + videoSrcDirFile.getPath());
    System.out.println("To: " + videoDstDirFile.getPath());
    success = CopyUtils.copyDirectory(videoSrcDirFile, videoDstDirFile,
      saveSuffix);
    if(!success) {
      System.err.println("Copy failed:\n  From: " + videoSrcDirFile.getPath()
        + "\n  To: " + videoDstDirFile.getPath() + "\n");
    }
  }

  /**
   * @param args There is one argument, the suffix, e.g. "012".
   */
  public static void main(String[] args) {
    Date start = new Date();
    System.out.println("CopyZIP");
    if(args.length != 1) {
      System.out.println("Must specify a suffix");
      System.exit(1);
    }
    String suffix = args[0];
    System.out.println("suffix is " + suffix);
    copy(suffix);

    Date end = new Date();
    double elapsed = (end.getTime() - start.getTime()) / 1000. / 60.;
    System.out.printf("\nElapsed: %.2f min\n", elapsed);
    System.out.println("\nAll done");
  }

}
