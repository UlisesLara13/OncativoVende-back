package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.services.FileService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Data
public class FileServiceImpl implements FileService {

    private static final String PROFILE_PIC_DIRECTORY = "C:/Users/USER/Desktop/tesis/files/Profiles/";
    private static final String PUBLICATION_PIC_DIRECTORY = "C:/Users/USER/Desktop/tesis/files/Publications/";

    @Override
    public String uploadProfilePic(Long userId, MultipartFile file) throws IOException {
        String fileName = userId + "-profile-picture.jpg";
        Path path = Paths.get(PROFILE_PIC_DIRECTORY + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return "http://localhost:8080/Profiles/" + fileName;
    }

    @Override
    public String uploadPublicationPic(Long publicationId, Long userId, MultipartFile file, int photoNumber) throws IOException {
        String fileName = publicationId + "-" + userId + "-foto" + photoNumber + ".jpg";
        Path path = Paths.get(PUBLICATION_PIC_DIRECTORY + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return "http://localhost:8080/Publications/" + fileName;
    }

}
