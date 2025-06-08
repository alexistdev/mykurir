package com.alexistdev.mykurir.v1.models.repository;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProvinceRepo  extends JpaRepository<Province, Long> {
    Province findByName(String name);

    @Query("SELECT u FROM Province u WHERE u.name LIKE %:filter%")
    Page<Province> findByFilter(@Param("filter")  String filter, Pageable pageable);

}
