package com.alexistdev.mykurir.v1.entity;

import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DistrictTest {

    private District district;

    private Regency regency;

    @BeforeEach
    void setUp() {
        district = new District();
        regency = new Regency();
        regency.setId(1L);
        regency.setName("Bandar Lampung");
    }

    @Test
    void testId(){
        Long id = 1L;
        district.setId(id);
        Assertions.assertEquals(id, district.getId());
    }

    @Test
    void testName(){
        String name = "Pringsewu";
        district.setName(name);
        Assertions.assertEquals(name, district.getName());
    }

    @Test
    void testRegency(){
        district.setRegency(regency);
        Assertions.assertEquals(regency, district.getRegency());
        Assertions.assertEquals("Bandar Lampung", district.getRegency().getName());
    }

    @Test
    void testNullValues(){
        Assertions.assertNull(district.getId());
        Assertions.assertNull(district.getName());
        Assertions.assertNull(district.getRegency());
    }

    @Test
    void testNonNullConstrains(){
        Assertions.assertThrows(NullPointerException.class, () -> district.setRegency(null));
    }

    @Test
    void testRegencyRelationship(){
        district.setRegency(regency);
        Assertions.assertNotNull(district.getRegency());
        Assertions.assertEquals(1L, district.getRegency().getId());
    }

}
