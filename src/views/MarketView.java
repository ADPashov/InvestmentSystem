package views;

import Models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Date;

/** View for bonds */
class MarketView extends JPanel {
    private final BondsTable bondsTable;
    private final State db;

    MarketView(Investor currentInvestor, PortfolioView currentInvestorPortfolioView, State db) {
        this.bondsTable = new BondsTable(MarketBondsTableModel.getInstance());
        for (Bond bond : db.getBonds()) {
            bondsTable.addBond(bond);
        }

        this.db = db;

        this.bondsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // double click or more, maybe there's a better way but fuck it
                if (e.getClickCount() >= 2 && bondsTable.rowAtPoint(e.getPoint()) >= 0) {
                    Bond bondToBePurchased = (Bond) bondsTable.getModel().getValueAt(bondsTable.rowAtPoint(e.getPoint()), 0);

                    // init dialog
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Are you sure?");
                    dialog.setSize(400, 450);
                    dialog.setLocationRelativeTo(null);
                    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                    // add info

                    dialog.setLayout(new BorderLayout());

                    JPanel infoPanel = new JPanel();
                    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

                    DecimalFormat df = new DecimalFormat("#0.000");

                    JLabel areUSureLbl = new JLabel("Are you sure you want to buy [" + bondToBePurchased.getName() + "]?");
                    areUSureLbl.setFont(new Font(areUSureLbl.getFont().getName(), Font.PLAIN, 16));
                    JLabel payoutLbl = new JLabel("Payout at term:");
                    JTextField payoutJtf = new JTextField(String.valueOf(bondToBePurchased.getAllPayouts()));
                    JLabel valueLbl = new JLabel("Value:");
                    JTextField valueJtf = new JTextField(String.valueOf(df.format(bondToBePurchased.getValue())));
                    JLabel macaulayLbl = new JLabel("Macaulay duration:");
                    JTextField macaulayJtf = new JTextField(String.valueOf(df.format(bondToBePurchased.getMacaulay())));
                    JLabel irrLbl = new JLabel("Internal rate of return:");
                    JTextField irrJtf = new JTextField(String.valueOf(df.format(bondToBePurchased.getIrr())));
                    JLabel countLbl = new JLabel("This bond has been bought:");
                    JTextField countJtf = new JTextField(String.valueOf(bondToBePurchased.getPayments().size() + " times"));
                    JLabel payLbl = new JLabel("Investment amount is £100. Total amount to pay: ");
                    JTextField payJtf = new JTextField("£" + (bondToBePurchased.getPrice() + 100));

                    // disable fields
                    payoutJtf.setEnabled(false);
                    valueJtf.setEnabled(false);
                    macaulayJtf.setEnabled(false);
                    irrJtf.setEnabled(false);
                    countJtf.setEnabled(false);
                    payJtf.setEditable(false);

                    infoPanel.add(Box.createRigidArea(new Dimension(1,20)));
                    infoPanel.add(areUSureLbl);
                    infoPanel.add(Box.createRigidArea(new Dimension(1,20)));
                    infoPanel.add(payoutLbl);
                    infoPanel.add(payoutJtf);
                    infoPanel.add(valueLbl);
                    infoPanel.add(valueJtf);
                    infoPanel.add(macaulayLbl);
                    infoPanel.add(macaulayJtf);
                    infoPanel.add(irrLbl);
                    infoPanel.add(irrJtf);
                    infoPanel.add(countLbl);
                    infoPanel.add(countJtf);
                    infoPanel.add(Box.createRigidArea(new Dimension(1,20)));
                    infoPanel.add(payLbl);
                    infoPanel.add(payJtf);
                    infoPanel.add(Box.createRigidArea(new Dimension(1,20)));

                    dialog.add(infoPanel, BorderLayout.NORTH);

                    JButton yesBtn = new JButton("Buy");
                    yesBtn.addActionListener(e2 -> {
                        Payment payment = currentInvestor.purchaseBond(bondToBePurchased);
                        currentInvestorPortfolioView.addBond(bondToBePurchased);
                        bondToBePurchased.addPayment(payment);
                        dialog.setVisible(false);
                    });

                    JButton noBtn = new JButton("Cancel");
                    noBtn.addActionListener(e2 -> dialog.setVisible(false));

                    JPanel yesNoPanel = new JPanel(new FlowLayout());
                    yesNoPanel.add(yesBtn);
                    yesNoPanel.add(noBtn);
                    dialog.add(yesNoPanel, BorderLayout.CENTER);

                    // "turn on"
                    dialog.setVisible(true);
                }
            }
        });

        JScrollPane bondsScrollPane  = new JScrollPane(bondsTable);

        // button for new bond
        JButton addBondBtn = createAddBtn();
        JPanel btnHolder = new JPanel(new FlowLayout());
        btnHolder.add(addBondBtn);

        // add everything to bonds view
        this.setLayout(new BorderLayout());
        this.add(new JLabel("Bonds"), BorderLayout.NORTH);
        this.add(btnHolder, BorderLayout.NORTH);

        JLabel info = new JLabel("Note: double click on a bond to buy it.");
        info.setForeground(Color.RED);
        JPanel center = new JPanel(new BorderLayout());
        center.add(info, BorderLayout.NORTH);
        center.add(bondsScrollPane, BorderLayout.CENTER);
        this.add(center, BorderLayout.CENTER);

    }

    /**
     * Creates a button that opens the Create Bond modal
     * @return button that opens the Create Bond modal
     */
    private JButton createAddBtn() {
        JButton createBondBtn = new JButton("Create Bond");
        createBondBtn.setOpaque(true);
        createBondBtn.setForeground(Color.RED);
        createBondBtn.setPreferredSize(new Dimension(200, 50));

        createBondBtn.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("Create a bond");
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            dialog.setLayout(new BorderLayout());
            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
            dialog.add(innerPanel, BorderLayout.CENTER);

            // fields with labels
            JLabel nameLbl = new JLabel("Name");
            JTextField name = new JTextField();
            JLabel priceLbl = new JLabel("Price");
            JTextField price = new JTextField();
            JLabel termLbl = new JLabel("Term");
            JTextField term = new JTextField();
            JLabel couponLbl = new JLabel("Coupon (%)");
            JTextField coupon = new JTextField();
            JLabel inflLbl = new JLabel("Inflation rate (%)");
            JTextField infl = new JTextField();

            // bottom panel
            JPanel addCancelPanel = new JPanel(new FlowLayout());

            // add button
            JButton addBtn = new JButton("Add");
            addBtn.addActionListener(e2 -> {
                Bond bond = new Bond(
                        name.getText(),
                        Double.parseDouble(price.getText()),
                        Integer.parseInt(term.getText()),
                        Double.parseDouble(coupon.getText()),
                        Double.parseDouble(infl.getText()),
                        new Date()
                );

                db.addBondToMarket(bond);
                bondsTable.addBond(bond);
                dialog.setVisible(false);
            });

            // cancel button
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(e2 -> dialog.setVisible(false));

            addCancelPanel.add(addBtn);
            addCancelPanel.add(cancel);

            innerPanel.add(nameLbl);
            innerPanel.add(name);
            innerPanel.add(priceLbl);
            innerPanel.add(price);
            innerPanel.add(termLbl);
            innerPanel.add(term);
            innerPanel.add(couponLbl);
            innerPanel.add(coupon);
            innerPanel.add(inflLbl);
            innerPanel.add(infl);

            dialog.add(addCancelPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
            dialog.pack();
        });

        return createBondBtn;
    }
}
