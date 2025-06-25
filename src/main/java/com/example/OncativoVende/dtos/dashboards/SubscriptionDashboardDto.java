package com.example.OncativoVende.dtos.dashboards;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDashboardDto {

    private Long totalSubscriptions;
    private BigDecimal totalRevenue;

    private Long withDiscount;
    private Long withoutDiscount;

    private Long activeSubscriptions;
    private Long inactiveSubscriptions;

    private String yearAnalize;

    private List<SubscriptionMonthlyCountDto> subscriptionsByMonth;
}
