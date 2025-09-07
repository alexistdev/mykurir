package com.alexistdev.mykurir.v1.request;

import com.alexistdev.mykurir.v1.masterconstant.Validation;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistrictRequest {

    @Nullable
    private Long id;

    @Nullable
    private Long regencyId;

    @NotEmpty(message = "Name is required")
    @Size(max = 150, message = Validation.nameMax)
    private String name;
}
