package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.RegencyRepo;
import com.alexistdev.mykurir.v1.request.RegencyRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RegencyService {

    @Autowired
    private RegencyRepo regencyRepo;

    @Autowired
    private ProvinceService provinceService;

    public Page<Regency> getAllRegencies(Pageable pageable) {
        return regencyRepo.findByIsDeletedFalse(pageable);
    }

    public List<Regency> getAllRegenciesByProvinceId(Long provinceId) {
        return regencyRepo.findAllByProvinceId(provinceId).stream().filter(r -> !r.getDeleted()).toList();
    }

    public Regency saveRegency(RegencyRequest request) {
        Province existProvince = provinceService.findProvinceById(request.getProvinceId());

        Regency saveRegency = new Regency();

        if(existProvince == null){
            throw new RuntimeException("Province Not Found");
        }

        Regency existRegency = regencyRepo.findByName(request.getName());

        if(existRegency != null){
            if(existRegency.getDeleted()){
                existRegency.setDeleted(false);
                existRegency.setName(request.getName());
                existRegency.setProvince(existProvince);
                return regencyRepo.save(existRegency);
            }
            throw new RuntimeException("Regency name already exist");
        }

        if(request.getId() != null){
            Regency currentRegency = regencyRepo.findById(request.getId()).orElse(null);
            if(currentRegency != null){
                currentRegency.setDeleted(false);
                saveRegency = currentRegency;
            }
        }

        saveRegency.setProvince(existProvince);
        saveRegency.setName(request.getName());
        return regencyRepo.save(saveRegency);
    }

}
