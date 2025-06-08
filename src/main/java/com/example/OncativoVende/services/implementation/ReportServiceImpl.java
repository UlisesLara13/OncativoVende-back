package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.get.GetReportDto;
import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.PostReportDto;
import com.example.OncativoVende.dtos.post.ReportFilterDto;
import com.example.OncativoVende.entities.ReportEntity;
import com.example.OncativoVende.repositores.PublicationRepository;
import com.example.OncativoVende.repositores.ReportRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.PublicationService;
import com.example.OncativoVende.services.ReportService;
import com.example.OncativoVende.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final UserService userService;

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
    public List<GetReportDto> getReportedPublications() {
        return reportRepository.findAll().stream().map(reportEntity -> {
            GetReportDto dto = new GetReportDto();
            mapReportEntityToDto(reportEntity, dto);
            return dto;
        }).toList();
    }

    private void mapReportEntityToDto(ReportEntity reportEntity, GetReportDto getReportDto) {
        getReportDto.setId(reportEntity.getId());
        GetUserDto getUserDto = new GetUserDto();
        userService.mapUserEntityToDto(reportEntity.getUser(), getUserDto);
        getReportDto.setReporter(getUserDto);
        GetPublicationDto getPublicationDto = new GetPublicationDto();
        publicationService.mapPublicationEntityToDto(reportEntity.getPublication(), getPublicationDto);
        getReportDto.setPublication(getPublicationDto);
        getReportDto.setReason(reportEntity.getReason());
        getReportDto.setCreated_at(reportEntity.getCreated_at());
        getReportDto.setStatus(reportEntity.getStatus());
        getReportDto.setResponse(reportEntity.getResponse());
    }

    @Override
    public boolean userHasReportedPublication(Integer userId, Integer publicationId) {
        return reportRepository.existsByPublicationIdAndUserId(publicationId, userId);
    }

    @Override
    public Page<GetReportDto> filterReports(ReportFilterDto dto) {
        String searchTerm = (dto.getSearchTerm() != null && !dto.getSearchTerm().isBlank())
                ? dto.getSearchTerm().toLowerCase()
                : null;

        String status = (dto.getStatus() != null && !dto.getStatus().isBlank())
                ? dto.getStatus().toLowerCase()
                : null;

        int page = Math.max(dto.getPage(), 0);
        int size = dto.getSize() > 0 ? dto.getSize() : 10;

        String sortBy = isValidSortField(dto.getSortBy()) ? dto.getSortBy() : "created_at";
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(dto.getSortDir())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<ReportEntity> reportEntities = reportRepository.findReportsWithFilters(searchTerm, status, pageable);

        return reportEntities.map(reportEntity -> {
            GetReportDto dtoResult = new GetReportDto();
            mapReportEntityToDto(reportEntity, dtoResult);
            return dtoResult;
        });
    }

    private boolean isValidSortField(String field) {
        return List.of("created_at", "status", "reason").contains(field);
    }

}
