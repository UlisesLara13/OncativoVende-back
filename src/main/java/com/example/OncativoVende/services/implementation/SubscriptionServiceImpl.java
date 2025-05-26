package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetSubscriptionDto;
import com.example.OncativoVende.dtos.get.GetSubscriptionTypeDto;
import com.example.OncativoVende.dtos.get.GetShortUserDto;
import com.example.OncativoVende.dtos.post.PostSubscriptionDto;
import com.example.OncativoVende.entities.SubscriptionEntity;
import com.example.OncativoVende.entities.SubscriptionTypeEntity;
import com.example.OncativoVende.entities.UserEntity;
import com.example.OncativoVende.repositores.SubscriptionRepository;
import com.example.OncativoVende.repositores.SubscriptionTypeRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;

    private final SubscriptionTypeRepository subscriptionTypeRepository;

    private final SubscriptionRepository subscriptionRepository;

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
    public GetSubscriptionDto createSubscriptionByUserIdAndSubscription(String userId, String subscription){
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
        subscriptionEntity.setTotal_price(subscriptionTypeEntity.getPrice());
        subscriptionEntity.setDiscount_applied(0);

        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscriptionEntity);
        GetSubscriptionDto getSubscriptionDto = new GetSubscriptionDto();
        mapSubscriptionEntityToDto(savedSubscription, getSubscriptionDto);

        return getSubscriptionDto;
    }
}
