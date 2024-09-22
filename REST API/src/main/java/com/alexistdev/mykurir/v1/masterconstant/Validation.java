package com.alexistdev.mykurir.v1.masterconstant;

public class Validation {

    public static final String nameMin = "Name is to Short";

    public static final String nameMax = "Maximum Name's character length is 150 characters";

    public static String nameExist(String name)
    {
        return String.format("Name %s already exists", name);
    }

    public static String success(String name)
    {
        return String.format("%s successfully registered", name);
    }
}
