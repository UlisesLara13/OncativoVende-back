package com.example.OncativoVende.services;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {

    String uploadProfilePic(Long userId, MultipartFile file) throws IOException;

    String uploadPublicationPic(Long publicationId, Long userId, MultipartFile file, int photoNumber) throws IOException;

    String uploadEventPic(Long eventId, MultipartFile file) throws IOException;

}
