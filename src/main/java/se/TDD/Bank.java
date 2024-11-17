package se.TDD;

import java.util.HashMap;
import java.util.Map;

public class Bank implements BankInterface {
    private static final String BANK_NAME = "E-Corp ";
    private Map<String, User> users = new HashMap<>();

    public Bank() {
        users.put("test1", new User("test1", "0034", 1000.0));
        users.put("test2", new User("Pawlo Picasso", "0069", 1234567.124));
    }

    @Override
    public User getUserById(String id) {
        return users.get(id);
    }

    @Override
    public boolean isCardLocked(String userId) {
        User user = users.get(userId);
        return user != null && user.isLocked();
    }


    @Override
    public void lockCard(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.lockCard();

        }
    }
    @Override
    public double getBalance(String userId) {
        User user = users.get(userId);
        return user != null ? user.getBalance() : 0.0;
    }

    @Override
    public boolean withdraw(String userId, double amount) {
        User user = users.get(userId);
        return user != null && user.withdraw(amount);
    }

    @Override
    public void deposit(String userId, double amount) {
        User user = users.get(userId);
        if (user != null) {
            user.deposit(amount);
        }
    }

    public static String getBankName() {
        return BANK_NAME;
    }
}
