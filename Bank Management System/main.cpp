#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

class Account
{
private:
    int accNumber;
    string name;
    double balance;

public:
    Account() {}
    Account(int accNumber, string name, double balance)
    {
        this->accNumber = accNumber;
        this->name = name;
        this->balance = balance;
    }

    void createAccount()
    {
        cout << "Enter Account Number: ";
        cin >> accNumber;
        cout << "Enter Name: ";
        cin.ignore();
        getline(cin, name);
        cout << "Enter Initial Balance: ";
        cin >> balance;
    }

    void display() const
    {
        cout << "Account Number: " << accNumber << "\nName: " << name << "\nBalance: $" << balance << "\n";
    }

    void deposit(double amount)
    {
        balance += amount;
        cout << "Deposit Successful!\n";
    }

    bool withdraw(double amount)
    {
        if (amount > balance)
        {
            cout << "Insufficient balance.\n";
            return false;
        }
        balance -= amount;
        cout << "Withdrawal Successful!\n";
        return true;
    }

    int getAccNumber() const { return accNumber; }
    string getName() const { return name; }
    double getBalance() const { return balance; }

    void modify(string newName, double newBalance)
    {
        name = newName;
        balance = newBalance;
    }
};

class Bank
{
private:
    vector<Account> accounts;

    void loadFromFile()
    {
        accounts.clear();
        ifstream file("accounts.dat", ios::binary);
        if (file.is_open())
        {
            Account acc;
            while (file.read((char *)&acc, sizeof(Account)))
            {
                accounts.push_back(acc);
            }
            file.close();
        }
    }

    void saveToFile()
    {
        ofstream file("accounts.dat", ios::binary);
        for (auto &acc : accounts)
        {
            file.write((char *)&acc, sizeof(Account));
        }
        file.close();
    }

public:
    Bank() { loadFromFile(); }
    ~Bank() { saveToFile(); }

    void createAccount()
    {
        Account acc;
        acc.createAccount();
        accounts.push_back(acc);
        cout << "Account Created Successfully!\n";
    }

    void displayAllAccounts()
    {
        for (const auto &acc : accounts)
        {
            acc.display();
            cout << "---------------------\n";
        }
    }

    void searchAccount(int accNo)
    {
        for (const auto &acc : accounts)
        {
            if (acc.getAccNumber() == accNo)
            {
                acc.display();
                return;
            }
        }
        cout << "Account not found.\n";
    }

    void depositToAccount(int accNo, double amount)
    {
        for (auto &acc : accounts)
        {
            if (acc.getAccNumber() == accNo)
            {
                acc.deposit(amount);
                return;
            }
        }
        cout << "Account not found.\n";
    }

    void withdrawFromAccount(int accNo, double amount)
    {
        for (auto &acc : accounts)
        {
            if (acc.getAccNumber() == accNo)
            {
                acc.withdraw(amount);
                return;
            }
        }
        cout << "Account not found.\n";
    }

    void modifyAccount(int accNo)
    {
        for (auto &acc : accounts)
        {
            if (acc.getAccNumber() == accNo)
            {
                string newName;
                double newBalance;
                cout << "Enter new name: ";
                cin.ignore();
                getline(cin, newName);
                cout << "Enter new balance: ";
                cin >> newBalance;
                acc.modify(newName, newBalance);
                cout << "Account modified successfully.\n";
                return;
            }
        }
        cout << "Account not found.\n";
    }

    void deleteAccount(int accNo)
    {
        for (size_t i = 0; i < accounts.size(); ++i)
        {
            if (accounts[i].getAccNumber() == accNo)
            {
                accounts.erase(accounts.begin() + i);
                cout << "Account deleted successfully.\n";
                return;
            }
        }
        cout << "Account not found.\n";
    }
};

int main()
{
    Bank bank;
    int choice, accNo;
    double amount;

    while (true)
    {
        cout << "\n==== Bank Management System ====\n";
        cout << "1. Create Account\n";
        cout << "2. View All Accounts\n";
        cout << "3. Search Account\n";
        cout << "4. Deposit Money\n";
        cout << "5. Withdraw Money\n";
        cout << "6. Modify Account\n";
        cout << "7. Delete Account\n";
        cout << "8. Exit\n";
        cout << "Enter choice: ";
        cin >> choice;

        switch (choice)
        {
        case 1:
            bank.createAccount();
            break;
        case 2:
            bank.displayAllAccounts();
            break;
        case 3:
            cout << "Enter account number: ";
            cin >> accNo;
            bank.searchAccount(accNo);
            break;
        case 4:
            cout << "Enter account number: ";
            cin >> accNo;
            cout << "Enter amount to deposit: ";
            cin >> amount;
            bank.depositToAccount(accNo, amount);
            break;
        case 5:
            cout << "Enter account number: ";
            cin >> accNo;
            cout << "Enter amount to withdraw: ";
            cin >> amount;
            bank.withdrawFromAccount(accNo, amount);
            break;
        case 6:
            cout << "Enter account number: ";
            cin >> accNo;
            bank.modifyAccount(accNo);
            break;
        case 7:
            cout << "Enter account number: ";
            cin >> accNo;
            bank.deleteAccount(accNo);
            break;
        case 8:
            cout << "Exiting system...\n";
            return 0;
        default:
            cout << "Invalid choice. Try again.\n";
        }
    }
}

