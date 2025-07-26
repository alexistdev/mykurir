package com.alexistdev.mykurir.v1.request;

import com.alexistdev.mykurir.v1.masterconstant.Validation;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegencyRequest {

    @Nullable
    private Long id;

    @Nullable
    private Long provinceId;

    @NotEmpty(message = "Name is required")
    @Size(max = 150, message = Validation.nameMax)
    private String name;

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @Nullable
    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(@Nullable Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
