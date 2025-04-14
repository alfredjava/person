package com.person.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("phones")
public class Phone {
    @Id
    private UUID id;
    private String number;
    @Column("city_code")
    private String cityCode;
    @Column("country_code")
    private String countryCode;
    @Column("user_id")
    private UUID userId;
}
