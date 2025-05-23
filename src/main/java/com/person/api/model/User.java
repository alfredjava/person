package com.person.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("users")
public class User {
    @Id
    private UUID id;
    private String name;


    private String email;

    private String password;

    private LocalDateTime created;
    private LocalDateTime modified;
    @Column("last_login")
    private LocalDateTime lastLogin;
    private String token;
    @Column("active")
    private boolean isActive;

    @Transient
    private List<Phone> phones;
}
