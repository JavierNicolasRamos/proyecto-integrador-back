package com.proyecto.integrador.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private Boolean isAdmin;
    private Integer areaCode;
    private Integer prefix;
    private Integer phone;
    private Boolean isMobile;
    private String email;
    private String password;
}
