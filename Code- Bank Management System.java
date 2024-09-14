import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BankManagementSystem extends JFrame implements ActionListener {

    private JLabel balanceLabel, amountLabel, accountTypeLabel;
    private JTextField amountField;
    private JComboBox<String> accountTypeCombo;
    private JButton depositButton, withdrawButton, checkBalanceButton, displayStatsButton;
    private ArrayList<String> savingsTransactions = new ArrayList<>();
    private ArrayList<String> currentTransactions = new ArrayList<>();
    private Map<String, Float> accountBalances = new HashMap<>();
    private String currentAccountType = "Savings";

    public BankManagementSystem() {
        // Initialize account balances
        accountBalances.put("Savings", 50000.0f);
        accountBalances.put("Current", 10000.0f);

        setTitle("Bank Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.WEST;
        Font largeFont = new Font("Serif", Font.BOLD, 24); // Larger font for labels and buttons
        Font mediumFont = new Font("Serif", Font.PLAIN, 20); // Medium font for text fields and combo box

        // Account Type ComboBox
        accountTypeLabel = new JLabel("Account Type:");
        accountTypeLabel.setFont(largeFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(accountTypeLabel, gbc);

        String[] accountTypes = {"Savings", "Current"};
        accountTypeCombo = new JComboBox<>(accountTypes);
        accountTypeCombo.setFont(mediumFont);
        accountTypeCombo.addActionListener(this);
        gbc.gridx = 1;
        add(accountTypeCombo, gbc);

        // Balance Label
        balanceLabel = new JLabel("Current Balance: Rs. " + accountBalances.get(currentAccountType));
        balanceLabel.setFont(largeFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(balanceLabel, gbc);

        // Amount Field
        amountLabel = new JLabel("Amount:");
        amountLabel.setFont(largeFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(amountLabel, gbc);

        amountField = new JTextField(15);
        amountField.setFont(mediumFont);
        gbc.gridx = 1;
        add(amountField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        buttonPanel.setFont(largeFont);

        depositButton = new JButton("Deposit");
        depositButton.setFont(largeFont);
        depositButton.addActionListener(this);
        buttonPanel.add(depositButton);

        withdrawButton = new JButton("Withdraw");
        withdrawButton.setFont(largeFont);
        withdrawButton.addActionListener(this);
        buttonPanel.add(withdrawButton);

        checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.setFont(largeFont);
        checkBalanceButton.addActionListener(this);
        buttonPanel.add(checkBalanceButton);

        displayStatsButton = new JButton("Display Stats");
        displayStatsButton.setFont(largeFont);
        displayStatsButton.addActionListener(this);
        buttonPanel.add(displayStatsButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == accountTypeCombo) {
            currentAccountType = (String) accountTypeCombo.getSelectedItem();
            updateBalanceLabel();
        } else if (e.getSource() == depositButton) {
            handleDeposit();
        } else if (e.getSource() == withdrawButton) {
            handleWithdraw();
        } else if (e.getSource() == checkBalanceButton) {
            showBalance();
        } else if (e.getSource() == displayStatsButton) {
            displayStatistics();
        }
    }

    private void handleDeposit() {
        try {
            float amount = Float.parseFloat(amountField.getText());
            if (amount > 0) {
                accountBalances.put(currentAccountType, accountBalances.get(currentAccountType) + amount);
                String transactionMessage = "Deposited: Rs. " + amount + " to " + currentAccountType + " Account";
                if (currentAccountType.equals("Savings")) {
                    savingsTransactions.add(transactionMessage);
                } else {
                    currentTransactions.add(transactionMessage);
                }
                updateBalanceLabel();
                amountField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Deposit amount must be positive.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleWithdraw() {
        try {
            float amount = Float.parseFloat(amountField.getText());
            if (amount > 0) {
                if (amount <= accountBalances.get(currentAccountType)) {
                    accountBalances.put(currentAccountType, accountBalances.get(currentAccountType) - amount);
                    String transactionMessage = "Withdrawn: Rs. " + amount + " from " + currentAccountType + " Account";
                    if (currentAccountType.equals("Savings")) {
                        savingsTransactions.add(transactionMessage);
                    } else {
                        currentTransactions.add(transactionMessage);
                    }
                    updateBalanceLabel();
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance.", "Transaction Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Withdrawal amount must be positive.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Current Balance: Rs. " + accountBalances.get(currentAccountType));
    }

    private void showBalance() {
        JOptionPane.showMessageDialog(this,
                "Current Balance for " + currentAccountType + " Account: Rs. " + accountBalances.get(currentAccountType),
                "Balance",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void displayStatistics() {
        ArrayList<String> transactions = currentAccountType.equals("Savings") ? savingsTransactions : currentTransactions;
        int deposits = 0;
        int withdrawals = 0;
        float totalDeposits = 0;
        float totalWithdrawals = 0;

        for (String transaction : transactions) {
            if (transaction.startsWith("Deposited:")) {
                deposits++;
                totalDeposits += Float.parseFloat(transaction.split("Rs. ")[1].split(" ")[0]);
            } else if (transaction.startsWith("Withdrawn:")) {
                withdrawals++;
                totalWithdrawals += Float.parseFloat(transaction.split("Rs. ")[1].split(" ")[0]);
            }
        }

        String statsMessage = "Statistics for " + currentAccountType + " Account:\n" +
                "Deposits: " + deposits + " (Total: Rs. " + totalDeposits + ")\n" +
                "Withdrawals: " + withdrawals + " (Total: Rs. " + totalWithdrawals + ")";
        JOptionPane.showMessageDialog(this, statsMessage, "Transaction Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Show login dialog first
        JFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}

class LoginFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Font largeFont = new Font("Serif", Font.BOLD, 24); // Larger font for labels and buttons

        addTitleLabel(largeFont);
        addUsernameAndPasswordFields(largeFont);
        addLoginButton(largeFont);

        setVisible(true);
    }

    private void addTitleLabel(Font largeFont) {
        JLabel titleLabel = new JLabel("Bank Management System Login");
        titleLabel.setFont(largeFont);
        titleLabel.setBounds(50, 20, 400, 40);
        add(titleLabel);
    }

    private void addUsernameAndPasswordFields(Font largeFont) {
        addLabelAndField("Username:", 80, usernameField = new JTextField(), largeFont);
        addLabelAndField("Password:", 140, passwordField = new JPasswordField(), largeFont);
    }

    private void addLabelAndField(String labelText, int yPos, JTextField textField, Font font) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setBounds(50, yPos, 150, 40);
        add(label);

        textField.setFont(font);
        textField.setBounds(200, yPos, 250, 40);
        add(textField);
    }

    private void addLoginButton(Font largeFont) {
        JButton loginButton = new JButton("Login");
        loginButton.setFont(largeFont);
        loginButton.setBounds(150, 200, 200, 50);
        loginButton.addActionListener(this);
        add(loginButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if ("Abhilash".equals(username) && "Abhi@9618".equals(password)) {
            // Close the login window and open the main bank management system window
            this.dispose();
            new BankManagementSystem().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
