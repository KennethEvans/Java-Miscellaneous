package misc;

import java.io.Closeable;
import java.io.IOException;

/*
 * Created on Dec 8, 2020
 * By Kenneth Evans, Jr.
 */

public class TryWithResourcesTest
{
    public static void main(String[] args) {
        System.out.println("Trying Test1");
        try {
            try (Test1 test = new Test1()) {
                System.out.println("Running Test1");
            }
        } catch(Exception ex) {
            System.out.println("Got exception: " + ex.getMessage());
        }

        System.out.println();
        System.out.println("Trying Test2");
        try {
            try (Test2 test = new Test2()) {
                System.out.println("Running Test2");
            }
        } catch(Exception ex) {
            System.out.println("Got exception: " + ex.getMessage());
        }

        System.out.println();
        System.out.println("Trying Test3");
        try {
            try (Test3 test = new Test3()) {
                System.out.println("Running Test3");
            }
        } catch(Exception ex) {
            System.out.println("Got exception: " + ex.getMessage());
        }

        System.out.println();
        System.out.println("Trying Test4");
        try {
            try (Test4 test = new Test4()) {
                System.out.println("Running Test2");
            }
        } catch(Exception ex) {
            System.out.println("Got exception: " + ex.getMessage());
        }
        
        System.out.println();
        System.out.println("All done");
    }

    public static class Test1 implements Closeable
    {
        public Test1() {
            // throw new UnsupportedOperationException("Exception from Test1
            // CTOR");
        }

        public void close() throws IOException {
            throw new IOException("IOException for Test1.close");
        }
    }

    public static class Test2 implements AutoCloseable
    {
        public Test2() {
            // throw new UnsupportedOperationException("Exception from Test2
            // CTOR");
        }

        @Override
        public void close() throws Exception {
            throw new NullPointerException(
                "NullPointerException for Test2.close");
        }
    }

    public static class Test3 implements Closeable
    {
        public Test3() {
            throw new UnsupportedOperationException(
                "Exception from Test3 CTOR");
        }

        public void close() throws IOException {
            throw new IOException("IOException for Test3.close");
        }
    }

    public static class Test4 implements AutoCloseable
    {
        public Test4() {
            throw new UnsupportedOperationException(
                "Exception from Test4 CTOR");
        }

        @Override
        public void close() throws Exception {
            throw new NullPointerException(
                "NullPointerException for Test4.close");
        }
    }

}
