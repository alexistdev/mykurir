package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.masterconstant.Validation;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.repository.ProvinceRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProvinceService {

    @Autowired
    private ProvinceRepo provinceRepo;

    public List<Province> getAllProvinces() {
        return provinceRepo.findAll();
    };

    public Province saveProvince(Province province) {
        Province existProvince = provinceRepo.findByName(province.getName());
        if(existProvince != null){
            throw new RuntimeException(Validation.nameExist("Province"));
        }

        if(province.getId() != null){
            Province currentProvince = provinceRepo.findById(province.getId()).orElse(null);
            if(currentProvince != null){
                currentProvince.setName(province.getName());
                province=currentProvince;
            }
        }

        return provinceRepo.save(province);
    }

    public Province findProvinceById(Long id) {
        Optional<Province> result = provinceRepo.findById(id);
        return result.orElse(null);
    }
}
