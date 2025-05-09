package com.groupeisi.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO {
    private String id;
    private User user;
    private String phoneNumber;
    private String address;
    private String registrationNumber;
    private Boolean archive;
}
