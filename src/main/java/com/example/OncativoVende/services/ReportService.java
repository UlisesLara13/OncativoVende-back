package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostReportDto;

import java.util.List;

public interface ReportService {

    boolean generateReport(PostReportDto postReportDto);

    List<GetPublicationDto> getReportedPublications();

    boolean userHasReportedPublication(Integer publicationId,Integer userId);

}
