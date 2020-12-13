package me.scene.dinner.account.domain.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter @EqualsAndHashCode(exclude = "tempRawPassword")
public class TempPasswordIssuedEvent {

    private final String email;
    private final String tempRawPassword;

}
