package com.sparta.deliveryi.menu.domain.service;

import org.springframework.stereotype.Service;

@Service
public class MenuDescriptionGenerator {

    private static final String PREFIX = "너는 지금 음식점 마케터야. 다른 설명없이 다음 질문에 대한 답만 해줘.";
    private static final String SUFFIX = "설명을 더 잘 팔릴 수 있게, 상품 설명을 45~60자 정도 길이로 작성해줘.";

    /**
     * 프롬프트 조합만 담당
     */
    public String buildFullPrompt(String prompt, String menuName) {
        String target = (prompt != null && !prompt.isBlank()) ? prompt : menuName;
        return PREFIX + " " + target + " " + SUFFIX;
    }
}
