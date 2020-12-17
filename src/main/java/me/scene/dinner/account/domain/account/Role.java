package me.scene.dinner.account.domain.account;


public enum Role {

    ADMIN, USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }

}
