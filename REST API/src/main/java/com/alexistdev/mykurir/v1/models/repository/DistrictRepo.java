package com.alexistdev.mykurir.v1.models.repository;

import com.alexistdev.mykurir.v1.models.entity.District;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepo extends JpaRepository<District, Long> {
    District findByName(String name);

    Page<District> findByIsDeletedFalse(Pageable pageable);

    List<District> findAllByRegencyId(Long regencyId);

    @Query("SELECT u FROM District u WHERE u.name LIKE %:filter%")
    Page<District> findByFilter(@Param("filter") String filter, Pageable pageable);
}
