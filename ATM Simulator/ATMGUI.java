import java.awt.*
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class Account {
    private String accountNumber;
    private String pin;
    private double balance;

    public Account(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

class ATMSimulation extends JFrame implements ActionListener {
    private Map<String, Account> accounts;
    private Account currentAccount;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField accountField;
    private JPasswordField pinField;

    private JLabel balanceLabel;
    private JTextField amountField;
    private JLabel messageLabel;

    public ATMSimulation() {
        accounts = new HashMap<>();
        loadSampleAccounts();
        initUI();
    }

    private void loadSampleAccounts() {
        accounts.put("123456", new Account("123456", "1234", 1000.0));
        accounts.put("654321", new Account("654321", "4321", 500.0));
    }

    private void initUI() {
        setTitle("ATM Simulation - Java GUI Application");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createMenuPanel(), "Menu");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        accountField = new JTextField();
        pinField = new JPasswordField();

        formPanel.add(new JLabel("Enter Account Number:"));
        formPanel.add(accountField);
        formPanel.add(new JLabel("Enter 4-digit PIN:"));
        formPanel.add(pinField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setActionCommand("login");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        JLabel info = new JLabel("Welcome to ATM Simulator. Please login to continue.", JLabel.CENTER);
        info.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(info, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("ATM Main Menu", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        balanceLabel = new JLabel("Balance: $0.0", JLabel.CENTER);
        amountField = new JTextField();
        messageLabel = new JLabel("", JLabel.CENTER);

        JButton viewBalance = new JButton("View Balance");
        JButton deposit = new JButton("Deposit Money");
        JButton withdraw = new JButton("Withdraw Money");
        JButton logout = new JButton("Logout");

        viewBalance.setActionCommand("view");
        deposit.setActionCommand("deposit");
        withdraw.setActionCommand("withdraw");
        logout.setActionCommand("logout");

        viewBalance.addActionListener(this);
        deposit.addActionListener(this);
        withdraw.addActionListener(this);
        logout.addActionListener(this);

        centerPanel.add(new JLabel("Enter Amount:", JLabel.CENTER));
        centerPanel.add(amountField);
        centerPanel.add(viewBalance);
        centerPanel.add(deposit);
        centerPanel.add(withdraw);
        centerPanel.add(logout);

        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(balanceLabel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(messageLabel, BorderLayout.SOUTH);

        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "login":
                String accNum = accountField.getText().trim();
                String pin = new String(pinField.getPassword()).trim();
                if (accounts.containsKey(accNum)) {
                    Account acc = accounts.get(accNum);
                    if (acc.validatePin(pin)) {
                        currentAccount = acc;
                        updateBalanceLabel();
                        messageLabel.setText("Welcome! Use the options below to perform transactions.");
                        cardLayout.show(mainPanel, "Menu");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid PIN. Please try again.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "view":
                updateBalanceLabel();
                messageLabel.setText("Your current balance is displayed above.");
                break;
            case "deposit":
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    if (amount <= 0)
                        throw new NumberFormatException();
                    currentAccount.deposit(amount);
                    updateBalanceLabel();
                    messageLabel.setText("Deposited successfully: $" + amount);
                    amountField.setText("");
                } catch (NumberFormatException ex) {
                    messageLabel.setText("Please enter a valid positive amount.");
                }
                break;
            case "withdraw":
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    if (currentAccount.withdraw(amount)) {
                        updateBalanceLabel();
                        messageLabel.setText("Withdrawn successfully: $" + amount);
                        amountField.setText("");
                    } else {
                        messageLabel.setText("Insufficient funds or invalid amount.");
                    }
                } catch (NumberFormatException ex) {
                    messageLabel.setText("Please enter a valid positive amount.");
                }
                break;
            case "logout":
                currentAccount = null;
                accountField.setText("");
                pinField.setText("");
                amountField.setText("");
                messageLabel.setText("");
                balanceLabel.setText("Balance: $0.0");
                cardLayout.show(mainPanel, "Login");
                break;
        }
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Balance: $" + currentAccount.getBalance());
    }
}

class ATMGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMSimulation::new);
    }
}
