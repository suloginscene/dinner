package me.scene.dinner.account.domain.tempaccount;

import lombok.Data;


@Data
public class TempAccountCreatedEvent {

    private final String email;
    private final String verificationToken;

}
