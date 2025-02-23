package com.groupeisi.services.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "professeur")
public class ProfesseurEntity {
    @Id
    private String id;

    private String email;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private Boolean archive = false;
}