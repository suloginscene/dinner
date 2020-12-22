package me.scene.dinner.account.domain.tempaccount.repository;

import me.scene.dinner.account.domain.tempaccount.model.TempAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public interface TempAccountRepository extends JpaRepository<TempAccount, Long> {

    Optional<TempAccount> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


    default TempAccount findAccountByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

}
