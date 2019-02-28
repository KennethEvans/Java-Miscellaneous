package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/*
 * Created on Jan 28, 2019
 * By Kenneth Evans, Jr.
 */

public class Base64Encode
{

  public static void main(String[] args) {
    BufferedReader reader = new BufferedReader(
      new InputStreamReader(System.in));
    System.out.print("Enter string to encode : ");
    String input = null;
    try {
      input = reader.readLine();
    } catch(IOException e) {
      e.printStackTrace();
    }

    String encodedString = Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    // String encodedString = new String(
    // Base64.getEncoder().encode(input.getBytes()));

    System.out.println(encodedString);
  }

}
