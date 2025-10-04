package com.alexistdev.mykurir.v1.entity;

import com.alexistdev.mykurir.v1.models.entity.BaseEntity;
import com.alexistdev.mykurir.v1.models.entity.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.fail;

public class MenuTest {

    public Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        menu.setId(1L);
        menu.setName("Dashboard");
        menu.setCreatedDate(new java.util.Date());
        menu.setModifiedDate(new java.util.Date());
        menu.setDeleted(false);
        menu.setCreatedBy("System");
        menu.setModifiedBy("System");
    }

    @Test
    @DisplayName("Should return id of saved menu")
    void testSendAndGetId() {
        Long id = 1L;
        menu.setId(id);
        Assertions.assertEquals(id, menu.getId());
    }

    @Test
    @DisplayName("Should return name of saved menu")
    void testSendAndGetName() {
        String name = "Dashboard";
        menu.setName(name);
        Assertions.assertEquals(name, menu.getName());
    }

    @Test
    @DisplayName("Should verify Menu extends BaseEntity")
    void testExtendsBaseEntity() {
        Assertions.assertInstanceOf(BaseEntity.class,menu);
    }

    @Test
    @DisplayName("Ensure menu is Serializable")
    void testSerializable() {
        Assertions.assertInstanceOf(Serializable.class,menu);
    }

    @Test
    @DisplayName("Should verify serialVersionUID is 1L")
    void testSerialVersionUID() {
        try{
            Field field = Menu.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            Assertions.assertEquals(1L, field.getLong(null));
        } catch (NoSuchFieldException | IllegalAccessException e){
            fail("serialVersionUID should not throw exception");
        }
    }

    @Test
    @DisplayName("Should allow setting a valid name")
    void testSetNameWithValidValue() {
        String newName = "Settings";
        menu.setName(newName);
        Assertions.assertEquals(newName, menu.getName());
    }

    @Test
    @DisplayName("Should handle setting name to null")
    void testSetNameWithNullValue() {
        menu.setName(null);
        Assertions.assertNull(menu.getName());
    }
}
