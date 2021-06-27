package Models;

import javax.swing.table.DefaultTableModel;

/**
 * Table model for Market
 */
public class MarketBondsTableModel extends DefaultTableModel {
    private static MarketBondsTableModel marketBTM;

    private MarketBondsTableModel() {
        addColumn("Name");
        addColumn("Price");
        addColumn("Coupon (%)");
        addColumn("Frequency (per year)");
        addColumn("Inflation rate (%)");
        addColumn("Term (years)");
        addColumn("Date of purchase");
    }

    public static MarketBondsTableModel getInstance() {
        if (marketBTM == null) {
            marketBTM = new MarketBondsTableModel();
        }

        return marketBTM;
    }
}
