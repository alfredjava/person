package com.person.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
    private String cityCode;
    private String contryCode;
    private UUID userId;
}
