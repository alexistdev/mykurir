package com.alexistdev.mykurir.v1.controller;

import com.alexistdev.mykurir.v1.dto.RegencyDTO;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.request.RegencyRequest;
import com.alexistdev.mykurir.v1.service.RegencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username= "tester", roles = {"USER"})
public class RegencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegencyService regencyService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    @DisplayName("Test Get All Regencies - No Data Available")
    void testGetAllRegencies_whenNoData() throws Exception {
        Page<Regency> emptyPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(regencyService.getAllRegencies(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/v1/api/region/regency")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("No Regency found"))
                .andExpect(jsonPath("$.payload.content").isEmpty());
    }

    @Test
    @DisplayName("Test Get All Regencies - Invalid Sort Direction")
    void testGetAllRegencies_whenInvalidSortDirection() throws Exception {
        Page<Regency> emptyPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(regencyService.getAllRegencies(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/v1/api/region/regency")
                        .param("direction", "invalid")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("No Regency found"))
                .andExpect(jsonPath("$.payload.content").isEmpty());

    }


    @Test
    @DisplayName("Test Get All Regencies - Negative Page Value")
    void testGetAllRegencies_whenNegativePageIndex() throws Exception {
        mockMvc.perform(get("/v1/api/region/regency")
                        .param("page", "-1")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Test Get All Regencies - Invalid Sort Parameter")
    void testGetAllRegencies_withInvalidSortParameter() throws Exception {
        Page<Regency> emptyPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(regencyService.getAllRegencies(Mockito.any(Pageable.class)))
                .thenThrow(new RuntimeException("Invalid sort property"))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/v1/api/region/regency")
                        .param("sortBy", "invalidField")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("No Regency found"))
                .andExpect(jsonPath("$.payload.content").isEmpty());
    }

    @Test
    @DisplayName("Should return all data regencies when regencies exists")
    void testGetAllRegencies_whenDataExists() throws Exception {
        List<Regency> regencies = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            Regency regency = new Regency();
            regency.setId(i);
            regency.setName("Regency " + i);
            regencies.add(regency);
        }
        Page<Regency> regencyPage = new PageImpl<>(regencies,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                regencies.size());

        Mockito.when(regencyService.getAllRegencies(any(Pageable.class))).thenReturn(regencyPage);

        for (Regency regency : regencies) {
            RegencyDTO dto = new RegencyDTO();
            dto.setId(regency.getId());
            dto.setName(regency.getName());
            Mockito.when(modelMapper.map(regency, RegencyDTO.class)).thenReturn(dto);
        }

        mockMvc.perform(get("/v1/api/region/regency")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "asc")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.messages[0]").value("Retrieved page 0of regencies"))
                .andExpect(jsonPath("$.payload.content[0].id").value(1))
                .andExpect(jsonPath("$.payload.content[1].id").value(2))
                .andExpect(jsonPath("$.payload.content[2].id").value(3));
    }

    @Test
    @DisplayName("Should return no data regencies for non existent keyword")
    void testGetRegencyByFilter_whenNoData() throws Exception {
        Page<Regency> emptyPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(regencyService.getRegencyByFilter(any(Pageable.class), any(String.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/v1/api/region/regency/filter")
                        .param("filter", "NonExistent")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("No regency found"))
                .andExpect(jsonPath("$.payload.content").isEmpty());
    }

    @Test
    @DisplayName("Should return data regencies for existent keyword")
    void testGetRegencyByFilter_whenDataExists() throws Exception {
        List<Regency> regencies = new ArrayList<>();
        for (long i = 1; i <= 2; i++) {
            Regency regency = new Regency();
            regency.setId(i);
            regency.setName("Matching Regency " + i);
            regencies.add(regency);
        }
        Page<Regency> regencyPage = new PageImpl<>(regencies,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                regencies.size());

        Mockito.when(regencyService.getRegencyByFilter(any(Pageable.class), any(String.class))).thenReturn(regencyPage);

        for (Regency regency : regencies) {
            RegencyDTO dto = new RegencyDTO();
            dto.setId(regency.getId());
            dto.setName(regency.getName());
            Mockito.when(modelMapper.map(regency, RegencyDTO.class)).thenReturn(dto);
        }

        mockMvc.perform(get("/v1/api/region/regency/filter")
                        .param("filter", "Match")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "asc")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.messages[0]").value("Retrieved page 0 of regencies"))
                .andExpect(jsonPath("$.payload.content[0].id").value(1))
                .andExpect(jsonPath("$.payload.content[1].id").value(2));
    }

    @Test
    @DisplayName("Should add data regencies for valid request")
    void testAddRegency_whenValidRequest() throws Exception {
        Regency regency = new Regency();
        regency.setId(1L);
        regency.setName("Regency 1");

        RegencyRequest regencyRequest = new RegencyRequest();
        regencyRequest.setName("Regency 1");
        regencyRequest.setProvinceId(1L);

        Mockito.when(regencyService.saveRegency(any(RegencyRequest.class))).thenReturn(regency);

        mockMvc.perform(post("/v1/api/region/regency")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Regency 1\", \"provinceId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.messages[0]").value("regency successfully registered"))
                .andExpect(jsonPath("$.payload.id").value(1))
                .andExpect(jsonPath("$.payload.name").value("Regency 1"));
    }

    @Test
    @DisplayName("Should return bad request when invalid request")
    void testAddRegency_whenInvalidRequest() throws Exception {
        mockMvc.perform(post("/v1/api/region/regency")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("Name is required"));
    }

    @Test
    @DisplayName("Should update data regencies for valid request")
    void testUpdateRegency_whenValidRequest() throws Exception {
        Regency regency = new Regency();
        regency.setId(1L);
        regency.setName("Updated Regency");

        RegencyRequest regencyRequest = new RegencyRequest();
        regencyRequest.setId(1L);
        regencyRequest.setName("Updated Regency");
        regencyRequest.setProvinceId(2L);

        Mockito.when(regencyService.findRegencyById(1L)).thenReturn(regency);
        Mockito.when(regencyService.saveRegency(any(RegencyRequest.class))).thenReturn(regency);

        mockMvc.perform(post("/v1/api/region/regency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"name\":\"Updated Regency\", \"provinceId\":2}")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.messages[0]").value("regency successfully registered"))
                .andExpect(jsonPath("$.payload.id").value(1))
                .andExpect(jsonPath("$.payload.name").value("Updated Regency"));
    }

    @Test
    @DisplayName("Should return bad request when ID is missing in update")
    void testUpdateRegency_whenIdIsMissing() throws Exception {
        mockMvc.perform(patch("/v1/api/region/regency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Regency\"}")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("Id cannot be null"));
    }

    @Test
    @DisplayName("Should return bad request when regency not found on update regency")
    void testUpdateRegency_whenRegencyNotFound() throws Exception {
        RegencyRequest regencyRequest = new RegencyRequest();
        regencyRequest.setId(1L);
        regencyRequest.setName("Nonexistent Regency");

        Mockito.when(regencyService.findRegencyById(1L)).thenReturn(null);

        mockMvc.perform(patch("/v1/api/region/regency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"name\":\"Nonexistent Regency\"}")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("Regency Not Found"));
    }

    @Test
    @DisplayName("Should return bad request when invalid request on update regency")
    void testUpdateRegency_whenInvalidRequest() throws Exception {
        mockMvc.perform(post("/v1/api/region/regency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"name\":\"\"}")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("Name is required"));
    }

    @Test
    @DisplayName("Should delete regency when id is valid")
    void testDeleteRegency_whenValidId() throws Exception {
        Long regencyId = 1L;

        Mockito.doNothing().when(regencyService).deleteRegencyById(regencyId);

        mockMvc.perform(delete("/v1/api/region/regency/{id}", regencyId)
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.messages[0]").value("Regency successfully deleted!"));
    }

    @Test
    @DisplayName("Should return bad request when regency is not exists")
    void testDeleteRegency_whenRegencyNotFound() throws Exception {
        Long regencyId = 999L;

        Mockito.doThrow(new RuntimeException("Regency not found")).when(regencyService).deleteRegencyById(regencyId);

        mockMvc.perform(delete("/v1/api/region/regency/{id}", regencyId)
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.messages[0]").value("Regency not found"));
    }
}
