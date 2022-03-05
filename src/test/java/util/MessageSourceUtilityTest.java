package util;

import com.has_to_be.csms.util.MessageSourceUtility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MessageSourceUtilityTest {

    @Test
    void getExceptionMessage_messageBySpecifiedKey_foundCorrespondingValue() throws Exception {
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        String messageStub = "message found by key";
        when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class)))
                .thenReturn(messageStub);
        String message = MessageSourceUtility
                .getMessageByMessageKey(messageSource, "messageKeyStub", Mockito.mock(Locale.class));
        assertNotNull(message);
        assertEquals(message, messageStub);
        Mockito.verify(messageSource).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void getExceptionMessage_messageBySpecifiedKey_returnDefaultNotFoundMessage() throws Exception {
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        String missingMessageStub = "message not found by key";
        when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class)))
                .thenThrow(NoSuchMessageException.class);
        when(messageSource.getMessage(Mockito.eq(MessageSourceUtility.MESSAGE_MISSING_IN_BUNDLE),
                Mockito.any(), Mockito.any(Locale.class)))
                .thenReturn(missingMessageStub);
        String message = MessageSourceUtility
                .getMessageByMessageKey(messageSource, "messageKeyStub",
                        Mockito.mock(Locale.class));
        assertNotNull(message);
        assertEquals(message, missingMessageStub);
        Mockito.verify(messageSource, Mockito.times(2))
                .getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void getExceptionMessage_messageBySpecifiedKeyAndArgument_returnDefaultNotFoundMessage() throws Exception {
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        String messageStub = "message found by key";
        when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class)))
                .thenReturn(messageStub);
        String message = MessageSourceUtility.
                getMessageByMessageKey(messageSource, "messageKeyStub", new Object[]{},
                        Mockito.mock(Locale.class));
        assertNotNull(message);
        assertEquals(message, messageStub);
        Mockito.verify(messageSource).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void getExceptionMessage_messageBySpecifiedKeyAndArgument_foundCorrespondingValue() throws Exception {
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        String missingMessageStub = "message not found by key";
        when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class))).
                thenThrow(NoSuchMessageException.class);
        when(messageSource.getMessage(Mockito.eq(MessageSourceUtility.MESSAGE_MISSING_IN_BUNDLE),
                Mockito.any(), Mockito.any(Locale.class))).
                thenReturn(missingMessageStub);
        String message = MessageSourceUtility.
                getMessageByMessageKey(messageSource, "messageKeyStub", new Object[]{},
                        Mockito.mock(Locale.class));
        assertNotNull(message);
        assertEquals(message, missingMessageStub);
        Mockito.verify(messageSource, Mockito.times(2)).
                getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

}

