package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetContactDto;
import com.example.OncativoVende.dtos.post.PostContact;
import com.example.OncativoVende.dtos.post.PostContactDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContactService {
    List<GetContactDto> getContactsByPublicationId(Integer id);

    GetContactDto createContact(PostContactDto postContactDto);

    GetContactDto updateContact(Integer id, PostContact contactDto);

    void deleteContact(Integer id);

}
