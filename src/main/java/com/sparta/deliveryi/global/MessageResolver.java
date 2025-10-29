package com.sparta.deliveryi.global;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component
public class MessageResolver {
    private final MessageSource source;

    private static final String LANG_CD = "ko";

    private static MessageSource messageSource;

    private static Locale locale;

    @Autowired
    public MessageResolver(MessageSource source) {
        this.source = source;
    }

    /**
     * 초기화 메서드로 내부 messageSource와 locale을 설정한다.
     */
    @PostConstruct
    public synchronized void initialize() {
        messageSource = source;
        locale = Locale.of(LANG_CD);
    }

    /**
     * messageCode에 해당하는 message 반환 메서드.
     *
     * @param messageCode 가져올 message의 key 값
     * @return messageCode에 해당하는 message
     */
    public static String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, null, locale);
    }

    /**
     * messageCode에 해당하는 message 반환 메서드.
     *
     * @param messageCode 가져올 message의 key 값
     * @param messageArguments 가져올 message에 필요한 매개변수
     * @return messageCode에 해당하는 message
     */
    public static String getMessage(String messageCode, Object... messageArguments) {
        return messageSource.getMessage(messageCode, messageArguments, locale);
    }

    public static boolean hasFormatParameters(String messageCode) {
        try {
            String rawMessage = messageSource.getMessage(messageCode, null, locale);
            MessageFormat format = new MessageFormat(rawMessage, locale);
            return format.getFormats().length > 0;
        } catch (NoSuchMessageException e) {
            return false;
        }
    }
}
