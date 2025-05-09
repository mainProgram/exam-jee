package com.groupeisi.services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
}
