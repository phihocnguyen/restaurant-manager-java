package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {
    private String accUsername;
    private String accPassword;
    private String accEmail;
    private String accDisplayname;
    private Boolean accGender;
    private LocalDate accBday;
    private String accAddress;
    private String accPhone;
    private SimpleRoleDto role;
    private Boolean isdeleted;
    private String cccd;

    // Inner class for simple role
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleRoleDto {
        private String roleName;
    }
} 