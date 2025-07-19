package com.alexistdev.mykurir.v1.models.repository;

import com.alexistdev.mykurir.v1.models.entity.Regency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegencyRepo extends JpaRepository<Regency, Long> {
    Regency findByName(String name);

    Page<Regency> findByIsDeletedFalse(Pageable pageable);

    List<Regency> findAllByProvinceId(Long provinceId);
}
