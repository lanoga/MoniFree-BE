package hu.moni.model.dto;

import lombok.Data;

@Data
public class TwoFactorRequest {
    private String username;
    private int code;
}
