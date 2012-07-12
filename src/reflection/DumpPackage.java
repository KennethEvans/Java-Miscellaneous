package reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Created on Nov 23, 2010
 * By Kenneth Evans, Jr.
 */

public class DumpPackage
{
    private static final String[] DEFAULT_PACKAGES = {"net.kenevans.gpx"};

    // private static final String DEFAULT_DIR =
    // "C:/eclipseWorkspaces/Work/net.kenevans.gpx/bin";

    public static void findClasses(String args[]) {
        String[] packages = DEFAULT_PACKAGES;
        Class<?>[] classes = null;
        String output;
        String packageName;
        String declaringClass;
        List<String> list = null;
        try {
            if(args.length > 0) {
                packages = args;
            }
            for(String string : packages) {
                System.out.println("Package " + string + ":\n");
                classes = ReflectionUtils.getClasses(string);
                if(classes.length == 0) {
                    System.out.println("No classes found\n");
                    continue;
                }
                for(Class<?> c : classes) {
                    System.out.println(c.getName() + ":");
                    // Fields
                    System.out.println("Fields:");
                    Field fields[] = c.getDeclaredFields();
                    list = new ArrayList<String>();
                    for(Field field : fields) {
                        packageName = c.getPackage().toString().substring(8);
                        declaringClass = field.getDeclaringClass().toString()
                            .substring(6);
                        output = field.toString()
                            .replaceAll(declaringClass + ".", "")
                            .replaceAll(packageName + ".", "")
                            .replaceAll("java\\.lang\\.", "");
                        output = String.format("%-20s ", field.getName())
                            + output;
                        list.add(output);
                    }
                    Collections.sort(list);
                    for(String item : list) {
                        System.out.println(item);
                    }
                    // Methods
                    System.out.println("Methods:");
                    Method methods[] = c.getDeclaredMethods();
                    list = new ArrayList<String>();
                    for(Method method : methods) {
                        packageName = c.getPackage().toString().substring(8);
                        declaringClass = method.getDeclaringClass().toString()
                            .substring(6);
                        output = method.toString()
                            .replaceAll(declaringClass + ".", "")
                            .replaceAll(packageName + ".", "")
                            .replaceAll("java\\.lang\\.", "");
                        output = String.format("%-20s ", method.getName())
                            + output;
                        list.add(output);
                    }
                    Collections.sort(list);
                    for(String item : list) {
                        System.out.println(item);
                    }
                    System.out.println();
                }
                System.out.println();
            }
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        // DEBUG
        if(true) {
            System.out.println(System.getProperty("java.class.path"));
        }

        findClasses(args);

        System.out.println("\nAll Done");
    }

}
