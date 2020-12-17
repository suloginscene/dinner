package me.scene.dinner.account.domain.tempaccount;

import me.scene.dinner.account.domain.tempaccount.TempAccount;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TempAccountRepository extends JpaRepository<TempAccount, Long> {

    Optional<TempAccount> findByUsername(String username);

    Optional<TempAccount> findByEmail(String email);

}
