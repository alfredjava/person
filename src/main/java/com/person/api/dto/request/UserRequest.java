package com.person.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {

    @NotBlank(message = "El campo 'name' no puede estar en blanco")
    private String name;

    @NotBlank(message = "El campo 'email' no puede estar en blanco")
    @Email
    private String email;

    @NotBlank(message = "El campo 'password' no puede estar en blanco")
    private String password;

    private List<PhoneRequest> phones;
}



