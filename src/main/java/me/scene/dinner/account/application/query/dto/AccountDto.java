package me.scene.dinner.account.application.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountDto {

    private final String username;

    private final String email;

    @JsonIgnore
    private final String shortIntroduction;

}
