package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.service.ProvinceExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/import/data")
@RequiredArgsConstructor
public class ImportController {

    private final ProvinceExcelService provinceExcelService;

    @PostMapping("/province")
    public ResponseEntity<List<Province>> importExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            List<Province> imported = provinceExcelService.importExcelFile(file);
            return ResponseEntity.ok(imported);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
