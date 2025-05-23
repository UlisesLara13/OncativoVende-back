package com.example.OncativoVende.dtos.get;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPublicationDto {
    private Integer id;

    private GetShortUserDto user;

    private String title;

    private String description;

    private BigDecimal price;

    private Boolean active;

    private String location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate created_at;

    private List<String> categories;

    private List<String> tags;

    private List<GetContactDto> contacts;

    private List<String> images;

    private String coords;

}
