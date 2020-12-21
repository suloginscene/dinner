package me.scene.dinner.account.application.command;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(exclude = "rawPassword")
public class RandomPasswordAppliedEvent {

    private final String email;
    private final String rawPassword;

}
