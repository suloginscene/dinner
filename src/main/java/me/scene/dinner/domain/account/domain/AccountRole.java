package me.scene.dinner.domain.account.domain;

public enum AccountRole {
    ADMIN, USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
