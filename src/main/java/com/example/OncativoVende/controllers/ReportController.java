package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostReportDto;
import com.example.OncativoVende.services.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Boolean> createReport(@Valid @RequestBody PostReportDto postReportDto) {
        Boolean result = reportService.generateReport(postReportDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<GetPublicationDto>> getReportedPublications() {
        List<GetPublicationDto> result = reportService.getReportedPublications();

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}/publication/{publicationId}")
    public ResponseEntity<Boolean> userHasReportedPublication(@PathVariable Integer userId, @PathVariable Integer publicationId) {
        Boolean result = reportService.userHasReportedPublication(userId, publicationId);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

}
