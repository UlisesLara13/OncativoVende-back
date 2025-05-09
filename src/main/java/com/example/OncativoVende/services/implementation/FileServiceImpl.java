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

    private static final String PROFILE_PIC_DIRECTORY = "C:/Users/USER/Desktop/tesis/OncativoVende-front/Oncativo-Vende/src/assets/Profiles/";
    private static final String PUBLICATION_PIC_DIRECTORY = "C:/Users/USER/Desktop/tesis/OncativoVende-front/Oncativo-Vende/src/assets/Publications/";

    @Override
    public String uploadProfilePic(Long userId, MultipartFile file) throws IOException {
        // Nombre de archivo: id de usuario + "profile-picture.jpg"
        String fileName = userId + "-profile-picture.jpg";
        Path path = Paths.get(PROFILE_PIC_DIRECTORY + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "assets/Profiles/" + fileName; // Devuelves la URL relativa
    }

    @Override
    public String uploadPublicationPic(Long publicationId, Long userId, MultipartFile file, int photoNumber) throws IOException {
        // Nombre de archivo: id de publicación + "-" + id de usuario + "-foto" + número.jpg
        String fileName = publicationId + "-" + userId + "-foto" + photoNumber + ".jpg";
        Path path = Paths.get(PUBLICATION_PIC_DIRECTORY + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "assets/Publications/" + fileName; // Devuelves la URL relativa
    }

}
