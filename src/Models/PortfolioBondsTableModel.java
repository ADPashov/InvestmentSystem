package Models;

import javax.swing.table.DefaultTableModel;

/** Table model for Portfolio */
public class PortfolioBondsTableModel extends DefaultTableModel {
    private static PortfolioBondsTableModel portfolioBTM;

    private PortfolioBondsTableModel() {
        addColumn("Name");
        addColumn("Price");
        addColumn("Coupon (%)");
        addColumn("Frequency (per year)");
        addColumn("Inflation rate (%)");
        addColumn("Term (years)");
        addColumn("Creation date");
        addColumn("Purchase date");
        addColumn("Payout at term");
        addColumn("Value");
        addColumn("Macaulay duration");
        addColumn("Internal rate of return");
    }

    public static PortfolioBondsTableModel getInstance() {
        if (portfolioBTM == null) {
            portfolioBTM = new PortfolioBondsTableModel();
        }

        return portfolioBTM;
    }
}
