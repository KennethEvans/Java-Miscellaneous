package misc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * Created on Aug 11, 2019
 * By Kenneth Evans, Jr.
 */

public class ListFilesByDateModified
{
    private static final String DIR = "C:/Users/evans/Pictures/My Art";

    private static void listFiles() {
        File dir = new File(DIR);
        File[] files = dir.listFiles();
        List<File> fileList = new ArrayList<File>();
        for(File file : files) {
            if(file.isDirectory()) continue;
            fileList.add(file);
        }
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return Long.compare(o1.lastModified(), o2.lastModified());
            }
        });
        for(File file : fileList) {
            System.out.println(file.getName());
        }

    }

    public static void main(String[] args) {
        listFiles();
    }

}
