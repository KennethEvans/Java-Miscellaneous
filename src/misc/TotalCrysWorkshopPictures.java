package misc;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * TotalCrysWorkshopPictures renumbers the images in the given directory after
 * sorting them in alphabetical order. Note there is a problem after ordering
 * images by changing their numbers in that the new order may clobber the image
 * in that position. You may have to allow the block that renames to another
 * suffix below. Names are hardcoded.
 * 
 * @author Kenneth Evans, Jr.
 */
public class TotalCrysWorkshopPictures
{
  private static final boolean dryRun = true;
  private static final String mainDir = "c:/users/evans/Pictures/Digital Photos/ESRF April 2009/TCWS";
  private Comparator<File> fileComparator = null;

  /**
   * PrintTree constructor.
   */
  public TotalCrysWorkshopPictures() {
    fileComparator = new Comparator<File>() {
      public int compare(File fa, File fb) {
        if(true) {
          if(fa.isDirectory() && !fb.isDirectory()) return -1;
          if(fb.isDirectory() && !fa.isDirectory()) return 1;
        } else if(true) {
          if(fa.isDirectory() && !fb.isDirectory()) return 1;
          if(fb.isDirectory() && !fa.isDirectory()) return -1;
        }
        return (fa.getName().compareTo(fb.getName()));
      }
    };
  }

  /**
   * The main routine. Renames the files after sorting them in their original
   * order.
   */
  public void reorder() {
    File dir = new File(mainDir);
    if(dir == null) {
      System.err.println("Invalid directory");
      System.exit(1);
    }
    if(!dir.isDirectory()) {
      System.err.println("Not a directory");
      System.exit(1);
    }
    File dirList[] = dir.listFiles();
    // Convert it to a list so we can sort it
    List<File> list = Arrays.asList(dirList);
    // Sort it
    Collections.sort(list, fileComparator);

    // 
    String dirname = dir.getPath() + File.separator;
    String newName;
    File newFile;
    // Fix problems renumbering and clobbering existing names
    int i;
    ListIterator<File> iter;
    if(false) {
      i = 1;
      iter = list.listIterator();
      while(iter.hasNext()) {
        File file1 = (File)iter.next();
        newName = dirname + String.format("ATCWS_%04d.jpg", 10 * i++);
        newFile = new File(newName);
        if(!dryRun) {
          // System.out.println(newFile.getPath());
          file1.renameTo(newFile);
        }
      }
    }
    // Do the renaming
    i = 1;
    iter = list.listIterator();
    while(iter.hasNext()) {
      File file1 = (File)iter.next();
      newName = dirname + String.format("TCWS_%04d.jpg", 10 * i++);
      newFile = new File(newName);
      if(!dryRun) {
        System.out.println(newFile.getPath());
        file1.renameTo(newFile);
      }
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    TotalCrysWorkshopPictures app = new TotalCrysWorkshopPictures();
    app.reorder();
    System.out.println("All done");
  }

}
