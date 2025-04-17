package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetSubscriptionDto;
import com.example.OncativoVende.dtos.post.PostSubscriptionDto;
import com.example.OncativoVende.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<GetSubscriptionDto> createSubscription(@RequestBody PostSubscriptionDto postSubscriptionDto) {
        GetSubscriptionDto result = subscriptionService.createSubscription(postSubscriptionDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }
}
