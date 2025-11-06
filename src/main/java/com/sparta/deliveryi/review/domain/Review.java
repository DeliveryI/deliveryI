package com.sparta.deliveryi.review.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import com.sparta.deliveryi.global.domain.Rating;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Table(name = "p_revie")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private UUID storeId;

    @Column(nullable = false)
    private UUID orderId;

    @Embedded
    private Reviewer reviewer;

    @Embedded
    private Rating rating;

    private String content;

    public static Review register(ReviewRegisterRequest registerRequest) {
        Review order = new Review();

        order.storeId = requireNonNull(registerRequest.storeId());
        order.orderId = requireNonNull(registerRequest.orderId());
        order.reviewer = Reviewer.of(registerRequest.reviewerId());
        order.rating = Rating.of(registerRequest.rating());
        order.content = requireNonNull(registerRequest.content());

        return order;
    }

    public void update(ReviewUpdateRequest updateRequest) {
        this.rating = Rating.of(updateRequest.rating());
        this.content = updateRequest.content();
    }

    public void remove() {
        super.delete();
    }

    public ReviewId getId() {
        return ReviewId.of(id);
    }

}
