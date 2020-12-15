package me.scene.dinner.account.application.command.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode
public class TempAccountCreatedEvent {

    private final String email;
    private final String verificationToken;

}
