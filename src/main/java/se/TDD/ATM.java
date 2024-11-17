package se.TDD;

public class ATM {

    private BankInterface bankInterface;
    private User currentUser;

    public ATM(BankInterface bankInterface) {
        this.bankInterface = bankInterface;
    }

    public boolean insertCard(String cardId) {
        if (bankInterface.isCardLocked(cardId)) {
            System.err.println("Det här kortet är låst.");
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
            return true;
        } else {
            currentUser.incrementFailedAttempts();
            if (currentUser.getFailedAttempts() >= 3) {
                bankInterface.lockCard(currentUser.getId());
                System.err.println("För många misslyckade försök. Ditt kort är nu låst.🔒");
            } else {
                System.err.println("Felaktig PIN. Återstående försök: " + (3 - currentUser.getFailedAttempts()));
            }
            return false;
        }
    }

    public void checkBalance() {
        System.out.println("Din saldo är: " + bankInterface.getBalance(currentUser.getId()) + " kr");
    }

    public void depositMoney(double amount) {
        if (amount > 0) {
            bankInterface.deposit(currentUser.getId(), amount);
            System.out.println("Insättning lyckades. Ny saldo: " + bankInterface.getBalance(currentUser.getId()) + " kr");
        } else {
            System.err.println("Ogiltigt insättningsbelopp.");
        }
    }


    public boolean withdrawMoney(double amount) {
        if (amount > 0) {
            if (bankInterface.withdraw(currentUser.getId(), amount)) {
                System.out.println("Uttag lyckades. Ny saldo:" + bankInterface.getBalance(currentUser.getId()) + " kr");
                return true;
            } else {
                System.err.println("Otillräckligt saldo.");
                return false;
            }
        } else {
            System.err.println("Ogiltigt uttagsbelopp.");
            return false;
        }
    }
}