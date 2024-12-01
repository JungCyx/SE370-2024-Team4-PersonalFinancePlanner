package View;

import javax.swing.*;

import DAO.BudgetGoalDAO;
import Main.Controller.UserController;
import Model.BudgetGoal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class BudgetGUI extends JPanel{
    private JComboBox<String> categoryField;
    private JTextField amountField;
    private JTextField durationField_Start;
    private JTextField durationField_End;
    private JCheckBox notificationCheckBox;
    private JButton addButton;
    private JButton backButton;

    private BudgetGoal curreBudgetGoal;

    private UserController controller = new UserController();
    private BudgetGoalDAO bDao = new BudgetGoalDAO();
     
    //constructor for SavingsGUI
    BudgetGUI(){

        setLayout(new BorderLayout()); // Use BorderLayout for the main panel

        JLabel titleLabel = new JLabel("Budget", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel descriptionLabel = new JLabel("Add a new budget", JLabel.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        add(titleLabel, BorderLayout.NORTH);
        add(descriptionLabel, BorderLayout.PAGE_START);
        
         //Center Pnael to display the input fields
         JPanel centerPanel = new JPanel();
         centerPanel.setLayout(new GridBagLayout());
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.insets = new Insets(10, 10, 10, 10);

         // Dropdown for Categories
        JLabel categoryLabel = new JLabel("Category:");
        gbc.gridx = 0;
        gbc.gridy = 0;
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
            "Auto Insurance",  "Life Insurance", "Household Items/Supplies",
            "Gym Memberships","Salon Services", "Cosmetics", "Babysitter",
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
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(amountLabel, gbc);
        amountField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(amountField, gbc);



        JLabel durationLabel = new JLabel("Start Date:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(durationLabel, gbc);
        durationField_Start = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(durationField_Start, gbc);

        //Duration Field
        JLabel durationLabel2 = new JLabel("End Date:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(durationLabel2, gbc);
        durationField_End = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(durationField_End, gbc);


        //Notification set up
        //This will be it own seperate class later on
        notificationCheckBox = new JCheckBox("Turn On Notifications");
        gbc.gridx = 0;
        gbc.gridy = 4;
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
                handelBudget();
            }
        });
        buttonPanel.add(addButton);
        

        // Add the bottom panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);
    }


    //Handel the user's inputs
    private void handelBudget(){
        String category = (String) categoryField.getSelectedItem();
        String amount = amountField.getText();
        String duration_start = durationField_Start.getText();
        String duration_end = durationField_End.getText();
        boolean notificationsEnabled = notificationCheckBox.isSelected();


        //Check if any field is empty
        if (duration_start.isEmpty() || category.isEmpty() || amount.isEmpty() || duration_end.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please fill out all the fields", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //check the amount they inputed
        try{
        
            JOptionPane.showMessageDialog(this, null, "Success", JOptionPane.INFORMATION_MESSAGE);

            Double Pamount = Double.parseDouble(amount);

            curreBudgetGoal = controller.mapBudgetGoal(category, Pamount, duration_start, duration_end, notificationsEnabled);
            bDao.addBudgetIntoDatabase(curreBudgetGoal);

            //clear everything after it is added
            durationField_Start.setText("");
            durationField_End.setText("");
            categoryField.setSelectedIndex(0);
            amountField.setText("");
            notificationCheckBox.setSelected(false);
        }

        catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Please enter a number bewtween 1 - 1000000", "ERROR", JOptionPane.ERROR_MESSAGE);

        }
    }
}

