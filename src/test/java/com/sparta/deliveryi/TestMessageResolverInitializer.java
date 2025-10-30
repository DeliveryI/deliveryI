package com.sparta.deliveryi;

import com.sparta.deliveryi.global.infrastructure.MessageResolver;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;

public class TestMessageResolverInitializer {
    public static void initializeFromResourceBundle() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        // messages.properties 파일 로드
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");

        ReflectionTestUtils.setField(
                MessageResolver.class,
                "messageSource",
                messageSource
        );
    }
}
