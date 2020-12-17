package me.scene.dinner.account.application.command;

import lombok.Data;


@Data
public class RandomPasswordAppliedEvent {

    private final String email;
    private final String rawPassword;

}
