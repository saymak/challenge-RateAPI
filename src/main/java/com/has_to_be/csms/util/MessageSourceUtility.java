package com.has_to_be.csms.util;


import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@Log4j2
public final class MessageSourceUtility {
    public static final String MESSAGE_MISSING_IN_BUNDLE = "message.missing.in.bundle";

    private MessageSourceUtility() {
    }

    public static String getMessageByMessageKey(MessageSource messageSource,
                                                String messageKey,
                                                Locale locale) {
        try {
            return messageSource.getMessage(messageKey, null, locale);
        } catch (NoSuchMessageException e) {
            log.error("exception translation not found", e);
            return messageSource.getMessage(MESSAGE_MISSING_IN_BUNDLE, null, locale);
        }
    }

    public static String getMessageByMessageKey(MessageSource messageSource,
                                                String messageKey,
                                                Object[] args,
                                                Locale locale) {
        try {
            return messageSource.getMessage(messageKey, args, locale);
        } catch (NoSuchMessageException e) {
            log.error("exception translation not found", e);
            return messageSource.getMessage(MESSAGE_MISSING_IN_BUNDLE, args, locale);
        }
    }

}
