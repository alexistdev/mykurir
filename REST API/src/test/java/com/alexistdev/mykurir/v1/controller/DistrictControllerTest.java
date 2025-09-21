package com.alexistdev.mykurir.v1.controller;

import com.alexistdev.mykurir.v1.dto.DistrictDTO;
import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.request.DistrictRequest;
import com.alexistdev.mykurir.v1.service.DistrictService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
public class DistrictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DistrictService districtService;

    @MockBean
    private ModelMapper modelMapper;

    @Nested
    @DisplayName("Test Get District")
    class testGetDistrict {

        @Test
        @DisplayName("Should return empty list when no data")
        void testGetAllDistricts_whenNoData() throws Exception {
            Page<District> emptyPage = new PageImpl<>(Collections.emptyList());
            Mockito.when(districtService.getAllDistricts(any(Pageable.class))).thenReturn(emptyPage);

            mockMvc.perform(get("/v1/api/region/district")
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("No District found"))
                    .andExpect(jsonPath("$.payload.content").isEmpty());
        }

        @Test
        @DisplayName("Should returns districts when sort direction parameter is invalid")
        void testGetAllDistrict_whenInvalidSortDirection() throws Exception {
            Page<District> emptyPage = new PageImpl<>(Collections.emptyList());
            Mockito.when(districtService.getAllDistricts(any(Pageable.class))).thenReturn(emptyPage);

            mockMvc.perform(get("/v1/api/region/district")
                            .param("direction", "invalid")
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("No District found"))
                    .andExpect(jsonPath("$.payload.content").isEmpty());
        }

        @Test
        @DisplayName("Should Returns Bad Request when page parameter is negative")
        void testGetAllDistricts_whenNegativePageIndex() throws Exception {
            mockMvc.perform(get("/v1/api/region/district")
                            .param("page", "-1")
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
        }

        @Test
        @DisplayName("Should returns districts when sortBy parameter is invalid")
        void testGetAllDistricts_withInvalidSortParameter() throws Exception {
            Page<District> emptyPage = new PageImpl<>(Collections.emptyList());
            Mockito.when(districtService.getAllDistricts(any(Pageable.class)))
                    .thenThrow(new RuntimeException("Invalid sort property"))
                    .thenReturn(emptyPage);

            mockMvc.perform(get("/v1/api/region/district")
                            .param("sortBy", "invalidField")
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("No District found"))
                    .andExpect(jsonPath("$.payload.content").isEmpty());
        }

        @Test
        @DisplayName("Should return all data districts when regencies exists")
        void testGetAllDistricts_whenDataExists() throws Exception{
            List<District> districts = new ArrayList<>();
            for (long i = 1; i <= 3; i++) {
                District district = new District();
                district.setId(i);
                district.setName("District " + i);
                Regency regency = new Regency();
                regency.setId(i);
                regency.setName("Regency " + i);
                district.setRegency(regency);
                districts.add(district);
            }
            Page<District> districtPage = new PageImpl<>(districts,
                    PageRequest.of(0,10, Sort.by(Sort.Direction.ASC, "id")),
                    districts.size());
            Mockito.when(districtService.getAllDistricts(any(Pageable.class))).thenReturn(districtPage);

            for(District district : districts){
                DistrictDTO dto = new DistrictDTO();
                dto.setId(district.getId());
                dto.setName(district.getName());
                Mockito.when(modelMapper.map(district, DistrictDTO.class)).thenReturn(dto);
            }

            mockMvc.perform(get("/v1/api/region/district")
                            .param("page", "0")
                            .param("size", "10")
                            .param("sortBy", "id")
                            .param("direction", "asc")
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.messages[0]").value("Retrieved page 0 of districts"))
                    .andExpect(jsonPath("$.payload.content[0].id").value(1))
                    .andExpect(jsonPath("$.payload.content[1].id").value(2))
                    .andExpect(jsonPath("$.payload.content[2].id").value(3));
        }

        @Test
        @DisplayName("Should return no data district for non existent keyword")
        void testGetDistrictByFilter_whenNoData() throws Exception {
            Page<District> emptyPage = new PageImpl<>(Collections.emptyList());
            Mockito.when(districtService.getDistrictByFilter(any(Pageable.class), any(String.class))).thenReturn(emptyPage);

            mockMvc.perform(get("/v1/api/region/district/filter")
                            .param("filter", "NonExistent")
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("No District found"))
                    .andExpect(jsonPath("$.payload.content").isEmpty());
        }

        @Test
        @DisplayName("Should return districts for existent keyword")
        void testGetDistrictByFilter_whenDataExists() throws Exception {
            List<District> districts = new ArrayList<>();
            for (long i = 1; i <= 2; i++) {
                District district = new District();
                district.setId(i);
                district.setName("District " + i);
                Regency regency = new Regency();
                regency.setId(i);
                regency.setName("Regency " + i);
                district.setRegency(regency);
                districts.add(district);
            }
            Page<District> districtPage = new PageImpl<>(districts,
                    PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                    districts.size());

            Mockito.when(districtService.getDistrictByFilter(any(Pageable.class), any(String.class)))
                    .thenReturn(districtPage);

            for(District district : districts){
                DistrictDTO dto = new DistrictDTO();
                dto.setId(district.getId());
                dto.setName(district.getName());
                Mockito.when(modelMapper.map(district, DistrictDTO.class)).thenReturn(dto);
            }

            mockMvc.perform(get("/v1/api/region/district/filter")
                            .param("filter", "Match")
                            .param("page", "0")
                            .param("size", "10")
                            .param("sortBy", "id")
                            .param("direction", "asc")
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.messages[0]").value("Retrieved page 0 of districts"))
                    .andExpect(jsonPath("$.payload.content[0].id").value(1))
                    .andExpect(jsonPath("$.payload.content[1].id").value(2));
        }
    }

    @Nested
    @DisplayName("Test Add District")
    class testAddDistrict {

        @Test
        @DisplayName("Should add data district for valid request")
        void testAddDistrict_whenValidRequest() throws Exception {
            Regency regency = new Regency();
            regency.setId(1L);
            regency.setName("Regency 1");
            District district = new District();
            district.setId(1L);
            district.setName("District 1");
            district.setRegency(regency);

            DistrictRequest districtRequest = new DistrictRequest();
            districtRequest.setName("District 1");
            districtRequest.setRegencyId(1L);

            Mockito.when(districtService.saveDistrict(any(DistrictRequest.class))).thenReturn(district);


            mockMvc.perform(post("/v1/api/region/district")
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"District 1\", \"regencyId\": 1}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.messages[0]").value("District Saved"))
                    .andExpect(jsonPath("$.payload.id").value(1))
                    .andExpect(jsonPath("$.payload.name").value("District 1"));
        }

        @Test
        @DisplayName("Should return bad request when invalid request")
        void testAddDistrict_whenInvalidRequest() throws Exception {
            mockMvc.perform(post("/v1/api/region/district")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("Name is required"));
        }
    }

    @Nested
    @DisplayName("Test Update District")
    class testUpdateDistrict {

        @Test
        @DisplayName("Should update data district for valid request")
        void testUpdateDistrict_whenValidRequest() throws Exception {
            Regency regency = new Regency();
            regency.setId(1L);
            regency.setName("Regency");

            District district = new District();
            district.setId(1L);
            district.setName("District");
            district.setRegency(regency);

            DistrictRequest districtRequest = new DistrictRequest();
            districtRequest.setId(1L);
            districtRequest.setName("Updated District");
            districtRequest.setRegencyId(1L);

            Mockito.when(districtService.findDistrictById(1L)).thenReturn(district);

            District updatedDistrict = new District();
            updatedDistrict.setId(1L);
            updatedDistrict.setName("Updated District");
            updatedDistrict.setRegency(regency);

            Mockito.when(districtService.saveDistrict(any(DistrictRequest.class))).thenReturn(updatedDistrict);

            mockMvc.perform(patch("/v1/api/region/district")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"id\":1, \"name\":\"Updated District\", \"districtId\":1}")
                            .with(httpBasic("user", "password")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.messages[0]").value("District Saved"))
                    .andExpect(jsonPath("$.payload.id").value(1))
                    .andExpect(jsonPath("$.payload.name").value("Updated District"));
        }

        @Test
        @DisplayName("Should return bad request when ID is missing in update")
        void testUpdateDistrict_whenIdIsMissing() throws Exception {
            mockMvc.perform(patch("/v1/api/region/district")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Updated District\"}")
                            .with(httpBasic("user", "password")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("Id cannot be null!"));
        }

        @Test
        @DisplayName("Should return bad request when district not found on update district")
        void testUpdateDistrict_whenDistrictNotFound() throws Exception {
            DistrictRequest districtRequest = new DistrictRequest();
            districtRequest.setId(1L);
            districtRequest.setName("Nonexistent District");

            Mockito.when(districtService.findDistrictById(1L)).thenReturn(null);

            mockMvc.perform(patch("/v1/api/region/district")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"id\":1, \"name\":\"Nonexistent District\"}")
                            .with(httpBasic("user", "password")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("No District found"));
        }

        @Test
        @DisplayName("Should return bad request when invalid request on update district")
        void testUpdateDistrict_whenInvalidRequest() throws Exception {
            mockMvc.perform(patch("/v1/api/region/district")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"id\":1, \"name\":\"\"}")
                            .with(httpBasic("user", "password")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("Name is required"));
        }
    }

    @Nested
    @DisplayName("Test Delete District")
    class testDeleteDistrict {

        @Test
        @DisplayName("Should delete district when id is valid")
        void testDeleteDistrict_whenValidId() throws Exception {
            Long districtId = 1L;

            Mockito.doNothing().when(districtService).deleteDistrictById(districtId);

            mockMvc.perform(delete("/v1/api/region/district/{id}", districtId)
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.messages[0]").value("District successfully deleted!"));
        }

        @Test
        @DisplayName("Should return bad request when district is not exists")
        void testDeleteDistrict_whenDistrictNotFound() throws Exception {
            Long districtId = 999L;

            Mockito.doThrow(new RuntimeException("District not found")).when(districtService).deleteDistrictById(districtId);

            mockMvc.perform(delete("/v1/api/region/district/{id}", districtId)
                            .with(httpBasic("user", "password"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.messages[0]").value("District not found"));
        }

    }
}
