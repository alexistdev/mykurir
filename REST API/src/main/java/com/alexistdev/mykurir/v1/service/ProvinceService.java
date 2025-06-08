package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.models.repository.ProvinceRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ProvinceService {

    @Autowired
    private ProvinceRepo provinceRepo;

    public Page<Province> getAllProvinces(Pageable pageable) {
        return provinceRepo.findAll(pageable);
    };

    public Province saveProvince(Province province) {
        Province existProvince = provinceRepo.findByName(province.getName());

        if(existProvince != null){
            if(existProvince.getDeleted()){
                Province currentProvince = new Province();
                currentProvince.setDeleted(false);
                currentProvince.setId(existProvince.getId());
                currentProvince.setName(existProvince.getName());
                return provinceRepo.save(currentProvince);
            }
            throw new RuntimeException("Province name already exist");
        }

        if(province.getId() != null){
            Province currentProvince = provinceRepo.findById(province.getId()).orElse(null);
            if(currentProvince != null){
                currentProvince.setDeleted(false);
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

    public void deleteProvinceById(Long id){
        Province province = findProvinceById(id);
        if(province == null){
            throw new RuntimeException("Province not found" + id);
        }
        province.setDeleted(true);
        provinceRepo.save(province);
    }

    public Page<Province> getProvinceByFilter(Pageable pageable, String keyword) {
        return provinceRepo.findByFilter(keyword.toLowerCase(), pageable);
    }
}
