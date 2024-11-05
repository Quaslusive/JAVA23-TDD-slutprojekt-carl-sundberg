package se.TDD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BankTest {

    private Bank bank;
    private User mockUser;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        mockUser = mock(User.class);
        // bank.getUserById().put("12345", mockUser);
        bank.addUserForTesting("12345", mockUser);
    }

    @Test
    @DisplayName("Test getUserById - Valid User")
    public void testGetUserByIdValid() {
        User user = bank.getUserById("12345");
        assertNotNull(user, "User should not be null for a valid ID");
    }

    @Test
    @DisplayName("Test getUserById - Invalid User")
    public void testGetUserByIdInvalid() {
        User user = bank.getUserById("invalid");
        assertNull(user, "User should be null for an invalid ID");
    }

    @Test
    @DisplayName("Test isCardLocked - Unlocked Card")
    public void testIsCardLockedFalse() {
        when(mockUser.isLocked()).thenReturn(false); // Mocking behavior
        assertFalse(bank.isCardLocked("12345"), "Card should not be locked");
    }

    @Test
    @DisplayName("Test isCardLocked - Locked Card")
    public void testIsCardLockedTrue() {
        when(mockUser.isLocked()).thenReturn(true);
        assertTrue(bank.isCardLocked("12345"), "Card should be locked");
    }

    @Test
    @DisplayName("Test lockCard - Valid User")
    public void testLockCardValidUser() {
        bank.lockCard("12345");
        verify(mockUser).lockCard(); // Verifying lockCard method was called on User
    }

    @Test
    @DisplayName("Test lockCard - Invalid User")
    public void testLockCardInvalidUser() {
        bank.lockCard("invalid");
        verify(mockUser, never()).lockCard(); // lockCard should not be called
    }

    @Test
    @DisplayName("Test getBalance - Valid User")
    public void testGetBalanceValidUser() {
        when(mockUser.getBalance()).thenReturn(500.0);
        assertEquals(500.0, bank.getBalance("12345"), "Balance should match mocked value");
    }

    @Test
    @DisplayName("Test deposit - Valid User")
    public void testDepositValidUser() {
        bank.deposit("12345", 200.0);
        verify(mockUser).deposit(200.0); // Verifying deposit method was called with correct amount
    }

    @Test
    @DisplayName("Test withdraw - Sufficient Balance")
    public void testWithdrawSufficientBalance() {
        when(mockUser.withdraw(100.0)).thenReturn(true); // Mock sufficient balance
        bank.withdraw("12345", 100.0);
        verify(mockUser).withdraw(100.0);
    }

    @Test
    @DisplayName("Test withdraw - Insufficient Balance")
    public void testWithdrawInsufficientBalance() {
        when(mockUser.withdraw(1000.0)).thenReturn(false); // Mock insufficient balance
        bank.withdraw("12345", 1000.0);
        verify(mockUser).withdraw(1000.0);
    }
}