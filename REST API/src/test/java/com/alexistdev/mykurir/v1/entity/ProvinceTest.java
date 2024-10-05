package com.alexistdev.mykurir.v1.entity;

import com.alexistdev.mykurir.v1.models.entity.BaseEntity;
import com.alexistdev.mykurir.v1.models.entity.Province;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.fail;

public class ProvinceTest {

    private Province province;

    @BeforeEach
    void setUp() {
        province = new Province();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        province.setId(id);
        Assertions.assertEquals(id, province.getId());
    }

    @Test
    void testSetAndGetName() {
        String name = "Jakarta";
        province.setName(name);
        Assertions.assertEquals(name, province.getName());
    }


    @Test
    void testExtendsBaseEntity() {
        Assertions.assertInstanceOf(BaseEntity.class, province);
    }

    @Test
    void testImplementsSerializable() {
        Assertions.assertInstanceOf(Serializable.class, province);
    }

    @Test
    void testSerialVersionUID() {
        try {
            Field field = Province.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            Assertions.assertEquals(1L,field.getLong(null));
        } catch (NoSuchFieldException | IllegalAccessException e){
            fail("serialVersionUID should not throw exception");
        }
    }

    @Test
    void testNameNotNull() {
        Assertions.assertThrows(NullPointerException.class, () -> province.setName(null));
    }
}
