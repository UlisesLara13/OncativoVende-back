package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetSubscriptionDto;
import com.example.OncativoVende.dtos.post.PostSubscriptionDto;
import org.springframework.stereotype.Service;

@Service
public interface SubscriptionService {

    GetSubscriptionDto createSubscription(PostSubscriptionDto postSubscriptionDto);

    GetSubscriptionDto getSubscriptionByUserId(Integer userId);

    GetSubscriptionDto createSubscriptionByUserIdAndSubscription(String userId, String subscription, String unit_price);

    Integer getSubscriptionDiscount();

    boolean updateSubscriptionDiscount(Integer discount);
}
