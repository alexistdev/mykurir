package com.alexistdev.mykurir.v1.entity;

import com.alexistdev.mykurir.v1.models.entity.Role;
import org.apache.commons.lang3.EnumUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleTest {
    @Test
    void testEnumValues(){
        Role[] roles = Role.values();
        Assertions.assertEquals(4, roles.length);
        Assertions.assertEquals(Role.USER, roles[0]);
        Assertions.assertEquals(Role.COURIER, roles[1]);
        Assertions.assertEquals(Role.STAFF, roles[2]);
        Assertions.assertEquals(Role.ADMIN, roles[3]);
    }

    @Test
    void testEnumValuesExist(){
        Assertions.assertTrue(EnumUtils.isValidEnum(Role.class, "ADMIN"));
        Assertions.assertTrue(EnumUtils.isValidEnum(Role.class, "USER"));
        Assertions.assertTrue(EnumUtils.isValidEnum(Role.class, "COURIER"));
        Assertions.assertTrue(EnumUtils.isValidEnum(Role.class, "STAFF"));
    }

    @Test
    void testEnumValuesNotExist(){
        Assertions.assertFalse(EnumUtils.isValidEnum(Role.class, "MANAGER"));
        Assertions.assertFalse(EnumUtils.isValidEnum(Role.class, "SUPER ADMIN"));
    }

    @Test
    void testEnumOrdinal(){
        Assertions.assertSame(0,Role.USER.ordinal());
        Assertions.assertSame(1,Role.COURIER.ordinal());
        Assertions.assertSame(2,Role.STAFF.ordinal());
        Assertions.assertSame(3,Role.ADMIN.ordinal());
    }

    @Test
    void testEnumValueOf() {
        Assertions.assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        Assertions.assertEquals(Role.COURIER, Role.valueOf("COURIER"));
        Assertions.assertEquals(Role.STAFF, Role.valueOf("STAFF"));
        Assertions.assertEquals(Role.USER, Role.valueOf("USER"));
    }
}
