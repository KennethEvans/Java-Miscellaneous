package misc;

/*
 * Created on Dec 24, 2011
 * By Kenneth Evans, Jr.
 */

public class Test
{

    /**
     * @param args
     */
    public static void main(String[] args) {
        String string = "abc, def";
        int len = string.length();
        int index = string.indexOf(",");
        String string1 = string.substring(index+1, len);
        String string3 = string.substring(0,index);
        string1 = string1.trim();
        string3 = string3.trim();
        System.out.println("string: " + string);
        System.out.println("string1: " + string1);
        System.out.println("string3: " + string3);
    }

}
