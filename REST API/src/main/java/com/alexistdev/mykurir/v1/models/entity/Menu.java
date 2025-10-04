package com.alexistdev.mykurir.v1.models.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_menus")
public class Menu extends BaseEntity<String> implements Serializable  {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 150, nullable = false)
    private String urlink;

    @Column(length = 150, nullable = false)
    private String classlink;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlink() {
        return urlink;
    }

    public void setUrlink(String urlink) {
        this.urlink = urlink;
    }

    public String getClasslink() {
        return classlink;
    }

    public void setClasslink(String classlink) {
        this.classlink = classlink;
    }
}
