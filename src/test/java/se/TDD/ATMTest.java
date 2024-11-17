package se.TDD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    @Mock
    private BankInterface mockBankInterface;

    @InjectMocks
    private ATM atm;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        atm = new ATM(mockBankInterface);

        // Skapa en delvis mockad användare med spy
        mockUser = spy(new User("test1", "0034", 1000.0));
    }

    @Test
    @DisplayName("Test av kortinsättning - Giltigt kort")
    void testInsertCardValidCard() {
        when(mockBankInterface.isCardLocked("test1")).thenReturn(false);
        when(mockBankInterface.getUserById("test1")).thenReturn(mockUser);

        atm.insertCard("test1");

        verify(mockBankInterface, times(1)).isCardLocked("test1");
        verify(mockBankInterface, times(1)).getUserById("test1");
    }

    @Test
    @DisplayName("Testa kortinsättning - Ogiltigt kort")
    void testInsertCardInvalid() {
        when(mockBankInterface.getUserById("invalidCard")).thenReturn(null);

        assertFalse(atm.insertCard("invalidCard"), "kortet bör inte accepteras om det inte finns");
        verify(mockBankInterface, times(1)).isCardLocked("invalidCard");
    }

    @Test
    @DisplayName("Test av kortinsättning - Låst kort")
    void testInsertCardLockedCard() {
        when(mockBankInterface.isCardLocked("test1")).thenReturn(true);

        atm.insertCard("test1");

        verify(mockBankInterface, times(1)).isCardLocked("test1");
        verify(mockBankInterface, never()).getUserById("test1");
    }

    @Test
    @DisplayName("Test av PIN-inmatning - Korrekt PIN")
    void testEnterPinCorrectPin() {
        when(mockBankInterface.getUserById("test1")).thenReturn(mockUser);
        when(mockBankInterface.isCardLocked("test1")).thenReturn(false);

        atm.insertCard("test1");
        assertTrue(atm.enterPin("0034"), "PIN bör vara korrekt och returnera true");
        assertEquals(0, mockUser.getFailedAttempts(), "Misslyckade försök bör återställas efter korrekt PIN-inmatning");
    }

    @Test
    @DisplayName("Test av PIN-inmatning - Felaktig PIN")
    void testEnterPinIncorrect() {
        when(mockBankInterface.getUserById("test1")).thenReturn(mockUser);
        atm.insertCard("test1");

        assertFalse(atm.enterPin("0000"), "PIN bör vara felaktig");
        assertEquals(1, mockUser.getFailedAttempts(), "Antal misslyckade försök bör öka");

        assertFalse(atm.enterPin("1111"), "PIN bör vara felaktig");
        assertEquals(2, mockUser.getFailedAttempts(), "Antal misslyckade försök bör öka");

        assertFalse(atm.enterPin("2222"), "Kortet bör låsas efter tre misslyckade försök");
        verify(mockBankInterface, times(1)).lockCard("test1");
    }

    @Test
    @DisplayName("Test av saldokontroll")
    void testCheckBalance() {
        when(mockBankInterface.getUserById("test1")).thenReturn(mockUser);
        when(mockBankInterface.getBalance("test1")).thenReturn(1000.0);

        atm.insertCard("test1");
        atm.enterPin("0034");
        atm.checkBalance();

        verify(mockBankInterface, times(1)).getBalance("test1");
    }

    @Test
    @DisplayName("Test av insättning")
    void testDeposit() {
        when(mockBankInterface.getUserById("test1")).thenReturn(mockUser);

        atm.insertCard("test1");
        atm.enterPin("0034");
        atm.depositMoney(200.0);

        verify(mockBankInterface, times(1)).deposit("test1", 200.0);
    }

    @Test
    @DisplayName("Test av uttag - Tillräcklig balans")
    void testWithdrawSufficientBalance() {
        when(mockBankInterface.getUserById("test1")).thenReturn(mockUser);
        when(mockBankInterface.getBalance("test1")).thenReturn(1000.0);
        when(mockBankInterface.withdraw("test1", 500.0)).thenReturn(true);

        atm.insertCard("test1");
        atm.enterPin("0034");

        assertTrue(atm.withdrawMoney(500.0), "Uttaget bör lyckas med tillräcklig balans");

        verify(mockBankInterface, times(1)).withdraw("test1", 500.0);
    }

    @Test
    @DisplayName("Test av uttag - Otillräcklig balans")
    void testWithdrawInsufficientBalance() {
        when(mockBankInterface.getUserById("test1")).thenReturn(mockUser);
        when(mockBankInterface.getBalance("test1")).thenReturn(1000.0);
        when(mockBankInterface.withdraw("test1", 1500.0)).thenReturn(false);

        atm.insertCard("test1");
        atm.enterPin("0034");

        assertFalse(atm.withdrawMoney(1500.0), "Uttaget bör misslyckas vid otillräcklig balans");

        verify(mockBankInterface, times(1)).withdraw("test1", 1500.0);
    }

    @Test
    @DisplayName("Test av bankens namn, E-Corp")
    void testBankName() {
        assertEquals("E-Corp ", Bank.getBankName(), "Bankens namn bör vara korrekt 💰");

    }
}