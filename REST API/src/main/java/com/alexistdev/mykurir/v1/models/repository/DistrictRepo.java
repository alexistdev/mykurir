package com.alexistdev.mykurir.v1.models.repository;

import com.alexistdev.mykurir.v1.models.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepo extends JpaRepository<District, Long> {
    District findByName(String name);
}
