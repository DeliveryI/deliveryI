package com.sparta.deliveryi.store.application.event;

import com.sparta.deliveryi.global.domain.Rating;
import com.sparta.deliveryi.review.application.event.RatineCalculatedEvent;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreRepository;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class StoreRatingEventHandler {

    private final StoreFinder storeFinder;

    private final StoreRepository storeRepository;

    @Async
    @TransactionalEventListener(RatineCalculatedEvent.class)
    public void handle(RatineCalculatedEvent event){
        StoreId storeId = StoreId.of(event.storeId());

        Store store = storeFinder.find(storeId);

        store.updateRating(Rating.of(event.rating()));

        storeRepository.save(store);
    }
}
