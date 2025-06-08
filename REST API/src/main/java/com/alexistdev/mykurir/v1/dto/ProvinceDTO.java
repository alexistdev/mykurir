package com.alexistdev.mykurir.v1.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProvinceDTO {

    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
