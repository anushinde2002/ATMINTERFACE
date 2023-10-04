import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Transaction {
	private String transactionType;
	private double amount;
	private String timestamp;

	public Transaction(String transactionType, double amount, String timestamp) {
		this.transactionType = transactionType;
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public String getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "Type: " + transactionType + ", Amount: $" + amount + ", Timestamp: " + timestamp;
	}
}

class Account {
	private int accountNumber;
	private int pin;
	private double balance;
	private List<Transaction> transactionHistory;

	public Account(int accountNumber, int pin) {
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.balance = 0.0;
		this.transactionHistory = new ArrayList<>();
	}

	public boolean validatePin(int enteredPin) {
		return this.pin == enteredPin;
	}

	public void deposit(double amount) {
		balance += amount;
		transactionHistory.add(new Transaction("Deposit", amount, getCurrentTimestamp()));
	}

	public boolean withdraw(double amount) {
		if (balance >= amount) {
			balance -= amount;
			transactionHistory.add(new Transaction("Withdrawal", amount, getCurrentTimestamp()));
			return true;
		}
		return false;
	}

	public void transfer(Account receiver, double amount) {
		if (withdraw(amount)) {
			receiver.deposit(amount);
			transactionHistory.add(new Transaction("Transfer", amount, getCurrentTimestamp()));
		}
	}

	public List<Transaction> getTransactionHistory() {
		return transactionHistory;
	}

	public double getBalance() {
		return balance;
	}

	private String getCurrentTimestamp() {
		// Implement timestamp generation logic here
		return "2023-10-03 14:30:00";
	}
}

class User {
	private int userId;
	private int pin;
	private Account account;

	public User(int userId, int pin) {
		this.userId = userId;
		this.pin = pin;
		this.account = new Account(userId, pin);
	}

	public int getUserId() {
		return userId;
	}

	public boolean validatePin(int enteredPin) {
		return this.pin == enteredPin;
	}

	public Account getAccount() {
		return account;
	}
}

public class ATM {
	private List<User> users;
	private User currentUser;
	private Scanner scanner;

	public ATM() {
		this.users = new ArrayList<>();
		this.scanner = new Scanner(System.in);
	}

	public void addUser(User user) {
		users.add(user);
	}

	public void start() {
		System.out.println("Welcome to the ATM!");
		while (true) {
			System.out.print("Enter User ID (0 to quit): ");
			int userId = scanner.nextInt();

			if (userId == 0) {
				System.out.println("Thank you for using the ATM. Goodbye!");
				break;
			}

			System.out.print("Enter PIN: ");
			int pin = scanner.nextInt();

			User foundUser = findUser(userId);
			if (foundUser != null && foundUser.validatePin(pin)) {
				currentUser = foundUser;
				performTransaction();
			} else {
				System.out.println("Invalid User ID or PIN. Please try again.");
			}
		}
	}

	private User findUser(int userId) {
		for (User user : users) {
			if (user.getUserId() == userId) {
				return user;
			}
		}
		return null;
	}

	private void performTransaction() {
		Account account = currentUser.getAccount();

		while (true) {
			System.out.println("\nChoose an option:");
			System.out.println("1. Check Balance");
			System.out.println("2. Deposit");
			System.out.println("3. Withdraw");
			System.out.println("4. Transfer");
			System.out.println("5. Transaction History");
			System.out.println("6. Quit");

			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				System.out.println("Balance: $" + account.getBalance());
				break;
			case 2:
				System.out.print("Enter deposit amount: $");
				double depositAmount = scanner.nextDouble();
				account.deposit(depositAmount);
				System.out.println("Deposit successful.");
				break;
			case 3:
				System.out.print("Enter withdrawal amount: $");
				double withdrawalAmount = scanner.nextDouble();
				if (account.withdraw(withdrawalAmount)) {
					System.out.println("Withdrawal successful.");
				} else {
					System.out.println("Insufficient funds.");
				}
				break;
			case 4:
				System.out.print("Enter recipient's User ID: ");
				int recipientUserId = scanner.nextInt();
				User recipientUser = findUser(recipientUserId);

				if (recipientUser != null) {
					System.out.print("Enter transfer amount: $");
					double transferAmount = scanner.nextDouble();
					account.transfer(recipientUser.getAccount(), transferAmount);
					System.out.println("Transfer successful.");
				} else {
					System.out.println("Recipient User ID not found.");
				}
				break;
			case 5:
				List<Transaction> transactionHistory = account.getTransactionHistory();
				System.out.println("\nTransaction History:");
				for (Transaction transaction : transactionHistory) {
					System.out.println(transaction);
				}
				break;
			case 6:
				System.out.println("Thank you for using the ATM. Goodbye!");
				return;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	public static void main(String[] args) {
		ATM atm = new ATM();

		// Add sample users to the ATM
		User user1 = new User(1, 1234);
		User user2 = new User(2, 5678);

		atm.addUser(user1);
		atm.addUser(user2);

		atm.start();
	}
}
