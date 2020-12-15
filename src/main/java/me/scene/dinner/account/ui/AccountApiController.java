package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.dto.AccountDto;
import me.scene.dinner.account.application.query.AccountQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountQueryService queryService;

    @GetMapping("/api/accounts/{username}")
    public ResponseEntity<AccountDto> find(@PathVariable String username) {
        return queryService.existsByUsername(username) ?
                ResponseEntity.ok(queryService.findDto(username)) :
                ResponseEntity.notFound().build();
    }

}
