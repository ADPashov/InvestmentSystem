package Models;

import java.util.ArrayList;

/** Current state of app, contains investors and market bonds */
public class State {
    private ArrayList<Investor> investors = new ArrayList<>();
    private ArrayList<Bond> bonds = new ArrayList<>();

    public ArrayList<Investor> getInvestors() { return this.investors; }
    public ArrayList<Bond> getBonds() { return this.bonds; }
    public void addInvestor(Investor inv) { this.investors.add(inv); }
    public void addBondToMarket(Bond bond) { this.bonds.add(bond); }
}
