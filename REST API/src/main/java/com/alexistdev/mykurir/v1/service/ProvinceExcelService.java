package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.repository.ProvinceRepo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProvinceExcelService {

    private final ProvinceRepo provinceRepo;

    public List<Province> importExcelFile(MultipartFile file) throws IOException {
        List<Province> provinces = new ArrayList<>();

        try(InputStream is = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(is)){

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if(rows.hasNext()){
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Cell nameCell = currentRow.getCell(0);

                if(nameCell != null){
                    String provinceName = getCellValueAsString(nameCell);
                    if(provinceName != null && !provinceName.trim().isEmpty()){
                        Province province = new Province();
                        province.setName(provinceName.toLowerCase());
                        provinces.add(province);
                    }
                }
            }
        }
        return provinceRepo.saveAll(provinces);
    }

    private String getCellValueAsString(Cell cell){
        if(cell == null) return null;

        switch (cell.getCellType()){
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
}
