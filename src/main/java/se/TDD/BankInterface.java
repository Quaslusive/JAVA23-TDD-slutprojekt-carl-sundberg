package se.TDD;

public interface BankInterface {

    User getUserById(String id);

    boolean isCardLocked(String userId);

    void lockCard(String userId);

    double getBalance(String userId);

    void deposit(String userId, double amount);

    boolean withdraw(String userId, double amount);


}
