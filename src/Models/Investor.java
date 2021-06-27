package Models;

/** The class will represent the investor's profile */
public class Investor {
    public static double DEFAULT_INVESTED = 100;

    private double wallet; //will represent the Investor's money, we will take money, when he buys bond, we will give money when paying
    private String name;
    private Portfolio portfolio;

    public Investor(String nameIn, double walletIn){ //2nd constructor: where we provide name and wallet amount, portfolio is initialised
        name = nameIn;
        wallet = walletIn;
        portfolio = new Portfolio();
    }

    private void pay(double amount) { this.wallet -= amount; }

    public Portfolio getPortfolio() { return portfolio; }

    public Payment purchaseBond(Bond bond) {
        if (this.wallet >= bond.getPrice()) {
            Payment payment = new Payment(bond, this, Investor.DEFAULT_INVESTED);
            this.portfolio.addBond(bond);
            bond.addPayment(payment);

            this.pay(payment.getAmountPaid());
            return payment;
        }

        return null;
    }

    @Override
    public String toString() {
        return this.name + " [£" + this.wallet + "]";
    }
}
