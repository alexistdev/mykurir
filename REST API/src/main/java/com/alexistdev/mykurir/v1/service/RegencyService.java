package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.RegencyRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RegencyService {

    @Autowired
    private RegencyRepo regencyRepo;

    public List<Regency> getAllRegencies() {
        return regencyRepo.findAll();
    }

}
