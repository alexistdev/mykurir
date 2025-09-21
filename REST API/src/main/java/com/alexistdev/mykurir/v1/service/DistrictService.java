package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.DistrictRepo;
import com.alexistdev.mykurir.v1.models.repository.RegencyRepo;
import com.alexistdev.mykurir.v1.request.DistrictRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DistrictService {

    @Autowired
    private DistrictRepo districtRepo;

    @Autowired
    private RegencyService regencyService;
    @Autowired
    private RegencyRepo regencyRepo;

    public Page<District> getAllDistricts(Pageable pageable){
        return districtRepo.findByIsDeletedFalse(pageable);
    }

    public District saveDistrict(DistrictRequest request) {
        District saveDistrict = new District();

        Regency existRegency = regencyService.findRegencyById(request.getRegencyId());

        if(existRegency == null || existRegency.getDeleted()){
            throw new RuntimeException("Regency Not Found");
        }

        District existDistrict = districtRepo.findByName(request.getName());

        if(existDistrict != null){
            if(!existDistrict.getDeleted() && request.getId() == null){
                throw new RuntimeException("District name already exist");
            }

            if(existDistrict.getDeleted()){
                existDistrict.setDeleted(false);
            }

            existDistrict.setName(request.getName());
            existDistrict.setRegency(existRegency);
            saveDistrict = existDistrict;
        }

        if(request.getId() != null){
            District currentDistrict = districtRepo.findById(request.getId()).orElse(null);
            if(currentDistrict != null){
                currentDistrict.setDeleted(false);
            }
        }

        saveDistrict.setRegency(existRegency);
        saveDistrict.setName(request.getName());
        return districtRepo.save(saveDistrict);
    }

    public Page<District> getDistrictByFilter(Pageable pageable, String keyword) {
        return districtRepo.findByFilter(keyword.toLowerCase(),pageable);
    }

    public void deleteDistrictById(Long id){
        District districtResult = findDistrictById(id);
        if(districtResult == null){
            throw new RuntimeException("District not found " + id);
        }
        districtResult.setDeleted(true);
        districtRepo.save(districtResult);
    }

    public District findDistrictById(Long id){
        Optional<District> result = districtRepo.findById(id);
        return result.orElse(null);
    }
}
