package com.safehome.backend.interfaces.dto;

import lombok.Data;

@Data
public class ZoneResponse {
    private String name;
    private Long deviceCount;
    private String status;
    private Integer signalStrength;
}