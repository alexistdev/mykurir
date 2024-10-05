package com.alexistdev.mykurir.v1.entity;

import com.alexistdev.mykurir.v1.models.entity.BaseEntity;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.Serializable;
import java.lang.reflect.Field;

public class RegencyTest {

    private Regency regency;

    private Province province;

    @BeforeEach
    void setUp() {
        regency = new Regency();
        province = new Province();
    }

    @Test
    void testSendAndGetId() {
        Long id = 1L;
        regency.setId(id);
        Assertions.assertEquals(id, regency.getId());
    }

    @Test
    void testSendAndGetName() {
        String name = "Lampung";
        regency.setName(name);
        Assertions.assertEquals(name, regency.getName());
    }

    @Test
    void testSendAndGetProvince() {
        String provinceName = "Lampung";
        Long provinceId = 1L;
        province.setId(provinceId);
        province.setName(provinceName);

        regency.setProvince(province);
        Assertions.assertEquals(provinceName, regency.getProvince().getName());
        Assertions.assertEquals(provinceId, regency.getProvince().getId());
    }

    @Test
    void testExtendsBaseEntity() {
        Assertions.assertInstanceOf(BaseEntity.class,regency);
    }

    @Test
    void testSerializable() {
        Assertions.assertInstanceOf(Serializable.class,regency);
    }

    @Test
    void testSerialVersionUID() {
        try{
            Field field = Regency.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            Assertions.assertEquals(1L, field.getLong(null));
        } catch (NoSuchFieldException | IllegalAccessException e){
            fail("serialVersionUID should not throw exception");
        }
    }
}
