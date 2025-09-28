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
            if(!existDistrict.getDeleted()){
                throw new RuntimeException("District name already exist");
            }
            existDistrict.setDeleted(false);
            saveDistrict = existDistrict;
        }

        saveDistrict.setRegency(existRegency);
        saveDistrict.setName(request.getName());
        return districtRepo.save(saveDistrict);
    }

    public District updatedDistrict(DistrictRequest request) {
        Regency existRegency = regencyService.findRegencyById(request.getRegencyId());
        if(existRegency == null || Boolean.TRUE.equals(existRegency.getDeleted())){
            throw new RuntimeException("Regency Not Found");
        }

        assert request.getId() != null;
        Optional<District> optionalDistrict = districtRepo.findById(request.getId());
        if(optionalDistrict.isEmpty()){
            throw new RuntimeException("District Not Found");
        }

        District existDistrict = optionalDistrict.get();

        //validate if district name is already exists with the different's id
        if(!existDistrict.getName().equals(request.getName().trim())){
            District anotherDistrict = districtRepo.findByName(request.getName());
            if(anotherDistrict != null && !anotherDistrict.getId().equals(existDistrict.getId())
                && !Boolean.TRUE.equals(anotherDistrict.getDeleted())){
                throw new RuntimeException("District already exists");
            }
        }

        if(Boolean.TRUE.equals(existDistrict.getDeleted())){
            existDistrict.setDeleted(false);
        }

        existDistrict.setName(request.getName());
        existDistrict.setRegency(existRegency);
        return districtRepo.save(existDistrict);
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
