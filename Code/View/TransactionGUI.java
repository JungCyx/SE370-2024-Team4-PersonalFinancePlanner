package View;

import DAO.TransactionDAO;
import Main.Controller.UserController;
import Model.Transaction;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TransactionGUI extends JPanel {

    //all the fields user will be using
    //private because it is used once everytime a transaction is created 
    private JComboBox<String> typeField;
    private JComboBox<String> categoryField;
    private JTextField amountField;
    private JCheckBox notificationCheckBox;
    private JButton addButton;
    private JButton backButton;
    private UserController controller;
    private TransactionDAO tDao;

    // Constructor for TransactionGUI
    public TransactionGUI() {

        setLayout(new BorderLayout()); // Use BorderLayout for the main panel

        // Create the title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(38, 120, 190)); // Modern blue
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create title label
        JLabel titleLabel = new JLabel("Transactions", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        // Create description label
        JLabel descriptionLabel = new JLabel("Add a New Financial Transaction", JLabel.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabel.setForeground(Color.WHITE);

        // Add labels to the title panel
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(descriptionLabel, BorderLayout.SOUTH);

        // Add title panel to the top
        add(titlePanel, BorderLayout.NORTH);
        //Center Pnael to display the input fields
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        //Dropdown for Type
        JLabel typeLabel = new JLabel("Type:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(typeLabel, gbc);

        //possibilities
        typeField = new JComboBox<>(new String[]{
            "Deposit", "Withdrawal", "Transfer"
        });

        typeField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(typeField, gbc);

        // Dropdown for Categories
        JLabel categoryLabel = new JLabel("Category:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(categoryLabel, gbc);

        // possiblilites
        categoryField = new JComboBox<>(new String[]{
            "Mortgage", "Rent", "Property Taxes", "Household Repairs", "HOA Fees",
            "Transportation", "Car Payment", "Car Warranty", "Gas", "Tires",
            "Car Maintenance", "Parking Fees", "Car Repairs",
            "DMV Fees", "Groceries", "Restaurants",
            "Pet Food", "Electricity", "Water", "Garbage",
            "Phones", "Cable", "Internet", "Apperal",
            "Healthcare", "Dental Care", "Health Insurance", "Homeowner’s Insurance",
            "Auto Insurance", "Life Insurance", "Household Items/Supplies",
            "Gym Memberships", "Salon Services", "Cosmetics", "Babysitter",
            "Subscriptions", "Personal Loans", "Student Loans",
            "Credit Cards", "Retirement Fund", "Investing",
            "Emergency Fund", "Big Purchases",
            "Gifts/Donations", "Special Occasion", "Entertainment",
            "Vacations", "Subscriptions"
        });

        categoryField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(categoryField, gbc);

        //Amount Field
        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(amountLabel, gbc);
        amountField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(amountField, gbc);

        //Notification set up
        //This will be it own seperate class later on
        notificationCheckBox = new JCheckBox("Turn On Notifications");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(notificationCheckBox, gbc);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        // Create the "Back" button
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the "Dashboard" panel when the button is clicked
                // Don't Delete
                LoginGUI.cardLayout.show(LoginGUI.mainPanel, "Dashboard");
            }
        });
        buttonPanel.add(backButton);

        //Create the "Add" button
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the "Dashboard" panel when the button is clicked
                // Don't Delete
                handelTransaction();
            }
        });
        buttonPanel.add(addButton);

        // Add the bottom panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //Handel the user's inputs
    private void handelTransaction() {
        String type = (String) typeField.getSelectedItem();
        String category = (String) categoryField.getSelectedItem();
        String amount = amountField.getText();
        Double newAmount = Double.parseDouble(amount);
        boolean notificationsEnabled = notificationCheckBox.isSelected();

        controller = new UserController();
        tDao = new TransactionDAO();
        Transaction transactions = controller.mapTransaction(type, category, newAmount, notificationsEnabled);
        tDao.addTransactionIntoDatabase(transactions);

        //Check if any field is empty
        if (type.isEmpty() || category.isEmpty() || amount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all the fields", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //check the amount they inputed
        try {

            JOptionPane.showMessageDialog(this, null, "Success", JOptionPane.INFORMATION_MESSAGE);

            //clear everything after it is added
            typeField.setSelectedIndex(0);
            categoryField.setSelectedIndex(0);
            amountField.setText("");
            notificationCheckBox.setSelected(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a number bewtween 1 - 1000000", "ERROR", JOptionPane.ERROR_MESSAGE);

        }

    }
}
