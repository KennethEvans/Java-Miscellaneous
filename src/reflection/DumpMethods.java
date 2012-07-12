package reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Created on Nov 23, 2010
 * By Kenneth Evans, Jr.
 */

public class DumpMethods
{
    // private static final String DEFAULT_CLASS =
    // "net.kenevans.gpxinspector.utils.SWTUtils";
    // private static final String DEFAULT_CLASS =
    // "net.kenevans.gpxinspector.utils.RainbowColorScheme";
    // private static final String DEFAULT_CLASS =
    // "net.kenevans.gpxinspector.utils.Utils";
    private static final String[] DEFAULT_CLASSES = {
        "net.kenevans.gpx.GpxType", "net.kenevans.gpx.TrkType",
        "net.kenevans.gpx.RteType", "net.kenevans.gpx.MetadataType",
        "net.kenevans.gpx.PersonType", "net.kenevans.gpx.EmailType",
        "net.kenevans.gpx.LinkType", "net.kenevans.gpx.ExtensionsType",};

    public static void main(String args[]) {
        // DEBUG
        if(true) {
            System.out.println(System.getProperty("java.class.path"));
        }
        String[] classes = DEFAULT_CLASSES;
        String output;
        String packageName;
        String declaringClass;
        Class<?> c = null;
        try {
            if(args.length > 0) {
                classes = args;
            }
            List<String> list = null;
            for(String string : classes) {
                System.out.println(string + ":");
                c = Class.forName(string);
                Method methods[] = c.getDeclaredMethods();
                // Make a list
                list = new ArrayList<String>();
                for(Method method : methods) {
                    packageName = c.getPackage().toString().substring(8);
                    declaringClass = method.getDeclaringClass().toString()
                        .substring(6);
                    output = method.toString()
                        .replaceAll(declaringClass + ".", "")
                        .replaceAll(packageName + ".", "")
                        .replaceAll("java\\.lang\\.", "");
                    output = String.format("%-20s ", method.getName()) + output;
                    list.add(output);
                }
                Collections.sort(list);
                for(String item : list) {
                    System.out.println(item);
                }
                System.out.println();
            }
        } catch(Throwable ex) {
            ex.printStackTrace();
        }

        System.out.println("\nAll Done");
    }

}
