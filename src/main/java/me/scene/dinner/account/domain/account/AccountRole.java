package me.scene.dinner.account.domain.account;

public enum AccountRole {

    ADMIN, USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }

}
