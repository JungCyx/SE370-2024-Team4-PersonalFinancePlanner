package View;

import javax.swing.*;

import DAO.SavingsGoalDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.SavingsGoal;

public class SavingsGUI extends JPanel {
    private JTextField nameField;
    private JTextField targetField;
    private JComboBox<String> deadlineComboBox; // Dropdown for deadline
    private JTextField startingAmountField;
    private JCheckBox notificationCheckBox;
    private JButton addButton;
    private JButton backButton;
    public SavingsGoal savingsGoal;

    private SavingsGoalDAO savingDao = new SavingsGoalDAO();

    // Constructor for SavingsGUI
    public SavingsGUI() {

        setLayout(new BorderLayout()); // Use BorderLayout for the main panel

        // Create the title label
        JLabel titleLabel = new JLabel("Savings", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel descriptionLabel = new JLabel(" Create a new savings goal", JLabel.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        // Add the title label to the top of the panel
        add(titleLabel, BorderLayout.NORTH);
        add(descriptionLabel, BorderLayout.PAGE_START);

        // Center Panel for input fields
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Name Field
        JLabel nameLabel = new JLabel("Goal Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(nameLabel, gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(nameField, gbc);

        // Target Amount Field
        JLabel targetLabel = new JLabel("Target Amount:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(targetLabel, gbc);
        targetField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(targetField, gbc);

        // Deadline Dropdown
        JLabel deadlineLabel = new JLabel("Deadline:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(deadlineLabel, gbc);
        String[] deadlineOptions = { "1 Week", "2 Weeks", "1 Month", "3 Months", "6 Months", "1 Year" };
        deadlineComboBox = new JComboBox<>(deadlineOptions);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(deadlineComboBox, gbc);

        // Starting Amount Field
        JLabel startingAmountLabel = new JLabel("Starting Amount:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(startingAmountLabel, gbc);
        startingAmountField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(startingAmountField, gbc);

        // Notification Checkbox
        notificationCheckBox = new JCheckBox("Receive Notifications");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(notificationCheckBox, gbc);
        add(centerPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI.cardLayout.show(LoginGUI.mainPanel, "Dashboard");
            }
        });
        buttonPanel.add(backButton);

        addButton = new JButton("Create Savings");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSavings();
            }
        });
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Handle user's input
    private void handleSavings() {

        String name = nameField.getText();
        String target = targetField.getText();
        String deadline = (String) deadlineComboBox.getSelectedItem(); // Get selected deadline
        String startingAmount = startingAmountField.getText();
        boolean notificationsEnabled = notificationCheckBox.isSelected();

        // Check for empty fields
        if (name.isEmpty() || target.isEmpty() || startingAmount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Parse target and starting amount as doubles
            Float targetAmount = Float.parseFloat(target);
            Float startingAmountValue = Float.parseFloat(startingAmount);

            // Create a SavingsGoal object
            SavingsGoal newGoal = new SavingsGoal(name, targetAmount, deadline, startingAmountValue,
                    notificationsEnabled);

            // Add the saving goal into the database table
            savingDao.addGoalIntoDatabase(newGoal);

            SavingsGoalDAO.setCurrentSavingsGoal(newGoal);

            // Show success message
            JOptionPane.showMessageDialog(this, "Savings goal created successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Optionally, clear the fields
            nameField.setText("");
            targetField.setText("");
            deadlineComboBox.setSelectedIndex(0);
            startingAmountField.setText("");
            notificationCheckBox.setSelected(false);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for target and/or starting amounts.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
