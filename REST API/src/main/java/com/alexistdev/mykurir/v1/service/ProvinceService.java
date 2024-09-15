package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.repository.ProvinceRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProvinceService {

    @Autowired
    private ProvinceRepo provinceRepo;

    public List<Province> getAllProvinces() {
        return provinceRepo.findAll();
    };
}
