package Models;

import java.util.ArrayList;

/** Portfolio of investor */
public class Portfolio {
    private ArrayList<Bond> bonds = new ArrayList<>();
    void addBond(Bond toBeAdded){ bonds.add(toBeAdded); }
    public ArrayList<Bond> getBonds() { return bonds; }
}
