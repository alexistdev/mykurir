package com.alexistdev.mykurir.v1.models.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="tb_provinces")
public class Province extends BaseEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=150, nullable=false)
    private String name;

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
        if(name == null || name.trim().isEmpty()){
            throw new NullPointerException("Name cannot be null or empty");
        }
        this.name = name;
    }
}
