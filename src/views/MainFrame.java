package views;

import Models.Investor;
import Models.State;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/** Main frame of the app */
public class MainFrame extends JFrame {
    private State db;
    private Investor currentInvestor;

    public MainFrame() {
        setTitle("Simple example");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // init all panels and components
        JPanel bodyPanel = new JPanel(new BorderLayout());
        DefaultComboBoxModel<Investor> dcbm = new DefaultComboBoxModel<>();
        JPanel investorPanel = new JPanel();
        JPanel bondsPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        JComboBox<Investor> investorSelectorDropdown = new JComboBox<>(dcbm);
        JButton newInvestorBtn = createNewInvestorButton(dcbm);

        // manage state/db and hardcode test investor
        db = new State();
        Investor testInvestor = new Investor("Initial investor", 3000);
        db.addInvestor(testInvestor);
        currentInvestor = db.getInvestors().get(0);

        // when investor is changed...
        investorSelectorDropdown.addActionListener(e -> {
            JComboBox cb = (JComboBox)e.getSource();
            currentInvestor = (Investor)cb.getSelectedItem();

            refreshTabbedPane(tabbedPane);
        });

        // init dropdown
        refreshInvestorsInComboBox(dcbm);

        // new investor button
        investorPanel.add(newInvestorBtn, BorderLayout.NORTH);
        investorPanel.add(new JLabel("Choose investor"), BorderLayout.NORTH);
        investorPanel.add(investorSelectorDropdown);
        bodyPanel.add(investorPanel, BorderLayout.NORTH);

        // init portfolio and market
        refreshTabbedPane(tabbedPane);

        // add stuff
        this.add(bodyPanel, BorderLayout.CENTER);
        bodyPanel.add(bondsPanel, BorderLayout.CENTER);
        bondsPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    /** Creates new investor button */
    private JButton createNewInvestorButton(DefaultComboBoxModel<Investor> dcbm) {
        JButton newInvestorBtn = new JButton("New investor");
        newInvestorBtn.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("Create new investor");
            dialog.setSize(300, 150);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setLayout(new BorderLayout());

            // inputs
            JPanel inputHolder = new JPanel();
            inputHolder.setLayout(new BoxLayout(inputHolder, BoxLayout.Y_AXIS));

            JLabel nameLbl = new JLabel("Name:");
            final JTextField nameTF = new JTextField();
            JLabel walletLbl = new JLabel("Initial money:");
            JTextField walletTF = new JTextField();

            inputHolder.add(nameLbl);
            inputHolder.add(nameTF);
            inputHolder.add(walletLbl);
            inputHolder.add(walletTF);

            // Ok / cancel
            JButton okBtn = new JButton("Create");
            okBtn.addActionListener(e2 -> {
                Investor newInvestor = new Investor(nameTF.getText(), Double.parseDouble(walletTF.getText()));
                db.addInvestor(newInvestor);
                refreshInvestorsInComboBox(dcbm);
                dialog.setVisible(false);
            });
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.addActionListener(e2 -> dialog.setVisible(false));

            // add inputs
            dialog.add(inputHolder, BorderLayout.NORTH);

            // add controls
            JPanel bottomPanel = new JPanel(new FlowLayout());
            bottomPanel.add(okBtn);
            bottomPanel.add(cancelBtn);
            dialog.add(bottomPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
        });

        return newInvestorBtn;
    }

    private void refreshTabbedPane(JTabbedPane tabbedPane) {
        PortfolioView portfolioView = new PortfolioView(currentInvestor);
        MarketView marketView = new MarketView(currentInvestor, portfolioView, db);

        tabbedPane.removeAll();
        tabbedPane.addTab("Market", marketView);
        tabbedPane.addTab("Portfolio", portfolioView);
    }

    private void refreshInvestorsInComboBox(DefaultComboBoxModel<Investor> dcbm) {
        ArrayList<Investor> investors = db.getInvestors();

        for (Investor inv : investors) {
            if (dcbm.getIndexOf(inv) == -1) {
                dcbm.addElement(inv);
            }
        }
    }
}
