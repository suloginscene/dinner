package me.scene.paper.service.account.domain.account.repository;

import me.scene.paper.service.account.domain.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("select" +
            " count(n)" +
            " from Notification n" +
            " where n.receiver = :username and n.checked = false")
    long countUncheckedNotification(@Param("username") String username);


    default Account find(String username) {
        return findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    default Account findAccountByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

}
