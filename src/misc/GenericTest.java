package misc;

public class GenericTest
{

  /**
   * Does nothing useful
   * 
   * @param dVals
   */
  public static void makeDVals(double[] dVals) {
    int nVals = 3;
    dVals = new double[nVals];
    ones(dVals);
    printDVals(dVals);
  }

  /**
   * Prints an array
   * @param dVals
   */
  public static void printDVals(double[] dVals) {
    int nVals = dVals.length;
    for(int i = 0; i < nVals; i++) {
      System.out.print(dVals[i] + " ");
    }
    System.out.println();
  }

  /**
   * Sets an array to all ones.
   * 
   * @param dVals
   */
  public static void ones(double[] dVals) {
    int nVals = dVals.length;
    for(int i = 0; i < nVals; i++) {
      dVals[i] = 1;
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // Demonstrate you can't create an array by passing it as a reference to
    // a method to create it. The reference, originally null, remains unchanged
    // (null).
    double[] dVals = null;
    makeDVals(dVals);
    System.out.println("dVals[Main]=" + dVals);

    dVals = new double[3];
    System.out.println("dVals[Main]=" + dVals);
    printDVals(dVals);
    makeDVals(dVals);
    System.out.println("dVals[Main]=" + dVals);
    printDVals(dVals);
    ones(dVals);
    System.out.println("dVals[Main]=" + dVals);
    printDVals(dVals);
  }

}
