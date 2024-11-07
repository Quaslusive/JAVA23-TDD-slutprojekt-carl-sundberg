package se.TDD;

import java.util.Scanner;

public class ATM {

    private BankInterface bankInterface;
    private User currentUser;

    public ATM(BankInterface bankInterface) {
        this.bankInterface = bankInterface;
    }

    public boolean insertCard(String cardId) {
        if (bankInterface.isCardLocked(cardId)) {
            System.out.println("Det här kortet är låst.");
            return false;
        }
        currentUser = bankInterface.getUserById(cardId);
        if (currentUser != null) {
            System.out.println("Kort accepterat. Ange din PIN:");
            return true;
        } else {
            System.err.println("Ogiltigt kort.");
        return false;
        }
    }

    public boolean enterPin(String pin) {
        if (currentUser == null) {
            System.out.println("Sätt in ditt kort först.");
            return false;
        }

        if (currentUser.verifyPin(pin)) {
            System.out.println("PIN kod korrekt. Åtkomst beviljas.");
            currentUser.resetFailedAttempts();
          //  showMenu();
            return true;
        } else {
            currentUser.incrementFailedAttempts();
            if (currentUser.getFailedAttempts() >= 3) {
                bankInterface.lockCard(currentUser.getId());
                System.out.println("För många misslyckade försök. Ditt kort är nu låst.🔒");
            } else {
                System.out.println("Felaktig PIN. Återstående försök: " + (3 - currentUser.getFailedAttempts()));
            }
            return false;
        }
    }


    private void showMenu() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Välkommen till " + Bank.getBankName());
                System.out.println("Select an option:\n1. Kontrollera saldo\n2. Insättning\n3. Ta ut pengar\n4. Avsluta");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> checkBalance();
                    case 2 -> depositMoney(scanner);
                    case 3 -> withdrawMoney(scanner);
                    case 4 -> {
                        System.out.println("Avslutar...");
                        return;
                    }
                    default -> System.err.println("Ogiltigt val. Försök igen.");
                }
            }
        }
    }

    public void checkBalance() {
        System.out.println("Din saldo är: " + bankInterface.getBalance(currentUser.getId()) + " kr");
    }

    private void depositMoney(Scanner scanner) {
        System.out.print("Ange belopp för insättning: ");
        double amount = scanner.nextDouble();
        if (amount > 0) {
            bankInterface.deposit(currentUser.getId(), amount);
            System.out.println("Insättning lyckades. Ny saldo: " + bankInterface.getBalance(currentUser.getId()) + " kr");
        } else {
            System.out.println("Ogiltigt insättnings belopp.");
        }
    }

    private void withdrawMoney(Scanner scanner) {
        System.out.print("Ange belopp: ");
        double amount = scanner.nextDouble();
        if (amount > 0) {
            if (bankInterface.withdraw(currentUser.getId(), amount)) {
                System.out.println("Uttag lyckades. Ny saldo:" + bankInterface.getBalance(currentUser.getId()) + " kr");
            } else {
                System.out.println("Otillräckligt saldo.");
            }
        } else {
            System.out.println("Ogiltigt uttagsbelopp.");
        }
    }
}
