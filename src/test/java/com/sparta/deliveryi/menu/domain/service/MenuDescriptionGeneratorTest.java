package com.sparta.deliveryi.menu.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("MenuDescriptionGenerator 단위 테스트")
class MenuDescriptionGeneratorTest {

    private final MenuDescriptionGenerator generator = new MenuDescriptionGenerator();

    @Test
    @DisplayName("prompt가 있을 때 fullPrompt 정상 생성")
    void buildFullPrompt_withPrompt() {
        String result = generator.buildFullPrompt("매운탕", "매운탕");
        assertThat(result).contains("매운탕").contains("너는 지금 음식점 마케터야");
    }

    @Test
    @DisplayName("prompt가 비어있을 때 menuName으로 대체")
    void buildFullPrompt_withoutPrompt() {
        String result = generator.buildFullPrompt(" ", "제육볶음");
        assertThat(result).contains("제육볶음");
    }
}
