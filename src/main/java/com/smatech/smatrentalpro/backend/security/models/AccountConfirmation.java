package com.smatech.smatrentalpro.backend.security.models;

import lombok.Data;

@Data
public class AccountConfirmation {
    private String username;
    private String code;
}
