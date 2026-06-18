package com.safehome.backend.interfaces.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class LoginResponse {
    private String token;
    private UUID userId;
    private String email;
    private String fullName;
    private String role;
}