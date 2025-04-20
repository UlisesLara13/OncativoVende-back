package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostReportDto;
import com.example.OncativoVende.entities.ReportEntity;
import com.example.OncativoVende.repositores.PublicationRepository;
import com.example.OncativoVende.repositores.ReportRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.PublicationService;
import com.example.OncativoVende.services.ReportService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    private final PublicationService publicationService;

    private final PublicationRepository publicationRepository;

    private final UserRepository userRepository;

    @Override
    public boolean generateReport(PostReportDto postReportDto) {

        if (postReportDto == null) {
            return false;
        }
        else {
            ReportEntity reportEntity = new ReportEntity();
            mapToReportEntity(postReportDto, reportEntity);
            reportRepository.save(reportEntity);
            return true;
        }

    }

    public void mapToReportEntity(PostReportDto postReportDto, ReportEntity reportEntity) {
        reportEntity.setPublication(publicationRepository.findById(postReportDto.getPublication_id())
                .orElseThrow(() -> new RuntimeException("Publication not found")));
        reportEntity.setUser(userRepository.findById(postReportDto.getReported_by_user_id())
                .orElseThrow(() -> new RuntimeException("User not found")));
        reportEntity.setStatus("PENDIENTE");
        reportEntity.setReason(postReportDto.getReason());
        reportEntity.setCreated_at(LocalDate.now());
    }

    @Override
    public List<GetPublicationDto> getReportedPublications() {
        List<GetPublicationDto> result = new ArrayList<>();
        for (ReportEntity reportEntity : reportRepository.findAll()) {
            GetPublicationDto publicationDto = publicationService.getPublicationById(reportEntity.getPublication().getId());
            if (publicationDto != null) {
                result.add(publicationDto);
            }
        }
        return result;
    }
}
