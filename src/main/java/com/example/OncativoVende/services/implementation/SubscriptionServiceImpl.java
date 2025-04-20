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
        subscriptionEntity.setStart_date(postSubscriptionDto.getStart_date());
        subscriptionEntity.setEnd_date(postSubscriptionDto.getEnd_date());
        subscriptionEntity.setTotal_price(postSubscriptionDto.getTotal_price());
        subscriptionEntity.setUser_id(getUserById(postSubscriptionDto.getUser_id()));
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
        getSubscriptionDto.setStart_date(subscriptionEntity.getStart_date());
        getSubscriptionDto.setEnd_date(subscriptionEntity.getEnd_date());
        getSubscriptionDto.setTotal_price(subscriptionEntity.getTotal_price());
        getSubscriptionDto.setDiscount_applied(subscriptionEntity.getDiscount_applied());
    }

    private void mapUserAndSubsType(GetShortUserDto getShortUserDto, GetSubscriptionTypeDto getSubscriptionTypeDto, SubscriptionEntity subscriptionEntity) {
        getShortUserDto.setId(subscriptionEntity.getUser_id().getId());
        getShortUserDto.setName(subscriptionEntity.getUser_id().getName());
        getShortUserDto.setSurname(subscriptionEntity.getUser_id().getSurname());
        getShortUserDto.setVerified(subscriptionEntity.getUser_id().getVerified());
        getShortUserDto.setAvatar_url(subscriptionEntity.getUser_id().getAvatar_url());
        getShortUserDto.setRating(ratingService.calculateRating(subscriptionEntity.getUser_id().getId()));
        getSubscriptionTypeDto.setId(subscriptionEntity.getSubscription_type_id().getId());
        getSubscriptionTypeDto.setDescription(subscriptionEntity.getSubscription_type_id().getDescription());
        getSubscriptionTypeDto.setPrice(subscriptionEntity.getSubscription_type_id().getPrice());
    }
}
