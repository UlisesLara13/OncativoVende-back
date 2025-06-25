package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetSubscriptionDto;
import com.example.OncativoVende.dtos.get.GetSubscriptionTypeDto;
import com.example.OncativoVende.dtos.get.GetShortUserDto;
import com.example.OncativoVende.dtos.post.PostSubscriptionDto;
import com.example.OncativoVende.entities.OptionEntity;
import com.example.OncativoVende.entities.SubscriptionEntity;
import com.example.OncativoVende.entities.SubscriptionTypeEntity;
import com.example.OncativoVende.entities.UserEntity;
import com.example.OncativoVende.repositores.OptionRepository;
import com.example.OncativoVende.repositores.SubscriptionRepository;
import com.example.OncativoVende.repositores.SubscriptionTypeRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;

    private final SubscriptionTypeRepository subscriptionTypeRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final OptionRepository optionRepository;

    private final RatingServiceImpl ratingService;

    @Override
    public GetSubscriptionDto createSubscription(PostSubscriptionDto postSubscriptionDto) {
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        mapSubscriptionDtoToEntity(postSubscriptionDto, subscriptionEntity);

        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscriptionEntity);
        GetSubscriptionDto getSubscriptionDto = new GetSubscriptionDto();
        mapSubscriptionEntityToDto(savedSubscription, getSubscriptionDto);

        return getSubscriptionDto;
    }

    private void mapSubscriptionDtoToEntity(PostSubscriptionDto postSubscriptionDto, SubscriptionEntity subscriptionEntity) {
        subscriptionEntity.setDiscount_applied(postSubscriptionDto.getDiscount_applied());
        subscriptionEntity.setStartDate(postSubscriptionDto.getStart_date());
        subscriptionEntity.setEndDate(postSubscriptionDto.getEnd_date());
        subscriptionEntity.setTotal_price(postSubscriptionDto.getTotal_price());
        subscriptionEntity.setUserId(getUserById(postSubscriptionDto.getUser_id()));
        subscriptionEntity.setSubscription_type_id(getSubscriptionTypeById(postSubscriptionDto.getSubscription_type_id()));
    }

    private UserEntity getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    private SubscriptionTypeEntity getSubscriptionTypeById(Integer subscriptionTypeId) {
        return subscriptionTypeRepository.findById(subscriptionTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription type not found with id: " + subscriptionTypeId));
    }

    private void mapSubscriptionEntityToDto(SubscriptionEntity subscriptionEntity, GetSubscriptionDto getSubscriptionDto) {
        getSubscriptionDto.setUser(new GetShortUserDto());
        getSubscriptionDto.setSubscription_type(new GetSubscriptionTypeDto());

        mapUserAndSubsType(getSubscriptionDto.getUser(), getSubscriptionDto.getSubscription_type(), subscriptionEntity);
        getSubscriptionDto.setId(subscriptionEntity.getId());
        getSubscriptionDto.setStart_date(subscriptionEntity.getStartDate());
        getSubscriptionDto.setEnd_date(subscriptionEntity.getEndDate());
        getSubscriptionDto.setTotal_price(subscriptionEntity.getTotal_price());
        getSubscriptionDto.setDiscount_applied(subscriptionEntity.getDiscount_applied());
    }

    private void mapUserAndSubsType(GetShortUserDto getShortUserDto, GetSubscriptionTypeDto getSubscriptionTypeDto, SubscriptionEntity subscriptionEntity) {
        getShortUserDto.setId(subscriptionEntity.getUserId().getId());
        getShortUserDto.setName(subscriptionEntity.getUserId().getName());
        getShortUserDto.setSurname(subscriptionEntity.getUserId().getSurname());
        getShortUserDto.setUsername(subscriptionEntity.getUserId().getUsername());
        getShortUserDto.setVerified(subscriptionEntity.getUserId().getVerified());
        getShortUserDto.setAvatar_url(subscriptionEntity.getUserId().getAvatar_url());
        getShortUserDto.setRating(ratingService.calculateRating(subscriptionEntity.getUserId().getId()));
        getSubscriptionTypeDto.setId(subscriptionEntity.getSubscription_type_id().getId());
        getSubscriptionTypeDto.setDescription(subscriptionEntity.getSubscription_type_id().getDescription());
        getSubscriptionTypeDto.setPrice(subscriptionEntity.getSubscription_type_id().getPrice());
    }

    @Override
    public GetSubscriptionDto getSubscriptionByUserId(Integer userId) {
        UserEntity userEntity = getUserById(userId);
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserIdAndEndDateAfter(userEntity, LocalDate.now());
        if (subscriptionEntity == null) {
            throw new EntityNotFoundException("No active subscription found for user with id: " + userId);
        }
        GetSubscriptionDto getSubscriptionDto = new GetSubscriptionDto();
        mapSubscriptionEntityToDto(subscriptionEntity, getSubscriptionDto);
        return getSubscriptionDto;
    }

    @Override
    public GetSubscriptionDto createSubscriptionByUserIdAndSubscription(String userId, String subscription, String unit_price) {

        LocalDate today = LocalDate.now();

        Long activeCount = subscriptionRepository.countActiveSubscriptions(Integer.parseInt(userId), today);
        if (activeCount > 0) {
            throw new IllegalStateException("Ya existe una suscripción activa para este usuario.");
        }

        UserEntity userEntity = userRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        SubscriptionTypeEntity subscriptionTypeEntity = subscriptionTypeRepository.findByDescription(subscription)
                .orElseThrow(() -> new EntityNotFoundException("Subscription type not found with id: " + subscription));

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setUserId(userEntity);
        subscriptionEntity.setSubscription_type_id(subscriptionTypeEntity);
        subscriptionEntity.setStartDate(LocalDate.now());
        switch (subscription) {
            case "Bronce":
                subscriptionEntity.setEndDate(LocalDate.now().plusMonths(1));
                break;
            case "Plata":
                subscriptionEntity.setEndDate(LocalDate.now().plusMonths(6));
                break;
            case "Oro":
                subscriptionEntity.setEndDate(LocalDate.now().plusMonths(12));
                break;
        }
        subscriptionEntity.setTotal_price(new BigDecimal(unit_price));
        subscriptionEntity.setDiscount_applied(calculateDiscountPercentage(subscription, new BigDecimal(unit_price)));

        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscriptionEntity);
        GetSubscriptionDto getSubscriptionDto = new GetSubscriptionDto();
        mapSubscriptionEntityToDto(savedSubscription, getSubscriptionDto);

        return getSubscriptionDto;
    }

    int calculateDiscountPercentage(String subscriptionType, BigDecimal unitPrice) {
        BigDecimal normalPrice;

        switch (subscriptionType) {
            case "Bronce":
                normalPrice = BigDecimal.valueOf(1500);
                break;
            case "Plata":
                normalPrice = BigDecimal.valueOf(7500);
                break;
            case "Oro":
                normalPrice = BigDecimal.valueOf(12000);
                break;
            default:
                throw new IllegalArgumentException("Tipo de suscripción no válido: " + subscriptionType);
        }

        if (normalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        BigDecimal discount = normalPrice.subtract(unitPrice)
                .divide(normalPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return discount.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    @Override
    public Integer getSubscriptionDiscount() {
        OptionEntity optionEntity = optionRepository.findByOptionName("subscription_discount")
                .orElseThrow(() -> new EntityNotFoundException("Option not found"));
        return optionEntity.getValue();
    }

    @Override
    public boolean updateSubscriptionDiscount(Integer discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        OptionEntity optionEntity = optionRepository.findByOptionName("subscription_discount")
                .orElseThrow(() -> new EntityNotFoundException("Option not found"));
        optionEntity.setValue(discount);
        optionRepository.save(optionEntity);
        return true;
    }
}
