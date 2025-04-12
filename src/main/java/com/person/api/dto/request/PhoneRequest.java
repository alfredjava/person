package com.person.api.dto.request;

import lombok.Data;

@Data
public class PhoneRequest {
    private String number;
    private String cityCode;
    private String countryCode;
}
