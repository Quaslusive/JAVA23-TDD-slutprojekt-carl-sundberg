package se.TDD;
public interface BankInterface {
    User getUserById(String id);
    boolean isCardLocked(String userId);
    void lockCard(String userId);
    double getBalance(String userId);
    void deposit(String userId, int amount);
    void withdraw(String userId, int amount);
}
