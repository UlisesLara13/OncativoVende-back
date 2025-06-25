package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.dashboards.PublicationDashboardDto;
import com.example.OncativoVende.dtos.dashboards.SubscriptionDashboardDto;
import com.example.OncativoVende.dtos.dashboards.UserDashboardDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface DashboardService {

    UserDashboardDto getUserDashboard(LocalDate from, LocalDate to);

    PublicationDashboardDto getPublicationDashboard(LocalDate from, LocalDate to);

    SubscriptionDashboardDto getSubscriptionDashboard(LocalDate from, LocalDate to);

}
