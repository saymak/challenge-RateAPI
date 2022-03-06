package util;

import com.has_to_be.csms.util.MessageSourceUtility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static com.has_to_be.csms.util.MessageSourceUtility.getMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MessageSourceUtilityTest {

    private MessageSource underTest = Mockito.mock(MessageSource.class);

    @Test
    void getMessage_messageBySpecifiedKey_foundCorrespondingValue() {
        String givenMessageStub = "message found by key";
        when(underTest.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class)))
                .thenReturn(givenMessageStub);

        String result = getMessage(underTest, "messageKeyStub", Mockito.mock(Locale.class));

        assertNotNull(result);
        assertEquals(result, givenMessageStub);
        Mockito.verify(underTest).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void getMessage_messageBySpecifiedKey_returnDefaultNotFoundMessage() {
        String missingMessageStub = "message not found by key";
        when(underTest.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class)))
                .thenThrow(NoSuchMessageException.class);
        when(underTest.getMessage(Mockito.eq(MessageSourceUtility.MESSAGE_MISSING_IN_BUNDLE),
                Mockito.any(), Mockito.any(Locale.class)))
                .thenReturn(missingMessageStub);

        String result = getMessage(underTest, "messageKeyStub",
                Mockito.mock(Locale.class));

        assertNotNull(result);
        assertEquals(result, missingMessageStub);
        Mockito.verify(underTest, Mockito.times(2))
                .getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void getMessage_messageBySpecifiedKeyAndArgument_returnDefaultNotFoundMessage() {
        String givenMessageStub = "message found by key";
        when(underTest.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class)))
                .thenReturn(givenMessageStub);

        String result = getMessage(underTest, "messageKeyStub", Mockito.mock(Locale.class), new Object[]{});

        assertNotNull(result);
        assertEquals(result, givenMessageStub);
        Mockito.verify(underTest).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void getMessage_messageBySpecifiedKeyAndArgument_foundCorrespondingValue() {
        String missingMessageStub = "message not found by key";
        when(underTest.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class))).
                thenThrow(NoSuchMessageException.class);
        when(underTest.getMessage(Mockito.eq(MessageSourceUtility.MESSAGE_MISSING_IN_BUNDLE),
                Mockito.any(), Mockito.any(Locale.class))).
                thenReturn(missingMessageStub);

        String result = getMessage(underTest, "messageKeyStub", Mockito.mock(Locale.class), new Object[]{});

        assertNotNull(result);
        assertEquals(result, missingMessageStub);
        Mockito.verify(underTest, Mockito.times(2)).
                getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

}

