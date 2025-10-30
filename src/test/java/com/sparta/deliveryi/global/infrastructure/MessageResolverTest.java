package com.sparta.deliveryi.global.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageResolverTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MessageResolver messageResolver;

    private static final Locale KO_LOCALE = Locale.of("ko");

    @BeforeEach
    void setUp() {
        // @PostConstruct 메서드를 수동으로 호출하여 static 필드 초기화
        messageResolver.initialize();
    }

    @Test
    void getMessageWithMessageCode() {
        String messageCode = "test.message";
        String expectedMessage = "테스트 메시지";
        when(messageSource.getMessage(messageCode, null, KO_LOCALE))
                .thenReturn(expectedMessage);

        String actualMessage = MessageResolver.getMessage(messageCode);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void getMessageWithMessageCodeAndParams() {
        String messageCode = "test.message.with.params";
        Object[] params = {"사용자", 100};
        String expectedMessage = "사용자님의 포인트는 100점입니다";
        when(messageSource.getMessage(messageCode, params, KO_LOCALE))
                .thenReturn(expectedMessage);

        String actualMessage = MessageResolver.getMessage(messageCode, params);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void getMessageThrowException() {
        String messageCode = "non.existent.code";
        when(messageSource.getMessage(messageCode, null, KO_LOCALE))
                .thenThrow(new NoSuchMessageException(messageCode));

        assertThatThrownBy(() -> MessageResolver.getMessage(messageCode))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void hasFormatParametersWhenFormatPlaceholdersExist() {
        String messageCode = "test.message.with.format";
        String messageWithFormat = "{0}님 안녕하세요. {1}개의 알림이 있습니다.";
        when(messageSource.getMessage(messageCode, null, KO_LOCALE))
                .thenReturn(messageWithFormat);

        boolean result = MessageResolver.hasFormatParameters(messageCode);

        assertThat(result).isTrue();
    }

    @Test
    void hasFormatParametersWhenNoFormatPlaceholders() {
        String messageCode = "test.message.without.format";
        String messageWithoutFormat = "일반 메시지입니다.";
        when(messageSource.getMessage(messageCode, null, KO_LOCALE))
                .thenReturn(messageWithoutFormat);

        boolean result = MessageResolver.hasFormatParameters(messageCode);

        assertThat(result).isFalse();
    }

    @Test
    void hasFormatParametersWhenMessageCodeDoesNotExist() {
        String messageCode = "non.existent.code";
        when(messageSource.getMessage(messageCode, null, KO_LOCALE))
                .thenThrow(new NoSuchMessageException(messageCode));

        boolean result = MessageResolver.hasFormatParameters(messageCode);

        assertThat(result).isFalse();
    }

    @Test
    void getMessageWhenEmptyParamsArrayProvided() {
        String messageCode = "test.message";
        Object[] emptyParams = {};
        String expectedMessage = "테스트 메시지";
        when(messageSource.getMessage(messageCode, emptyParams, KO_LOCALE))
                .thenReturn(expectedMessage);

        String actualMessage = MessageResolver.getMessage(messageCode, emptyParams);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void getMessageWhenNullParamProvided() {
        String messageCode = "test.message";
        Object[] nullParams = {null};
        String expectedMessage = "null 값을 포함한 메시지";
        when(messageSource.getMessage(messageCode, nullParams, KO_LOCALE))
                .thenReturn(expectedMessage);

        String actualMessage = MessageResolver.getMessage(messageCode, nullParams);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
