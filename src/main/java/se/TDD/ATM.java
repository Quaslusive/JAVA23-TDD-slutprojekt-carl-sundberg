package se.TDD;
import java.util.Scanner;

public class ATM {
    private BankInterface bankInterface;
    private User currentUser;

    public ATM(BankInterface bankInterface) {
        this.bankInterface = bankInterface;
    }

    public void insertCard(String cardId) {
        if (bankInterface.isCardLocked(cardId)) {
            System.out.println("This card is locked.");
            return;
        }
        currentUser = bankInterface.getUserById(cardId);
        if (currentUser != null) {
            System.out.println("Card accepted. Please enter your PIN:");
        } else {
            System.out.println("Invalid card.");
        }
    }

    public void enterPin(String pin) {
        if (currentUser == null) {
            System.out.println("Please insert your card first.");
            return;
        }

        if (currentUser.verifyPin(pin)) {
            System.out.println("PIN correct. Access granted.");
            currentUser.resetFailedAttempts();
            showMenu();
        } else {
            currentUser.incrementFailedAttempts();
            if (currentUser.getFailedAttempts() >= 3) {
                bankInterface.lockCard(currentUser.getId());
                System.out.println("Too many failed attempts. Your card is now locked.");
            } else {
                System.out.println("Incorrect PIN. Attempts remaining: " + (3 - currentUser.getFailedAttempts()));
            }
        }
    }

    private void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select an option:\n1. Check Balance\n2. Deposit Money\n3. Withdraw Money\n4. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> checkBalance();
                case 2 -> depositMoney(scanner);
                case 3 -> withdrawMoney(scanner);
                case 4 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void checkBalance() {
        System.out.println("Your balance is: $" + currentUser.getBalance());
    }

    private void depositMoney(Scanner scanner) {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        currentUser.deposit(amount);
        System.out.println("Deposit successful. New balance: $" + currentUser.getBalance());
    }

    private void withdrawMoney(Scanner scanner) {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        if (currentUser.withdraw(amount)) {
            System.out.println("Withdrawal successful. New balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

}
