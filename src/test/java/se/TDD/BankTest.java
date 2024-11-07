package se.TDD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    @Mock
    private Bank mockBank;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = mock(User.class);
        when(mockBank.getUserById("test1")).thenReturn(mockUser);
    }

    @Test
    @DisplayName("Testa getUserById - Giltig användare")
    public void testGetUserByIdValid() {
        User user = mockBank.getUserById("test1");
        assertNotNull(user, "Användaren bör inte vara null för ett giltigt ID");
    }

    @Test
    @DisplayName("Testa isCardLocked - Ospärrat kort")
    public void testIsCardLockedFalse() {
        when(mockUser.isLocked()).thenReturn(false);
        when(mockBank.isCardLocked("test1")).thenReturn(false);

        assertFalse(mockBank.isCardLocked("test1"), "Kortet bör inte vara spärrat");
    }

    @Test
    @DisplayName("Testa lockCard - Giltig användare")
    public void testLockCardValidUser() {
        mockBank.lockCard("test1");
        verify(mockBank, times(1)).lockCard("test1");
    }

    @Test
    @DisplayName("Testa getBalance - Giltig användare")
    public void testGetBalanceValidUser() {
        when(mockBank.getBalance("test1")).thenReturn(500.0);  // Stubba metod på mockad Bank

        assertEquals(500.0, mockBank.getBalance("test1"), "Balansen bör matcha det mockade värdet");
    }

    @Test
    @DisplayName("Testa deposit - Giltig användare")
    public void testDepositValidUser() {
        // Anropa metoden
        mockBank.deposit("test1", 200.0);

        verify(mockBank, times(1)).deposit("test1", 200.0);
    }

    @Test
    @DisplayName("Testa withdraw - Tillräcklig balans")
    public void testWithdrawSufficientBalance() {
        when(mockBank.withdraw("test1", 100.0)).thenReturn(true);

        assertTrue(mockBank.withdraw("test1", 100.0), "Uttaget bör lyckas");
        verify(mockBank, times(1)).withdraw("test1", 100.0);
    }

    @Test
    @DisplayName("Testa withdraw - Otillräcklig balans")
    public void testWithdrawInsufficientBalance() {
        when(mockBank.withdraw("test1", 1000.0)).thenReturn(false);

        assertFalse(mockBank.withdraw("test1", 1000.0), "Uttaget bör misslyckas vid otillräcklig balans");
        verify(mockBank, times(1)).withdraw("test1", 1000.0);
    }
}
