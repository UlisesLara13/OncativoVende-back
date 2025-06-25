package com.example.OncativoVende.controllers;

import com.example.OncativoVende.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Operation(summary = "Sube la foto de perfil de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo subido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error al subir el archivo")
    })
    @PostMapping(value = "/upload/profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfilePic(
            @PathVariable("userId") Long userId,
            @Parameter(description = "Archivo de imagen a subir", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.uploadProfilePic(userId, file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }

    @Operation(summary = "Sube una foto para una publicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo subido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error al subir el archivo")
    })
    @PostMapping(value = "/upload/publication/{publicationId}/{userId}/{photoNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPublicationPic(
            @PathVariable("publicationId") Long publicationId,
            @PathVariable("userId") Long userId,
            @PathVariable("photoNumber") int photoNumber,
            @Parameter(description = "Archivo de imagen a subir", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.uploadPublicationPic(publicationId, userId, file, photoNumber);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }

    @Operation(summary = "Sube una foto para un evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo subido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error al subir el archivo")
    })
    @PostMapping(value = "/upload/event/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadEventPic(
            @PathVariable("eventId") Long eventId,
            @Parameter(description = "Archivo de imagen a subir", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.uploadEventPic(eventId, file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }
}
