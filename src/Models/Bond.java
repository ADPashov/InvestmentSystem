package Models;

import java.util.ArrayList;
import java.util.Date;

/** The Bond model. All the bond info is calculated on creation, but is shown at certain places for better UX. */
public class Bond {
    private static int lastID = 0;
    private int id;
    private String name; //name of the bond
    private double price; //price of the bond
    private int term; // number of years to expiry
    private double coupon; // percentage of investments paid regularly
    public static double DEFAULT_FREQUENCY = 1; // how many times a year, a coupon will be paid
    private Date createDate; //date when the bond was created
    private double inflation;
    private double value;
    private double allPayouts;
    private double macaulay;
    private double irr;
    private ArrayList<Payment> payments;

    public Bond(String nameIn, double priceIn, int termIn, double couponIn, double inflationIn, Date creationDate){
        name = nameIn;
        price = priceIn;
        term = termIn;
        coupon = couponIn/100; //Convert to double representation (5% gets saved as 0.05)
        inflation = inflationIn/100; //Convert to double representation (5% gets saved as 0.05)
        createDate = creationDate;
        calculateAllPayouts();
        calculateBondValue();
        calculateMacaulayDuration();
        calculateIRR();
        id = lastID++;
        payments = new ArrayList<>();
    }

    // Getters

    public int getId() { return id; }
    public int getTerm() { return term; }
    public double getCoupon() { return coupon*100; } /*Return coupon as user friendly representation */
    public double getInflationRate() { return inflation*100;} /*Return inflation as user friendly representation */
    public String getName() { return name; }
    public Date getCreateDate() { return createDate; }
    public double getPrice() { return price; }
    public double getAllPayouts() { return allPayouts; }
    public double getValue() { return value; }
    public double getMacaulay() { return macaulay; }
    public double getIrr() { return irr; }
    public ArrayList<Payment> getPayments() { return payments; }

    // Methods

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public double calculateAllPayouts(){
        return allPayouts = Investor.DEFAULT_INVESTED + (term * DEFAULT_FREQUENCY) * (Investor.DEFAULT_INVESTED * coupon);
    }
    
    private double calculatePaymentDiscount(double amount, double yearsPassed){ // calculating the discount for single payment
        return amount/Math.pow((1+inflation), yearsPassed);
    }
    
    private double calculateSumOfDiscountedPayoutsForMacaulay(int x){
        value = 0;
        for(int i = 1; i<=x; i++){
        	if(i == x) value += calculatePaymentDiscount(Investor.DEFAULT_INVESTED + Investor.DEFAULT_INVESTED * coupon, term) * i;
        	else value += calculatePaymentDiscount(Investor.DEFAULT_INVESTED * coupon, i) * i;
        }
        return value;
    }

    public double calculateBondValue(){ // calculating total bond value with discount
    	value = 0;
        for(int i = 1; i<=term; i++){
        	if(i == term) value += calculatePaymentDiscount(Investor.DEFAULT_INVESTED + Investor.DEFAULT_INVESTED * coupon, term);
        	else value += calculatePaymentDiscount(Investor.DEFAULT_INVESTED * coupon, i);
        }
        return Math.round(value * 1000.0)/1000.0;
    }

    public double calculateMacaulayDuration(){ //calculating Macaulay Duration, sum of discounted pay-outs
        double sum1 = calculateSumOfDiscountedPayoutsForMacaulay(term);
        double sum2 = calculateBondValue();
        double result = sum1/sum2;
        macaulay = result;
        return Math.round(result*1000.0)/1000.0;
    }

    public double calculateIRR(){ // calculating internal rate of return
        ArrayList<Double> payments = new ArrayList<Double>();
        payments.add(price * (-1.0));
        for(int i = 1; i <= term; i++){
        	if(i == term) payments.add(Investor.DEFAULT_INVESTED + Investor.DEFAULT_INVESTED * coupon);
        	else payments.add(Investor.DEFAULT_INVESTED * coupon);
        }

        double guess = 0.035; // will use guessing, the method will calculate the lefthand side of the IRR equation(which should be zero) and will ammend the guess, according the error(whats on the left)
        double error = 0.0;
        double approximation = 0.0001;
        boolean isPositiveOld = true;
        boolean isPositiveNew;
        double multiplier = 0.01;
        while(true){
            error = payments.get(0);

            for(int i = 1; i < payments.size(); i++){
                error += payments.get(i) / Math.pow(1 + guess, i);
            }

            isPositiveNew = error > 0;

            if(isPositiveNew != isPositiveOld){
                multiplier = multiplier / 10;
            }

            if(error > 0){
                guess += 1 * multiplier;
            } else {
                guess -= 1 * multiplier;
            }

            if(error > 0.0 - approximation && error < 0.0 + approximation){
                irr = guess;
                return Math.round(guess*100000.0)/100000.0;
            }

            isPositiveOld = isPositiveNew;
        }
    }

    @Override
    public String toString() { return this.name; }
}
