package me.scene.dinner.account.application.command.request;

import lombok.Data;


@Data
public class SignupRequest {

    private final String username;
    private final String email;
    private final String password;

}
