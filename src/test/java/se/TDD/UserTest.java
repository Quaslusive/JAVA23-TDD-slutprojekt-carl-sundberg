package se.TDD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {

        user = new User("12345", "1234", 1000.0);
    }

    @Test
    @DisplayName("Testa verifiering av PIN - Korrekt PIN")
    void testVerifyPinCorrect() {
        assertTrue(user.verifyPin("1234"), "Korrekt PIN bör returnera true");
    }

    @Test
    @DisplayName("Testa verifiering av PIN - Felaktig PIN")
    void testVerifyPinIncorrect() {
        assertFalse(user.verifyPin("wrong"), "Felaktig PIN bör returnera false");
    }

    @Test
    @DisplayName("Testa ökning av misslyckade försök")
    void testIncrementFailedAttempts() {
        user.incrementFailedAttempts();
        user.incrementFailedAttempts();
        user.incrementFailedAttempts();
        assertEquals(3, user.getFailedAttempts(), "Antalet misslyckade försök bör ökas korrekt");
    }

    @Test
    @DisplayName("Testa återställning av misslyckade försök")
    void testResetFailedAttempts() {
        user.incrementFailedAttempts();
        user.incrementFailedAttempts();
        user.resetFailedAttempts();
        assertEquals(0, user.getFailedAttempts(), "Antalet misslyckade försök bör återställas till 0");
    }

    @Test
    @DisplayName("Testa låsning av kort")
    void testLockCard() {
        user.lockCard();
        assertTrue(user.isLocked(), "Kortet bör vara låst efter att lockCard() har anropats");
    }
/*
    @Test
    @DisplayName("Testa insättning - Positivt belopp")
    void testDeposit_PositiveAmount() {
        user.deposit(500.0);
        assertEquals(1500.0, user.getBalance(), "Balansen bör uppdateras korrekt efter insättning");
    }

    @Test
    @DisplayName("Testa uttag - Tillräcklig balans")
    void testWithdraw_SufficientBalance() {
        assertTrue(user.withdraw(500.0), "Uttaget bör lyckas med tillräcklig balans");
        assertEquals(500.0, user.getBalance(), "Balansen bör uppdateras korrekt efter uttag");
    }

    @Test
    @DisplayName("Testa uttag - Otillräcklig balans")
    void testWithdraw_InsufficientBalance() {
        assertFalse(user.withdraw(1500.0), "Uttaget bör misslyckas med otillräcklig balans");
        assertEquals(1000.0, user.getBalance(), "Balansen bör förbli oförändrad efter misslyckat uttag");
    }*/
}
