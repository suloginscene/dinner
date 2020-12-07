package me.scene.dinner.board.domain.topic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MagazineSummary {

    private final Long id;

    private final String manager;

    private final String title;

    private final String policy;

    private final List<String> members;

    public boolean doesAccept(String current) {
        if (policy.equals("OPEN")) return true;
        if (policy.equals("EXCLUSIVE")) return current.equals(manager);
        if (policy.equals("MANAGED")) {
            if (current.equals(manager)) return true;
            return members.contains(current);
        }
        throw new IllegalStateException("Magazine should have policy in enum");
    }

}
