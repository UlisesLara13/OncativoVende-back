package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetContactDto;
import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.get.GetShortUserDto;
import com.example.OncativoVende.dtos.post.PostContact;
import com.example.OncativoVende.dtos.post.PostPublicationDto;
import com.example.OncativoVende.entities.*;
import com.example.OncativoVende.repositores.*;
import com.example.OncativoVende.services.PublicationService;
import com.example.OncativoVende.services.RatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;

    private final LocationRepository locationRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final PublicationCategoryRepository publicationCategoryRepository;

    private final TagRepository tagRepository;

    private final PublicationTagRepository publicationTagRepository;

    private final PublicationImageRepository publicationImageRepository;

    private final ContactTypeRepository contactTypeRepository;

    private final RatingService ratingService;

    private final ContactRepository contactRepository;

    @Override
    public GetPublicationDto createPublication(PostPublicationDto postPublicationDto) {
        PublicationEntity publicationEntity = new PublicationEntity();

        mapPublicationDtoToEntity(postPublicationDto, publicationEntity);
        publicationEntity = publicationRepository.save(publicationEntity);

        createCategories(postPublicationDto, publicationEntity);
        createTags(postPublicationDto, publicationEntity);
        createImages(postPublicationDto, publicationEntity);
        createContacts(postPublicationDto, publicationEntity);

        GetPublicationDto getPublicationDto = new GetPublicationDto();
        mapPublicationEntityToDto(publicationEntity, getPublicationDto);

        return getPublicationDto;
    }

    @Override
    public List<GetPublicationDto> getAllPublications() {
        List<PublicationEntity> publicationEntities = publicationRepository.findAll();
        List<GetPublicationDto> getPublicationDtos = publicationEntities.stream()
                .map(publicationEntity -> {
                    GetPublicationDto getPublicationDto = new GetPublicationDto();
                    mapPublicationEntityToDto(publicationEntity, getPublicationDto);
                    return getPublicationDto;
                })
                .toList();
        return getPublicationDtos;
    }

    @Override
    public GetPublicationDto getPublicationById(Integer id) {
        PublicationEntity publicationEntity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + id));
        GetPublicationDto getPublicationDto = new GetPublicationDto();
        mapPublicationEntityToDto(publicationEntity, getPublicationDto);
        return getPublicationDto;
    }

    @Override
    @Transactional
    public GetPublicationDto updatePublication(Integer id, PostPublicationDto postPublicationDto) {
        PublicationEntity publicationEntity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + id));


        publicationCategoryRepository.deleteAllByPublicationId(publicationEntity.getId());
        publicationTagRepository.deleteAllByPublicationId(publicationEntity.getId());
        publicationImageRepository.deleteAllByPublicationId(publicationEntity.getId());
        contactRepository.deleteAllByPublicationId(publicationEntity.getId());

        mapPublicationDtoToEntity(postPublicationDto, publicationEntity);
        publicationEntity = publicationRepository.save(publicationEntity);

        createCategories(postPublicationDto, publicationEntity);
        createTags(postPublicationDto, publicationEntity);
        createImages(postPublicationDto, publicationEntity);
        createContacts(postPublicationDto, publicationEntity);

        GetPublicationDto getPublicationDto = new GetPublicationDto();
        mapPublicationEntityToDto(publicationEntity, getPublicationDto);

        return getPublicationDto;
    }

    @Override
    public void deletePublication(Integer id) {
        PublicationEntity publicationEntity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + id));

        publicationEntity.setActive(Boolean.FALSE);
        publicationRepository.save(publicationEntity);
//        publicationCategoryRepository.deleteAllByPublicationId(publicationEntity.getId());
//        publicationTagRepository.deleteAllByPublicationId(publicationEntity.getId());
//        publicationImageRepository.deleteAllByPublicationId(publicationEntity.getId());
//        contactRepository.deleteAllByPublicationId(publicationEntity.getId());
//
//        publicationRepository.delete(publicationEntity);
//
// TODO: Si se desea borrar las tablas intermedias descomentar esto
    }

    public void mapPublicationDtoToEntity(PostPublicationDto postPublicationDto, PublicationEntity publicationEntity) {
        publicationEntity.setTitle(postPublicationDto.getTitle());
        publicationEntity.setDescription(postPublicationDto.getDescription());
        publicationEntity.setPrice(postPublicationDto.getPrice());
        publicationEntity.setLocation_id(locationRepository.findById(postPublicationDto.getLocation_id())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + postPublicationDto.getLocation_id())));
        publicationEntity.setUser(userRepository.findById(postPublicationDto.getUser_id())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + postPublicationDto.getUser_id())));
        publicationEntity.setActive(Boolean.TRUE);
    }

    public void createCategories(PostPublicationDto postPublicationDto , PublicationEntity publicationEntity) {
        for (Integer category_id : postPublicationDto.getCategories()) {
            PublicationCategoryEntity publicationCategoryEntity = new PublicationCategoryEntity();
            publicationCategoryEntity.setPublication(publicationEntity);
            publicationCategoryEntity.setCategoryEntity(
                    categoryRepository.findById(category_id)
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + category_id)));
            publicationCategoryRepository.save(publicationCategoryEntity);
        }

    }

    public void createTags(PostPublicationDto postPublicationDto , PublicationEntity publicationEntity) {
        for (Integer tag_id : postPublicationDto.getTags()) {
            PublicationTagEntity publicationTagEntity = new PublicationTagEntity();
            publicationTagEntity.setPublication(publicationEntity);
            publicationTagEntity.setTag(
                    tagRepository.findById(tag_id)
                            .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tag_id)));
            publicationTagRepository.save(publicationTagEntity);
        }

    }

    public void createImages(PostPublicationDto postPublicationDto , PublicationEntity publicationEntity) {
        for (String image_url : postPublicationDto.getImages()) {
            PublicationImageEntity publicationImageEntity = new PublicationImageEntity();
            publicationImageEntity.setPublication(publicationEntity);
            publicationImageEntity.setImage_url(image_url);
            publicationImageRepository.save(publicationImageEntity);
        }

    }

    public void createContacts(PostPublicationDto postPublicationDto , PublicationEntity publicationEntity) {
        for (PostContact contact : postPublicationDto.getContacts()) {
            ContactEntity contactEntity = new ContactEntity();
            contactEntity.setPublication(publicationEntity);
            contactEntity.setContact_type(contactTypeRepository.findById(contact.getContact_type_id())
                    .orElseThrow(() -> new EntityNotFoundException("Contact type not found with id: " + contact.getContact_type_id())));
            contactEntity.setContact_value(contact.getContact_value());
            contactRepository.save(contactEntity);
        }
    }

    public void mapPublicationEntityToDto(PublicationEntity publicationEntity, GetPublicationDto getPublicationDto) {
        getPublicationDto.setId(publicationEntity.getId());
        getPublicationDto.setTitle(publicationEntity.getTitle());
        getPublicationDto.setDescription(publicationEntity.getDescription());
        getPublicationDto.setPrice(publicationEntity.getPrice());
        getPublicationDto.setLocation(publicationEntity.getLocation_id().getDescription());
        GetShortUserDto getShortUserDto = new GetShortUserDto();
        mapShortUserEntityToDto(publicationEntity.getUser(), getShortUserDto);
        getPublicationDto.setUser(getShortUserDto);
        getPublicationDto.setActive(publicationEntity.getActive());
        getPublicationDto.setCategories(mapCategoriesToDto(publicationEntity));
        getPublicationDto.setTags(mapTagsToDto(publicationEntity));
        getPublicationDto.setContacts(mapContactsToDto(publicationEntity));
        getPublicationDto.setImages(mapImagesToDto(publicationEntity));

    }

    public void mapShortUserEntityToDto(UserEntity userEntity, GetShortUserDto getShortUserDto) {
        getShortUserDto.setId(userEntity.getId());
        getShortUserDto.setName(userEntity.getName());
        getShortUserDto.setSurname(userEntity.getSurname());
        getShortUserDto.setVerified(userEntity.getVerified());
        getShortUserDto.setRating(ratingService.calculateRating(userEntity.getId()));
        getShortUserDto.setAvatar_url(userEntity.getAvatar_url());

    }

    public List<String> mapCategoriesToDto(PublicationEntity publicationEntity) {
        List<PublicationCategoryEntity> publicationCategoryEntity = publicationCategoryRepository.findAllByPublicationId(publicationEntity.getId());
        List<String> categories = publicationCategoryEntity.stream()
                .map(category -> category.getCategoryEntity().getDescription())
                .toList();
        return categories;

    }

    public List<String> mapTagsToDto(PublicationEntity publicationEntity) {
        List<PublicationTagEntity> publicationTagEntity = publicationTagRepository.findAllByPublicationId(publicationEntity.getId());
        List<String> tags = publicationTagEntity.stream()
                .map(tag -> tag.getTag().getDescription())
                .toList();
        return tags;

    }

    public List<GetContactDto> mapContactsToDto(PublicationEntity publicationEntity) {
        List<ContactEntity> contactEntity = contactRepository.findAllByPublicationId(publicationEntity.getId());
        List<GetContactDto> contacts = contactEntity.stream()
                .map(contact -> GetContactDto.builder()
                        .id(contact.getId())
                        .contact_type(contact.getContact_type().getDescription())
                        .contact_value(contact.getContact_value())
                        .build())
                .toList();
        return contacts;
    }

    public List<String> mapImagesToDto(PublicationEntity publicationEntity) {
        List<PublicationImageEntity> publicationImageEntity = publicationImageRepository.findAllByPublicationId(publicationEntity.getId());
        List<String> images = publicationImageEntity.stream()
                .map(image -> image.getImage_url())
                .toList();
        return images;
    }


}
