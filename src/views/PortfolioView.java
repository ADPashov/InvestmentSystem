package views;

import Models.Bond;
import Models.Investor;
import Models.PortfolioBondsTableModel;

import javax.swing.*;
import java.awt.*;

/** Portfolio of an investor */
class PortfolioView extends JPanel {
    private final BondsTable bondsTable;
    private final JScrollPane scrollpane;

    PortfolioView(Investor investor) {
        PortfolioBondsTableModel dtm = PortfolioBondsTableModel.getInstance();

        bondsTable = new BondsTable(dtm);

        // add bonds
        for (Bond bond : investor.getPortfolio().getBonds()) {
            bondsTable.addBond(bond);
        }

        scrollpane  = new JScrollPane(bondsTable);

        this.setLayout(new BorderLayout());
        this.add(new JLabel("Bonds"), BorderLayout.NORTH);
        this.add(scrollpane, BorderLayout.CENTER);
    }

    void addBond(Bond bond) {
        this.bondsTable.addBond(bond);
    }
}
