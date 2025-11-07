package com.sparta.deliveryi.review.presentation.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.AssertThatUtils;
import com.sparta.deliveryi.review.application.ReviewApplication;
import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.UUID;

import static com.sparta.deliveryi.AssertThatUtils.isTrue;
import static com.sparta.deliveryi.MockUtils.mockedCustomerToken;
import static com.sparta.deliveryi.review.ReviewFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewApiTest {

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void register() throws JsonProcessingException {
        ReviewRegisterRequest request = createReviewRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        Review review = createReview(request);
        given(application.register(any(ReviewRegisterRequest.class), any(UUID.class))).willReturn(review);
        mockedCustomerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue())
                .hasPathSatisfying("$.data", AssertThatUtils.notNull());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void update() throws JsonProcessingException {
        ReviewUpdateRequest request = createReviewUpdateRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        Review review = createReview();
        given(application.update(eq(review.getId().value()), any(ReviewUpdateRequest.class), any(UUID.class))).willReturn(review);
        ;
        mockedCustomerToken();

        MvcTestResult result = mvcTester.put()
                .uri("/v1/reviews/{reviewId}", review.getId().value())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue())
                .hasPathSatisfying("$.data", AssertThatUtils.notNull());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void remove() {
        Review review = createReview();
        given(application.remove(eq(review.getId().value()), any(UUID.class))).willReturn(review);
        mockedCustomerToken();

        MvcTestResult result = mvcTester.delete()
                .uri("/v1/reviews/{reviewId}", review.getId().value())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ReviewApplication application;

}