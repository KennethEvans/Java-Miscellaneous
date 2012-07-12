package misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * CopyUtils. Basic file copy routines based on those in VisAD. The progress
 * monitor is not implemented.
 * 
 * @author Kenneth Evans, Jr.
 */
public class CopyUtils
{
  /**
   * Copy files under the <i>source</i> directory to the <i>target</i>
   * directory. If necessary, <i>target</i> directory is created.<br>
   * <br>
   * For example, if this method is called with a <i>source</i> of <tt>/foo</tt>
   * (which contains <tt>/foo/a</tt> and <tt>/foo/b</tt>) and a <i>target</i> of
   * <tt>/bar</tt>, when this method exits <tt>/bar</tt> will contain
   * <tt>/bar/a</tt> and <tt>/bar/b</tt>. Note that <tt>foo</tt> itself is not
   * copied.
   * 
   * @param source source directory
   * @param target directory
   * @param saveSuffix if non-null, pre-existing files under <i>target</i> whose
   *          paths match files to be copied from <i>source</i> will be renamed
   *          to <tt>name + saveSuffix</tt>.
   * 
   * @return false if any problems were encountered.
   */
  public static final boolean copyDirectory(File source, File target,
    String saveSuffix) {
    // source must be a directory
    if(!source.isDirectory() || (target.exists() && !target.isDirectory())) {
      return false;
    }

    // if source and target are the same, we're done
    if(getPath(source).equals(getPath(target))) {
      return false;
    }

    // if the target doesn't exist yet, create it
    if(!target.exists()) {
      target.mkdirs();
    }

    boolean result = true;

    String[] list = source.list();
    for(int i = 0; i < list.length; i++) {
      File srcFile = new File(source, list[i]);
      File tgtFile = new File(target, list[i]);

      if(srcFile.isDirectory()) {
        result |= copyDirectory(srcFile, tgtFile, saveSuffix);
      } else {
        result |= copyFile(srcFile, tgtFile, saveSuffix);
      }
    }

    // if source was read-only, the target should be as well
    if(!source.canWrite()) {
      target.setReadOnly();
    }

    // sync up last-modified time
    target.setLastModified(source.lastModified());

    return result;
  }

  /**
   * Copy the <i>source</i> file to <i>target</i>. If <i>target</i> does not
   * exist, it is assumed to be the name of the copied file. If <i>target</i> is
   * a directory, the file will be copied into that directory.
   * 
   * @param source source directory
   * @param target target file/directory
   * @param saveSuffix if non-null and <i>target</i> exists, <i>target</i> will
   *          be renamed to <tt>name + saveSuffix</tt>.
   * 
   * @return false if any problems were encountered.
   */
  public static final boolean copyFile(File source, File target,
    String saveSuffix) {
    // don't copy directories
    if(source.isDirectory()) {
      return false;
    }

    // if source and target are the same, we're done
    if(getPath(source).equals(getPath(target))) {
      return false;
    }

    if(target.isDirectory()) {
      target = new File(target, source.getName());
    }

    FileInputStream in;
    try {
      in = new FileInputStream(source);
    } catch(IOException ioe) {
      System.err.println("Couldn't open source file " + source);
      return false;
    }

    copyStreamToFile(in, target, saveSuffix);

    try {
      in.close();
    } catch(Exception e) {
      ;
    }

    // if source was read-only, the target should be as well
    if(!source.canWrite()) {
      target.setReadOnly();
    }

    // sync up last-modified time
    target.setLastModified(source.lastModified());

    return true;
  }

  /**
   * Low level routine that does the actual copying.
   * 
   * @param in Input stream.
   * @param targetn Target file.
   * @param saveSuffix if non-null and <i>target</i> exists, <i>target</i> will
   *          be renamed to <tt>name + saveSuffix</tt>.
   * @return Whether successful or not.
   */
  private static final boolean copyStreamToFile(InputStream in, File target,
    String saveSuffix) {
    // if the target already exists and we need to save the existing file...
    if(target.exists()) {
      if(saveSuffix == null) {

        // out with the old...
        target.delete();
      } else {

        File saveFile = new File(target.getPath() + saveSuffix);

        // delete the old savefile
        if(saveFile.exists()) {
          saveFile.delete();
        }

        // save the existing target file
        target.renameTo(saveFile);
      }
    }

    FileOutputStream out;
    try {
      out = new FileOutputStream(target);
    } catch(IOException ioe) {
      System.err.println("Couldn't open output file " + target);
      return false;
    }

    byte buffer[] = new byte[1024];
    try {
      while(true) {
        int n = in.read(buffer);

        if(n < 0) {
          break;
        }

        out.write(buffer, 0, n);
      }
    } catch(IOException ioe) {
      ioe.printStackTrace();
      return false;
    } finally {
      try {
        out.close();
      } catch(Exception e) {
        ;
      }
    }

    return true;
  }

  /**
   * Gets the path for a File.
   * 
   * @return Either the canonical path or, if that is not available, the
   *         absolute path.
   */
  public static final String getPath(File f) {
    try {
      return f.getCanonicalPath();
    } catch(IOException ioe) {
      return f.getAbsolutePath();
    }
  }

}
