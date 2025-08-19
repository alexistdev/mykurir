package com.alexistdev.mykurir.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegencyDTO {
    private Long id;
    private String name;
    @JsonIgnoreProperties({"createdDate", "modifiedDate"})
    private ProvinceDTO province;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}

