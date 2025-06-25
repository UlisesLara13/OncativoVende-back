package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.dashboards.PublicationDashboardDto;
import com.example.OncativoVende.dtos.dashboards.SubscriptionDashboardDto;
import com.example.OncativoVende.dtos.dashboards.UserDashboardDto;
import com.example.OncativoVende.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboards")
@RequiredArgsConstructor
public class DashboardController {


    private final DashboardService dashboardService;

    @GetMapping("/users")
    public ResponseEntity<UserDashboardDto> getUserDashboard(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(dashboardService.getUserDashboard(from, to));
    }

    @GetMapping("/publications")
    public ResponseEntity<PublicationDashboardDto> getPublicationDashboard(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(dashboardService.getPublicationDashboard(from, to));
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<SubscriptionDashboardDto> getSubscriptionDashboard(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(dashboardService.getSubscriptionDashboard(from, to));
    }

}
