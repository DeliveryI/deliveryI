package com.sparta.deliveryi.ai.infrastructure.cilent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

@DisplayName("GeminiClient 단위 테스트")
class GeminiClientTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GeminiClient client = new GeminiClient(objectMapper);

    @Test
    @DisplayName("GeminiResponse JSON 문자열을 정상적으로 텍스트로 추출")
    void extractTextFromResponse_success() throws Exception {
        String json = """
        {
          "candidates": [
            {
              "content": {
                "parts": [
                  {"text": "맛있고 건강한 전복죽!"}
                ]
              }
            }
          ]
        }
        """;

        // private 메서드 접근 (Reflection)
        var method = GeminiClient.class.getDeclaredMethod("extractTextFromResponse", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(client, json);

        assertThat(result).isEqualTo("맛있고 건강한 전복죽!");
    }
}
