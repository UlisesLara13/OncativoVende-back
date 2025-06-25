package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.dashboards.*;
import com.example.OncativoVende.repositores.*;
import com.example.OncativoVende.services.DashboardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final PublicationRepository publicationRepository;
    private final PublicationCategoryRepository publicationCategoryRepository;
    private final PublicationTagRepository publicationTagRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public UserDashboardDto getUserDashboard(LocalDate from, LocalDate to) {
        boolean hasDates = from != null && to != null;

        Long total = hasDates
                ? userRepository.countByCreatedAtBetween(from, to)
                : userRepository.count();

        Long active = hasDates
                ? userRepository.countByActiveAndCreatedAtBetween(true, from, to)
                : userRepository.countByActive(true);

        Long inactive = hasDates
                ? userRepository.countByActiveAndCreatedAtBetween(false, from, to)
                : userRepository.countByActive(false);

        Long premium = hasDates
                ? userRepository.countByRoleNameBetweenDates("PREMIUM", from, to)
                : userRepository.countByRoleName("PREMIUM");

        Long standard = hasDates
                ? userRepository.countStandardUsersOnlyBetweenDates(from, to)
                : userRepository.countStandardUsersOnly();

        Long verified = hasDates
                ? userRepository.countByVerifiedAndCreatedAtBetween(true, from, to)
                : userRepository.countByVerified(true);

        Long unverified = hasDates
                ? userRepository.countByVerifiedAndCreatedAtBetween(false, from, to)
                : userRepository.countByVerified(false);

        List<Object[]> locationRaw = hasDates
                ? userRepository.countUsersByLocationBetweenDates(from, to)
                : userRepository.countUsersByLocationAllTime();

        List<LocationCountDto> locations = locationRaw.stream()
                .map(obj -> new LocationCountDto((String) obj[0], (Long) obj[1]))
                .toList();

        return UserDashboardDto.builder()
                .totalUsers(total)
                .activeUsers(active)
                .inactiveUsers(inactive)
                .premiumUsers(premium)
                .standardUsers(standard)
                .verifiedUsers(verified)
                .unverifiedUsers(unverified)
                .usersByLocation(locations)
                .build();
    }

    @Override
    public PublicationDashboardDto getPublicationDashboard(LocalDate from, LocalDate to) {
        boolean hasDates = from != null && to != null;

        Long total = hasDates
                ? publicationRepository.countByCreatedAtBetween(from, to)
                : publicationRepository.count();

        Long active = hasDates
                ? publicationRepository.countByActiveAndCreatedAtBetween(true, from, to)
                : publicationRepository.countByActive(true);

        Long inactive = hasDates
                ? publicationRepository.countByActiveAndCreatedAtBetween(false, from, to)
                : publicationRepository.countByActive(false);

        Long views = hasDates
                ? publicationRepository.getTotalViewsBetweenDates(from, to)
                : publicationRepository.getTotalViews();

        if (views == null) {
            views = 0L;
        }

        BigDecimal avgPrice = hasDates
                ? publicationRepository.getAveragePriceBetweenDates(from, to)
                : publicationRepository.getAveragePrice();

        if (avgPrice == null) {
            avgPrice = BigDecimal.ZERO;
        }

        List<Object[]> catRaw = publicationCategoryRepository.countByCategoryBetweenDates(from, to);
        List<LabelCountDto> categories = catRaw.stream()
                .map(obj -> new LabelCountDto((String) obj[0], (Long) obj[1]))
                .toList();

        List<Object[]> tagRaw = publicationTagRepository.countByTagBetweenDates(from, to);
        List<LabelCountDto> tags = tagRaw.stream()
                .map(obj -> new LabelCountDto((String) obj[0], (Long) obj[1]))
                .toList();

        List<Object[]> locRaw = publicationRepository.countByLocation(from, to);
        List<LabelCountDto> locations = locRaw.stream()
                .map(obj -> new LabelCountDto((String) obj[0], (Long) obj[1]))
                .toList();

        return PublicationDashboardDto.builder()
                .totalPublications(total)
                .activePublications(active)
                .inactivePublications(inactive)
                .totalViews(views)
                .averagePrice(avgPrice.setScale(2, BigDecimal.ROUND_HALF_UP))
                .publicationsByCategory(categories)
                .publicationsByTag(tags)
                .publicationsByLocation(locations)
                .build();
    }

    @Override
    public SubscriptionDashboardDto getSubscriptionDashboard(LocalDate from, LocalDate to) {
        boolean hasDates = from != null && to != null;
        LocalDate today = LocalDate.now();

        Long total = hasDates
                ? subscriptionRepository.countByStartDateBetween(from, to)
                : subscriptionRepository.count();

        BigDecimal revenue = hasDates
                ? subscriptionRepository.sumRevenueByStartDateBetween(from, to)
                : subscriptionRepository.sumRevenueByStartDateBetween(LocalDate.MIN, today);

        Long withDiscount = hasDates
                ? subscriptionRepository.countWithDiscount(from, to)
                : subscriptionRepository.countWithDiscount(LocalDate.MIN, today);

        Long withoutDiscount = hasDates
                ? subscriptionRepository.countWithoutDiscount(from, to)
                : subscriptionRepository.countWithoutDiscount(LocalDate.MIN, today);

        Long active = hasDates
                ? subscriptionRepository.countActiveSubscriptions(from, to, today)
                : subscriptionRepository.countActiveSubscriptions(LocalDate.MIN, today, today);

        Long inactive = hasDates
                ? subscriptionRepository.countInactiveSubscriptions(from, to, today)
                : subscriptionRepository.countInactiveSubscriptions(LocalDate.MIN, today, today);

        List<Object[]> rawMonthly = hasDates
                ? subscriptionRepository.countSubscriptionsByMonth(from, to)
                : subscriptionRepository.countSubscriptionsByMonth(today.withDayOfYear(1), today);

        Map<Integer, Long> monthlyMap = rawMonthly.stream()
                .collect(Collectors.toMap(
                        obj -> (Integer) obj[0],
                        obj -> (Long) obj[1]
                ));

        List<SubscriptionMonthlyCountDto> monthly = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {
                    String monthName = capitalize(Month.of(month).getDisplayName(TextStyle.FULL, new Locale("es")));
                    Long count = monthlyMap.getOrDefault(month, 0L);
                    return new SubscriptionMonthlyCountDto(monthName, count);
                })
                .toList();

        return SubscriptionDashboardDto.builder()
                .totalSubscriptions(total)
                .totalRevenue(revenue != null ? revenue : BigDecimal.ZERO)
                .withDiscount(withDiscount)
                .withoutDiscount(withoutDiscount)
                .yearAnalize(String.valueOf(from != null && to != null ? from.getYear() : LocalDate.now().getYear()))
                .activeSubscriptions(active)
                .inactiveSubscriptions(inactive)
                .subscriptionsByMonth(monthly)
                .build();
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
