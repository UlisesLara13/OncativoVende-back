package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.get.GetReportDto;
import com.example.OncativoVende.dtos.post.PostReportDto;
import com.example.OncativoVende.dtos.post.PostSolveReportDto;
import com.example.OncativoVende.dtos.post.ReportFilterDto;
import com.example.OncativoVende.services.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<List<GetReportDto>> getReportedPublications() {
        List<GetReportDto> result = reportService.getReportedPublications();

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

    @PostMapping("/filter")
    public Page<GetReportDto> filterReports(@RequestBody ReportFilterDto dto) {
        return reportService.filterReports(dto);
    }

    @PostMapping("/solve")
    public ResponseEntity<Boolean> solveReport(@RequestBody PostSolveReportDto postSolveReportDto) {
        Boolean result = reportService.solveReport(postSolveReportDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

}
