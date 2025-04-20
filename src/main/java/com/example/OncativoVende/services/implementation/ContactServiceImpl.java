package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetContactDto;
import com.example.OncativoVende.dtos.post.PostContact;
import com.example.OncativoVende.dtos.post.PostContactDto;
import com.example.OncativoVende.entities.ContactEntity;
import com.example.OncativoVende.repositores.ContactRepository;
import com.example.OncativoVende.repositores.ContactTypeRepository;
import com.example.OncativoVende.repositores.PublicationRepository;
import com.example.OncativoVende.services.ContactService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    private final ContactTypeRepository contactTypeRepository;

    private final PublicationRepository publicationRepository;

    @Override
    public List<GetContactDto> getContactsByPublicationId(Integer id) {
        List<ContactEntity> contactEntities = contactRepository.findAllByPublicationId(id);
        List<GetContactDto> contactDtos = new ArrayList<>();
        for (ContactEntity contact : contactEntities) {
            GetContactDto contactDto = new GetContactDto();
            contactDto.setContact_type(contact.getContact_type().getDescription());
            contactDto.setContact_value(contact.getContact_value());
            contactDtos.add(contactDto);
        }
        return contactDtos;
    }

    public void mapPostContactToEntity(PostContactDto contactDto, ContactEntity contactEntity){
        contactEntity.setContact_type(contactTypeRepository.findById(contactDto.getContact_type_id())
                .orElseThrow(() -> new EntityNotFoundException("Contact type not found with id: " + contactDto.getContact_type_id())));
        contactEntity.setContact_value(contactDto.getContact_value());
        contactEntity.setPublication(publicationRepository.findById(contactDto.getPublication_id())
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + contactDto.getPublication_id())));
    }

    public void mapPostContactToEntity(PostContact contactDto, ContactEntity contactEntity){
        contactEntity.setContact_type(contactTypeRepository.findById(contactDto.getContact_type_id())
                .orElseThrow(() -> new EntityNotFoundException("Contact type not found with id: " + contactDto.getContact_type_id())));
        contactEntity.setContact_value(contactDto.getContact_value());

    }

    public void mapContactToDto(ContactEntity contactEntity, GetContactDto contactDto){
        contactDto.setId(contactEntity.getId());
        contactDto.setContact_type(contactEntity.getContact_type().getDescription());
        contactDto.setContact_value(contactEntity.getContact_value());
    }

    @Override
    public GetContactDto createContact(PostContactDto contactDto) {
        ContactEntity contactEntity = new ContactEntity();
        mapPostContactToEntity(contactDto, contactEntity);
        contactRepository.save(contactEntity);
        GetContactDto getContactDto = new GetContactDto();
        mapContactToDto(contactEntity, getContactDto);
        return getContactDto;
    }

    @Override
    public GetContactDto updateContact(Integer id, PostContact postContact) {
        ContactEntity contactEntity = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
        mapPostContactToEntity(postContact, contactEntity);
        contactRepository.save(contactEntity);
        GetContactDto getContactDto = new GetContactDto();
        mapContactToDto(contactEntity, getContactDto);
        return getContactDto;
    }

    @Transactional
    @Override
    public void deleteContact(Integer id) {
        ContactEntity contactEntity = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
        contactRepository.delete(contactEntity);
    }
}
