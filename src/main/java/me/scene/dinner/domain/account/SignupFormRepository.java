package me.scene.dinner.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignupFormRepository extends JpaRepository<SignupForm, Long> {
    Optional<SignupForm> findByUsername(String username);

    Optional<SignupForm> findByEmail(String email);
}
