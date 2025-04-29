package com.cuzz.springaidemo.models;

import lombok.Data;

@Data
public class TestAccount {
    private String username;
    private String password;
    private String email;
    private String verificationCode;

}
