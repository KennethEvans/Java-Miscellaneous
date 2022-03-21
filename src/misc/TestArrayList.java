package misc;

import java.util.Arrays;
import java.util.List;

/*
 * Created on Dec 30, 2021
 * By Kenneth Evans, Jr.
 */

public class TestArrayList
{
    private static void printList(List<Integer> list) {
        String info = "";
        for (int i : list) {
            info += i + ",";
        }
        System.out.println(info);
    }
    
    private static void printArray(Integer[] array) {
        String info = "";
        for (int i : array) {
            info += i + ",";
        }
        System.out.println(info);
    }

    public static void main(String[] args) {
        Integer[] array = new Integer[] {1,2,3,4,5};
        List<Integer> list = Arrays.asList(array);
        printList(list);
        // Change an element in the array
        array[0] = 0;
        printList(list);
        // Change an element in the list
        list.set(0,  10);
        printArray(array);
    }
    
}
