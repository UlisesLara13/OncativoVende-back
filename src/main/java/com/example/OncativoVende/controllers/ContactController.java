package com.example.OncativoVende.controllers;


import com.example.OncativoVende.dtos.get.GetContactDto;
import com.example.OncativoVende.dtos.get.GetContactTypeDto;
import com.example.OncativoVende.dtos.post.PostContact;
import com.example.OncativoVende.dtos.post.PostContactDto;
import com.example.OncativoVende.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/publication/{id}")
    public ResponseEntity<List<GetContactDto>> getContactsByPublicationId(@PathVariable Integer id) {
        List<GetContactDto> result = contactService.getContactsByPublicationId(id);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> createContact(@RequestBody PostContactDto contactDto) {
        contactService.createContact(contactDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateContact(@PathVariable Integer id, @RequestBody PostContact contactDto) {
        contactService.updateContact(id, contactDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Integer id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/types")
    public ResponseEntity<List<GetContactTypeDto>> getContactTypes() {
        List<GetContactTypeDto> result = contactService.getContactTypes();

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

}
