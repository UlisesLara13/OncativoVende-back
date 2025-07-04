package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetContactDto;
import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.get.GetPublicationsStatsDto;
import com.example.OncativoVende.dtos.get.GetShortUserDto;
import com.example.OncativoVende.dtos.post.PostContact;
import com.example.OncativoVende.dtos.post.PostPublicationDto;
import com.example.OncativoVende.dtos.post.PublicationByUserFilterDto;
import com.example.OncativoVende.dtos.post.PublicationFilterDto;
import com.example.OncativoVende.dtos.put.PutPublicationDto;
import com.example.OncativoVende.entities.*;
import com.example.OncativoVende.repositores.*;
import com.example.OncativoVende.services.PublicationService;
import com.example.OncativoVende.services.RatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public GetPublicationDto getPublicationActiveById(Integer id) {
        PublicationEntity publicationEntity = publicationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + id));
        GetPublicationDto getPublicationDto = new GetPublicationDto();
        mapPublicationEntityToDto(publicationEntity, getPublicationDto);
        return getPublicationDto;
    }



    @Override
    @Transactional
    public GetPublicationDto updatePublication(Integer id, PutPublicationDto putPublicationDto) {
        PublicationEntity publicationEntity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + id));


        publicationCategoryRepository.deleteAllByPublicationId(publicationEntity.getId());
        publicationTagRepository.deleteAllByPublicationId(publicationEntity.getId());
        publicationImageRepository.deleteAllByPublicationId(publicationEntity.getId());
        contactRepository.deleteAllByPublicationId(publicationEntity.getId());

        mapPublicationDtoToEntity(putPublicationDto, publicationEntity);
        publicationEntity = publicationRepository.save(publicationEntity);

        createCategories(putPublicationDto, publicationEntity);
        createTags(putPublicationDto, publicationEntity);
        createImages(putPublicationDto, publicationEntity);
        createContacts(putPublicationDto, publicationEntity);

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
        publicationEntity.setCreatedAt(LocalDate.now());
        publicationEntity.setLatitude(postPublicationDto.getLatitude());
        publicationEntity.setLongitude(postPublicationDto.getLongitude());
        publicationEntity.setLocation_id(locationRepository.findById(postPublicationDto.getLocation_id())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + postPublicationDto.getLocation_id())));
        publicationEntity.setUser(userRepository.findById(postPublicationDto.getUser_id())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + postPublicationDto.getUser_id())));
        publicationEntity.setActive(Boolean.TRUE);
        publicationEntity.setViews(0);
    }

    public void mapPublicationDtoToEntity(PutPublicationDto putPublicationDto, PublicationEntity publicationEntity) {
        publicationEntity.setTitle(putPublicationDto.getTitle());
        publicationEntity.setDescription(putPublicationDto.getDescription());
        publicationEntity.setPrice(putPublicationDto.getPrice());
        publicationEntity.setCreatedAt(LocalDate.now());
        publicationEntity.setLatitude(putPublicationDto.getLatitude());
        publicationEntity.setLongitude(putPublicationDto.getLongitude());
        publicationEntity.setLocation_id(locationRepository.findById(putPublicationDto.getLocation_id())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + putPublicationDto.getLocation_id())));
        publicationEntity.setActive(Boolean.TRUE);
        publicationEntity.setViews(0);
    }

    public void createCategories(PutPublicationDto putPublicationDto , PublicationEntity publicationEntity) {
        for (Integer category_id : putPublicationDto.getCategories()) {
            PublicationCategoryEntity publicationCategoryEntity = new PublicationCategoryEntity();
            publicationCategoryEntity.setPublication(publicationEntity);
            publicationCategoryEntity.setCategoryEntity(
                    categoryRepository.findById(category_id)
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + category_id)));
            publicationCategoryRepository.save(publicationCategoryEntity);
        }

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

    public void createTags(PutPublicationDto putPublicationDto , PublicationEntity publicationEntity) {
        for (Integer tag_id : putPublicationDto.getTags()) {
            PublicationTagEntity publicationTagEntity = new PublicationTagEntity();
            publicationTagEntity.setPublication(publicationEntity);
            publicationTagEntity.setTag(
                    tagRepository.findById(tag_id)
                            .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tag_id)));
            publicationTagRepository.save(publicationTagEntity);
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
    

    public void createImages(PutPublicationDto putPublicationDto , PublicationEntity publicationEntity) {
        for (String image_url : putPublicationDto.getImages()) {
            PublicationImageEntity publicationImageEntity = new PublicationImageEntity();
            publicationImageEntity.setPublication(publicationEntity);
            publicationImageEntity.setImage_url(image_url);
            publicationImageRepository.save(publicationImageEntity);
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

    public void createContacts(PutPublicationDto putPublicationDto , PublicationEntity publicationEntity) {
        for (PostContact contact : putPublicationDto.getContacts()) {
            ContactEntity contactEntity = new ContactEntity();
            contactEntity.setPublication(publicationEntity);
            contactEntity.setContact_type(contactTypeRepository.findById(contact.getContact_type_id())
                    .orElseThrow(() -> new EntityNotFoundException("Contact type not found with id: " + contact.getContact_type_id())));
            contactEntity.setContact_value(contact.getContact_value());
            contactRepository.save(contactEntity);
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

    @Override
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
        getPublicationDto.setCreated_at(publicationEntity.getCreatedAt());
        getPublicationDto.setLatitude(publicationEntity.getLatitude());
        getPublicationDto.setLongitude(publicationEntity.getLongitude());
        getPublicationDto.setViews(publicationEntity.getViews());
    }

    public void mapShortUserEntityToDto(UserEntity userEntity, GetShortUserDto getShortUserDto) {
        getShortUserDto.setId(userEntity.getId());
        getShortUserDto.setName(userEntity.getName());
        getShortUserDto.setSurname(userEntity.getSurname());
        getShortUserDto.setUsername(userEntity.getUsername());
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
        if (images.isEmpty()) {
            images = List.of("assets/Publications/0-notfound.jpg");
        }
        return images;
    }

    @Override
    public List<GetPublicationDto> getLast10Publications(){

        List<PublicationEntity> publicationEntities = publicationRepository.findTop10ByActiveTrueOrderByCreatedAtDesc();
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
    public Page<GetPublicationDto> filterPublications(PublicationFilterDto dto) {
        String searchTerm = (dto.getSearchTerm() != null && !dto.getSearchTerm().isBlank())
                ? dto.getSearchTerm().toLowerCase()
                : null;

        List<String> categories = (dto.getCategories() != null && !dto.getCategories().isEmpty())
                ? dto.getCategories().stream().map(String::toLowerCase).toList()
                : null;

        List<String> tags = (dto.getTags() != null && !dto.getTags().isEmpty())
                ? dto.getTags().stream().map(String::toLowerCase).toList()
                : null;

        String location = (dto.getLocation() != null && !dto.getLocation().isBlank())
                ? dto.getLocation().toLowerCase()
                : null;

        Double minPrice = (dto.getMinPrice() != null && dto.getMinPrice() > 0) ? dto.getMinPrice() : null;
        Double maxPrice = (dto.getMaxPrice() != null && dto.getMaxPrice() > 0) ? dto.getMaxPrice() : null;

        String sortBy = (dto.getSortBy() != null && isValidSortField(dto.getSortBy()))
                ? dto.getSortBy()
                : "createdAt";

        String sortDir = (dto.getSortDir() != null && dto.getSortDir().equalsIgnoreCase("ASC"))
                ? "ASC"
                : "DESC";

        int page = (dto.getPage() >= 0) ? dto.getPage() : 0;
        int size = (dto.getSize() > 0) ? dto.getSize() : 12;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        Page<PublicationEntity> result = publicationRepository.findPublicationsWithAllFilters(
                searchTerm,
                categories,
                tags,
                location,
                minPrice,
                maxPrice,
                dto.getActive(),
                pageable
        );

        return result.map(entity -> {
            GetPublicationDto dtoResult = new GetPublicationDto();
            mapPublicationEntityToDto(entity, dtoResult);
            return dtoResult;
        });
    }

    private boolean isValidSortField(String field) {
        return List.of("createdAt", "price", "title", "views")
                .contains(field);
    }



    @Override
    public Page<GetPublicationDto> filterUserPublications(Integer userId, PublicationByUserFilterDto dto) {
        String searchTerm = (dto.getSearchTerm() != null && !dto.getSearchTerm().isBlank())
                ? dto.getSearchTerm().toLowerCase()
                : null;

        Boolean active = dto.getActive();

        String sortBy = (dto.getSortBy() != null && isValidSortField(dto.getSortBy()))
                ? dto.getSortBy()
                : "createdAt";

        String sortDir = (dto.getSortDir() != null && dto.getSortDir().equalsIgnoreCase("ASC"))
                ? "ASC"
                : "DESC";

        int page = (dto.getPage() >= 0) ? dto.getPage() : 0;
        int size = (dto.getSize() > 0) ? dto.getSize() : 12;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        Page<PublicationEntity> result = publicationRepository.findUserPublicationsWithFilters(
                userId,
                searchTerm,
                active,
                pageable
        );

        return result.map(entity -> {
            GetPublicationDto dtoResult = new GetPublicationDto();
            mapPublicationEntityToDto(entity, dtoResult);
            return dtoResult;
        });
    }

    @Override
    public void addView(Integer id) {
        PublicationEntity publicationEntity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + id));
            publicationEntity.setViews(publicationEntity.getViews() + 1);
            publicationRepository.save(publicationEntity);
    }

    @Override
    public void reactivatePublication(Integer id) {
        PublicationEntity publicationEntity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + id));
        publicationEntity.setActive(Boolean.TRUE);
        publicationEntity.setViews(0);
        publicationEntity.setCreatedAt(LocalDate.now());
        publicationRepository.save(publicationEntity);
    }

    @Override
    public boolean isSameUserPublication(Integer publicationId, Integer userId) {
        PublicationEntity publicationEntity = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + publicationId));
        return publicationEntity.getUser().getId().equals(userId);
    }

    @Override
    public void deleteAllPublicationsByUserId(Integer userId) {
        List<PublicationEntity> publications = publicationRepository.findAllByUser_Id(userId);
        for (PublicationEntity publication : publications) {
            deletePublication(publication.getId());
        }
    }

    @Override
    public void deleteAllPublicationsByUserPermanently(Integer userId) {
        List<PublicationEntity> publications = publicationRepository.findAllByUser_Id(userId);
        for (PublicationEntity publication : publications) {
            publicationCategoryRepository.deleteAllByPublicationId(publication.getId());
            publicationTagRepository.deleteAllByPublicationId(publication.getId());
            publicationImageRepository.deleteAllByPublicationId(publication.getId());
            contactRepository.deleteAllByPublicationId(publication.getId());
            publicationRepository.delete(publication);
        }

    }

    @Override
    public GetPublicationsStatsDto getPublicationsStats() {
        List<PublicationEntity> publications = publicationRepository.findAll();
        int totalPublications = publications.size();
        int activePublications = (int) publications.stream().filter(PublicationEntity::getActive).count();
        int inactivePublications = totalPublications - activePublications;
        int totalViews = publications.stream().mapToInt(PublicationEntity::getViews).sum();

        return GetPublicationsStatsDto.builder()
                .totalPublications(totalPublications)
                .activePublications(activePublications)
                .inactivePublications(inactivePublications)
                .totalViews(totalViews)
                .build();
    }
}
