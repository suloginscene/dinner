package me.scene.paper.service.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.account.application.query.AccountQueryService;
import me.scene.paper.service.account.application.query.dto.AccountInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountQueryService query;


    @GetMapping("/api/accounts/{username}")
    public ResponseEntity<AccountInfo> find(@PathVariable String username) {
        try {
            AccountInfo account = query.accountInfo(username);
            return ResponseEntity.ok(account);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
