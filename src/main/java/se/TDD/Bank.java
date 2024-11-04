package se.TDD;

import java.util.HashMap;
import java.util.Map;

public class Bank implements BankInterface {
    private static final String BANK_NAME = "E-Corp ";
    private Map<String, User> users = new HashMap<>();

    public Bank() {
        users.put("420", new User("0069", "14512", 1000.0));
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
    public boolean isLockCard(String userId) {
        User user = users.get(userId);
        return user != null && user.isLocked();
    }
    @Override
    public void lockCard(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.lockCard(true);

        }
    }
    @Override
    public double getBalance(String cardId) {
        User user = users.get(cardId);
        return user != null ? user.getBalance() : 0.0;
    }

    @Override
    public void deposit(String cardId, double amount) {
        User user = users.get(cardId);
        if (user != null) {
            user.setBalance(user.getBalance() + amount);
        }
    }

    @Override
    public void withdraw(String cardId, double amount) {
        User user = users.get(cardId);
        if (user != null && user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
        }
    }

    public static String getBankName() {
        return BANK_NAME;
    }
}
