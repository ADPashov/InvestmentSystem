package views;

import Models.Bond;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

/** A table that holds bonds as rows. To be used with different models */
public class BondsTable extends JTable {
    BondsTable(DefaultTableModel dtm) {
        super(dtm);
        setFillsViewportHeight(true);
        setBackground(Color.WHITE);
    }

    void addBond(Bond bond) {
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        model.addRow(new Object[]{
                bond,
                bond.getPrice(),
                bond.getCoupon(),
                Bond.DEFAULT_FREQUENCY,
                bond.getInflationRate(),
                bond.getTerm(),
                bond.getCreateDate(),
                new Date(), // date of
                bond.calculateAllPayouts(),
                bond.calculateBondValue(),
                bond.calculateMacaulayDuration(),
                bond.calculateIRR()
        });

        model.fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    @Override
    public int getRowHeight() {
        return super.getRowHeight() + 10;
    }
}
