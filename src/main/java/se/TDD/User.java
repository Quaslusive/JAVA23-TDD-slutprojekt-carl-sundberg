package se.TDD;
public class User {
    private String id;
    private String pin;
    private double balance;
    private int failedAttempts;
    private boolean isLocked;

    public User(String id, String pin, double balance) {
        this.id = id;
        this.pin = pin;
        this.balance = balance;
        this.failedAttempts = 0;
        this.isLocked = false;
    }

    // Getters och Setters
    public String getId() {
        return id;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void lockCard() {
        this.isLocked = true;
    }

    public void incrementFailedAttempts() {
        this.failedAttempts++;
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            this.balance -= amount;
            return true ;
        } else {
            return false;
        }
    }
    public boolean verifyPin(String inputPin) {
        return inputPin.equals(this.pin);
    }
}
