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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        atm = new ATM(mockBankInterface);

    }

/*    void testCardInsertion() {
        String cardID = "1234";
        User mockUser = new User(cardID,"123",100.0);

        when(mockBankInterface.getUserById(cardID)).thenReturn(mockUser);

        assertEquals("123", atm.insertCard(cardID), "User not inserted correctly");
        verify(mockBankInterface, times(1)).getUserById(cardID);

    }*/

}