package com.alexistdev.mykurir.v1.models.repository;

import com.alexistdev.mykurir.v1.models.entity.Regency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegencyRepo extends JpaRepository<Regency, Long> {
    Regency findByName(String name);
}
