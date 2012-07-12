package misc;

import java.io.File;

public class GetFolderSize
{
  private static final String fileName = "R:";
  private static boolean VERBOSE = false;
  private static boolean COUNT_FILES = true;

  int totalFolder = 0;
  int totalFile = 0;

  public long getFileSize(File folder) {
    totalFolder++;
    if(VERBOSE) {
      System.out.println("Folder: " + folder.getName());
    }
    long foldersize = 0;

    File[] filelist = folder.listFiles();
    if(filelist == null) {
      System.out.println("Unable to list files for: " + folder.getPath());
      return 0;
    }
    for(int i = 0; i < filelist.length; i++) {
      if(filelist[i].isDirectory()) {
        foldersize += getFileSize(filelist[i]);
      } else {
        totalFile++;
        foldersize += filelist[i].length();
      }
    }
    return foldersize;
  }

  public int getTotalFolder() {
    return totalFolder;
  }

  public int getTotalFile() {
    return totalFile;
  }

  public static void printSpace(String name, long space) {
    System.out.printf("%s %d bytes, %.2f MB, %.2f GB %.2f TB\n", name, space,
      space / (1024. * 1024.), space / (1024. * 1024. * 1024.), space
        / (1024. * 1024. * 1024. * 1024.));
  }

  public static void main(String args[]) {
    GetFolderSize size = new GetFolderSize();
    File file = new File(fileName);
    System.out.println("Folder: " + file.getPath());
    if(!file.isDirectory()) {
      System.out.println("Not a directory: " + fileName);
    }
    // These need Java 1.6
    long totalSpace = file.getTotalSpace();
    long freeSpace = file.getFreeSpace();
    long usableSpace = file.getUsableSpace();
    long usedSpace = totalSpace - freeSpace;
    printSpace("Total space:", totalSpace);
    printSpace("Usable space:", usableSpace);
    printSpace("Free space:", freeSpace);
    printSpace("Used space:", usedSpace);
    if(COUNT_FILES) {
      // Can take a long time
      System.out.println("Counting total file sizes...");
      long fileSizeByte = size.getFileSize(file);
      printSpace("Counted file size", fileSizeByte);
      System.out.println("Total Number of Folders: " + size.getTotalFolder());
      System.out.println("Total Number of Files: " + size.getTotalFile());
    }
    System.out.println();
    System.out.println("All done");
  }

}
