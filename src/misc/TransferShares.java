package misc;

import java.util.Hashtable;

/*
 * Created on Mar 16, 2011
 * By Kenneth Evans, Jr.
 */

public class TransferShares
{
    // // Prices 3-14-2011
    // public static Fund FDSCX = new Fund("FDSCX Fid Stk Sel Sm Cap", 4761.076,
    // 19.23);
    // public static Fund FDRXX = new Fund("FDRXX Fidelity Cash Res", 93585.03,
    // 1,
    // 43585.0329);
    // public static Fund FEXPX = new Fund("FEXPX Fid Exp & Multi", 5940.097,
    // 22.29, 132404.76); Shouldn't have target
    // public static Fund FDVLX = new Fund("FDVLX Fid Value", 3869.646, 71.53);
    // public static Fund FCNTX = new Fund("FCNTX Fid Contrafund", 4311.067,
    // 69.04, 100000.);
    //
    // public static Fund FLPSX = new Fund("FLPSX Fid Low-Price Stk", 0, 39.27,
    // 150000);
    // public static Fund FMILX = new Fund("FMILX Fid New Millenium", 0, 30.46,
    // 150000);
    // public static Fund FSCRX = new Fund("FSCRX Fid Sm Cap Disc", 0, 21.23,
    // 75000);
    // public static Fund FICDX = new Fund("FICDX Fid Canada", 0, 59.82, 75000);
    // public static Fund FVDFX = new Fund("FVDFX Fid Value Disc", 0, 15.21,
    // 99969.92);
    // public static Fund FTBFX = new Fund("FTBFX Fid Total Bond", 0, 10.80,
    // 90000);

    // // Prices 3-24-2011
    // public static Fund FDSCX = new Fund("FDSCX Fid Stk Sel Sm Cap", 4761.076,
    // 19.56);
    // public static Fund FDRXX = new Fund("FDRXX Fidelity Cash Res", 93585.03,
    // 1,
    // 43585.0329);
    // public static Fund FEXPX = new Fund("FEXPX Fid Exp & Multi", 5940.097,
    // 22.49);
    // public static Fund FDVLX = new Fund("FDVLX Fid Value", 3869.646, 72.48);
    // public static Fund FCNTX = new Fund("FCNTX Fid Contrafund", 4311.067,
    // 69.90, 100000.);
    //
    // public static Fund FLPSX = new Fund("FLPSX Fid Low-Price Stk", 0, 40.12,
    // 150000);
    // public static Fund FMILX = new Fund("FMILX Fid New Millenium", 0, 30.91,
    // 150000);
    // public static Fund FSCRX = new Fund("FSCRX Fid Sm Cap Disc", 0, 21.61,
    // 75000);
    // public static Fund FICDX = new Fund("FICDX Fid Canada", 0, 61.49, 75000);
    // public static Fund FVDFX = new Fund("FVDFX Fid Value Disc", 0, 15.36,
    // 99969.92);
    // public static Fund FTBFX = new Fund("FTBFX Fid Total Bond", 0, 10.76,
    // 90000);

    // Prices 3-29-2011
    public static Fund FDSCX = new Fund("FDSCX Fid Stk Sel Sm Cap", 4761.076,
        19.82);
    public static Fund FDRXX = new Fund("FDRXX Fidelity Cash Res", 93585.03, 1,
        43585.03);
    public static Fund FEXPX = new Fund("FEXPX Fid Exp & Multi", 5940.097,
        22.71);
    public static Fund FDVLX = new Fund("FDVLX Fid Value", 3869.646, 73.20);
    public static Fund FCNTX = new Fund("FCNTX Fid Contrafund", 4311.067,
        70.40, 100000.);

    public static Fund FLPSX = new Fund("FLPSX Fid Low-Price Stk", 0, 40.31,
        250000);
    // public static Fund FMILX = new Fund("FMILX Fid New Millenium", 0, 31.15,
    // 0);
    public static Fund FSCRX = new Fund("FSCRX Fid Sm Cap Disc", 0, 21.84,
        75000);
    public static Fund FICDX = new Fund("FICDX Fid Canada", 0, 61.29, 75000);
    // public static Fund FVDFX = new Fund("FVDFX Fid Value Disc", 0, 15.54,
    // 99969.92);
    public static Fund FTBFX = new Fund("FTBFX Fid Total Bond", 0, 10.73, 90000);
    public static Fund FUSEX = new Fund("FUSEX Fid Spartn 500 Idx", 0,
        46.88, 163528.12);

    TransferShares() {

    }

    public static class Fund
    {
        private String name;
        private double shares;
        private double price;
        private double target;
        private double sharesUsed;
        private Hashtable<Fund, Double> keyMap = new Hashtable<Fund, Double>();

        /**
         * Fund constructor that that assumes the target is the whole amount.
         * 
         * @param name
         * @param shares
         * @param price
         */
        public Fund(String name, double shares, double price) {
            // If no target is given, assume it is the entire amount
            this(name, shares, price, round3(shares * price));
        }

        /**
         * Fund constructor.
         * 
         * @param name
         * @param sharesUsed Number of sharesUsed.
         * @param price Price.
         * @param target Dollar amount to target.
         */
        public Fund(String name, double shares, double price, double target) {
            this.name = name;
            this.shares = shares;
            this.price = price;
            // Internally keep target in sharesUsed
            this.target = round3(target / price);
            sharesUsed = 0;
        }

        /**
         * @return The value of name.
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The new value for name.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The value of price.
         */
        public double getPrice() {
            return price;
        }

        /**
         * @param price The new value for price.
         */
        public void setPrice(double price) {
            this.price = price;
        }

        /**
         * @return The value of target.
         */
        public double getTarget() {
            return target;
        }

        /**
         * @param target The new value for target.
         */
        public void setTarget(double target) {
            this.target = target;
        }

        /**
         * @return The value of shares.
         */
        public double getShares() {
            return shares;
        }

        /**
         * @param sharesUsed The new value for shares.
         */
        public void setShares(double shares) {
            this.sharesUsed = shares;
        }

        /**
         * @return The value of keyMap.
         */
        public Hashtable<Fund, Double> getKeyMap() {
            return keyMap;
        }

        /**
         * @return The value of sharesUsed.
         */
        public double getSharesUsed() {
            return sharesUsed;
        }

        /**
         * @param sharesUsed The new value for sharesUsed.
         */
        public void setSharesUsed(double sharesUsed) {
            this.sharesUsed = sharesUsed;
        }

        /**
         * @return The value of the monetary value of the sharesUsed.
         */
        public double getValue() {
            return round2(shares * price);
        }

        /**
         * @return The sharesUsed left.
         */
        public double getSharesLeft() {
            return target - sharesUsed;
        }

    }

    /**
     * 
     * Rounds a number to 2 figures (for dollars)
     * 
     * @param x
     * @return
     */
    public static double round2(double x) {
        return Math.round(100. * x) / 100.;
    }

    /**
     * 
     * Rounds a number to 3 figures (for sharesUsed)
     * 
     * @param x
     * @return
     */
    public static double round3(double x) {
        return Math.round(1000. * x) / 1000.;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Fund[] src = {FDSCX, FDRXX, FEXPX, FDVLX, FCNTX};
        // Fund[] dst = {FLPSX, FMILX, FSCRX, FICDX, FVDFX, FTBFX};
        Fund[] dst = {FLPSX, FSCRX, FICDX, FTBFX, FUSEX};

        // Print out the Funds
        System.out.println("Sell (Target is how much to sell):");
        System.out.println("Fund                         "
            + "Shares  Price      Value     Target");
        for(Fund fund : src) {
            System.out.printf("%-24s %10.3f %6.2f %10.2f %10.2f\n",
                fund.getName(), fund.getShares(), fund.getPrice(),
                fund.getValue(), fund.getTarget() * fund.getPrice());
        }
        System.out.println();

        System.out.println("Buy (Target is how much to buy):");
        System.out.println("Fund                         "
            + "Shares  Price      Value     Target");
        for(Fund fund : dst) {
            System.out.printf("%-24s %10.3f %6.2f %10.2f %10.2f\n",
                fund.getName(), fund.getShares(), fund.getPrice(),
                fund.getValue(), fund.getTarget() * fund.getPrice());
        }
        System.out.println();

        // Find the total target available
        double total1 = 0;
        for(Fund fund1 : src) {
            total1 += round2(fund1.getTarget() * fund1.getPrice());
        }

        // Find the total target requested
        double total2 = 0;
        for(Fund fund2 : dst) {
            total2 += round2(fund2.getTarget() * fund2.getPrice());
        }
        System.out.println("Requesting a total of $" + round2(total2)
            + " from $" + round2(total1) + " available");
        System.out.println();

        // 1 = source, 2 = dest
        // Used and left are in dollars
        double left1, left2, used, sharesUsed1, sharesUsed2;
        double total = 0;
        String all;
        String msg;
        for(Fund fund2 : dst) {
            for(Fund fund1 : src) {
                left1 = round2(fund1.getSharesLeft() * fund1.getPrice());
                left2 = round2(fund2.getSharesLeft() * fund2.getPrice());
                if(left2 <= 0) {
                    // This dst is done
                    break;
                }
                if(left1 <= 0) {
                    // Try the next src
                    continue;
                }
                if(left1 >= left2) {
                    used = left2;
                } else {
                    used = left1;
                }
                total += used;
                sharesUsed1 = round3(used / fund1.getPrice());
                sharesUsed2 = round3(used / fund2.getPrice());
                fund1.setSharesUsed(fund1.getSharesUsed() + sharesUsed1);
                fund2.setSharesUsed(fund2.getSharesUsed() + sharesUsed2);
                if(round3(fund1.getSharesLeft()) <= 0) {
                    all = " [All]";
                } else {
                    all = "";
                }
                msg = "Use " + sharesUsed1 + all + " shares from \""
                    + fund1.getName() + "\" for \"" + fund2.getName() + "\" ($"
                    + used + ") [" + round3(fund1.getSharesLeft())
                    + " left to use]";
                System.out.println(msg);
            }
        }

        // Print total used
        System.out.println("Total funds used: $" + total);

        // Check balances in src
        double bal, targetBalance;
        System.out.println();
        for(Fund fund1 : src) {
            bal = round3(fund1.getShares() - fund1.getSharesUsed());
            targetBalance = round3(fund1.getTarget() - fund1.getSharesUsed());
            msg = "Left in \"" + fund1.getName() + "\" " + targetBalance
                + " shares left of target, " + bal + " shares left ($"
                + round2(bal * fund1.getPrice()) + ")";
            System.out.println(msg);
        }

        // Check balances in dest
        System.out.println();
        for(Fund fund2 : dst) {
            bal = -round3(fund2.getShares() - fund2.getSharesUsed());
            targetBalance = round3(fund2.getTarget() - fund2.getSharesUsed());
            msg = "Bought in \"" + fund2.getName() + "\" " + bal + " shares ($"
                + round2(bal * fund2.getPrice()) + ")";
            System.out.println(msg);
        }

        // Check dollars left in dest
        System.out.println();
        for(Fund fund2 : dst) {
            msg = "Not bought in \"" + fund2.getName() + "\" "
                + round3(fund2.getSharesLeft() * fund2.getPrice()) + " dollars";
            System.out.println(msg);
        }

    }
}
