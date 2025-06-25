package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetSubscriptionDto;
import com.example.OncativoVende.dtos.post.PostSubscriptionDto;
import com.example.OncativoVende.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<GetSubscriptionDto> getSubscriptionByUserId(@PathVariable Integer userId) {
        GetSubscriptionDto subscription = subscriptionService.getSubscriptionByUserId(userId);
        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(subscription);
    }

    @GetMapping("/discount")
    public ResponseEntity<Integer> getSubscriptionDiscount() {
        Integer discount = subscriptionService.getSubscriptionDiscount();
        if (discount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(discount);
    }

    @PutMapping("/discount")
    public ResponseEntity<Boolean> updateSubscriptionDiscount(@RequestParam Integer discount) {
        boolean updated = subscriptionService.updateSubscriptionDiscount(discount);
        if (!updated) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(true);
    }

}
