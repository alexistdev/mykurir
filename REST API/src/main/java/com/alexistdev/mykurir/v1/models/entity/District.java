package com.alexistdev.mykurir.v1.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="tb_districts")
@NoArgsConstructor
public class District {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(length=150, nullable=false)
    private String name;

    @ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "regency_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Regency regency;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Regency getRegency() {
        return regency;
    }

    public void setRegency(Regency regency) {
        if(regency == null){
            throw new NullPointerException("Regency cannot be null");
        }
        this.regency = regency;
    }
}
