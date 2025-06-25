package com.example.OncativoVende.dtos.dashboards;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDashboardDto {
    private Long totalUsers;

    private Long activeUsers;
    private Long inactiveUsers;

    private Long premiumUsers;
    private Long standardUsers;

    private Long verifiedUsers;
    private Long unverifiedUsers;

    private List<LocationCountDto> usersByLocation;
}
