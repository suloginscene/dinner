package me.scene.dinner.account.domain.tempaccount;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
public class TempAccountCreatedEvent {

    private final String email;
    private final String verificationToken;

}
