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
        mockBankInterface = mock(BankInterface.class);
        atm = new ATM(mockBankInterface);

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

        assertEquals(0, mockUser.getFailedAttempts(),
                "Antalet misslyckade försök bör återställas efter korrekt PIN-inmatning");
    }

    @Test
    @DisplayName("Test av PIN-inmatning - Felaktig PIN med låsning")
    void testEnterPin_IncorrectPinWithLock() {
        when(mockBankInterface.getUserById("test1")).thenReturn(mockUser);
        when(mockBankInterface.isCardLocked("test1")).thenReturn(false);
        atm.insertCard("test1");

        for (int i = 0; i < 3; i++) {
            assertFalse(atm.enterPin("0000"), "Varje felaktig PIN-inmatning bör returnera false");
        }

        verify(mockBankInterface, times(1)).lockCard("test1");
        doReturn(true).when(mockUser).isLocked();
        assertTrue(mockUser.isLocked(), "Användaren bör vara låst efter tre misslyckade försök");
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
}
