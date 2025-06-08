package com.alexistdev.mykurir.v1.models.repository;

import com.alexistdev.mykurir.v1.models.entity.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepo  extends JpaRepository<Province, Long> {
    Province findByName(String name);

//    Page<Province> findBy(Pageable pageable);

}
