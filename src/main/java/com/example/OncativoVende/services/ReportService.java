package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.get.GetReportDto;
import com.example.OncativoVende.dtos.post.PostReportDto;
import com.example.OncativoVende.dtos.post.ReportFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {

    boolean generateReport(PostReportDto postReportDto);

    List<GetReportDto> getReportedPublications();

    boolean userHasReportedPublication(Integer publicationId,Integer userId);

    Page<GetReportDto> filterReports(ReportFilterDto dto);

}
