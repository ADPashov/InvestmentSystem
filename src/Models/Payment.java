package Models;

import java.util.Date;
import java.util.UUID;

/** When a payment is executed (when a Bond has been bought) - a payment object is returned and saved into the bond,
 *  This ensures that the bond knows details about its buyers and payments */
public class Payment {
    private String id;
    private Bond bond;
    private double price;
    private Date date;
    private Investor investor;
    private double investAmount;

    Payment (Bond bond, Investor investor, double investAmount) {
        this.id = UUID.randomUUID().toString();
        this.date = new Date();
        this.bond = bond;
        this.price = bond.getPrice();
        this.investor = investor;
        this.investAmount = investAmount;
    }

    public double getAmountPaid() { return investAmount + price; }
    public Bond getBond() { return bond; }
    public Date getDate() { return date; }
    public double getPrice() { return price; }
    public Investor getInvestor() { return investor; }
    public String getId() { return id; }
    public double getInvestAmount() { return investAmount; }
}
