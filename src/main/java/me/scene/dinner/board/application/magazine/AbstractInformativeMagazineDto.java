package me.scene.dinner.board.application.magazine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class AbstractInformativeMagazineDto {

    @JsonIgnore
    protected final String policy;

    @JsonIgnore
    protected final String manager;

    @JsonIgnore
    protected final List<String> members;

    public boolean doesAccept(String current) {
        if (policy.equals("OPEN")) return true;
        if (policy.equals("EXCLUSIVE")) return current.equals(manager);
        if (policy.equals("MANAGED")) {
            if (current.equals(manager)) return true;
            return members.contains(current);
        }
        throw new IllegalStateException("Magazine should have policy in enum");
    }

    @JsonIgnore
    public boolean isOpen() {
        return policy.equals("OPEN");
    }

    @JsonIgnore
    public boolean isManaged() {
        return policy.equals("MANAGED");
    }

}
