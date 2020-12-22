package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.NotificationQueryService;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.common.security.Current;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationQueryService query;


    @GetMapping("/api/notifications/count-unchecked")
    public long countUnchecked(@Current Account current) {
        if (current == null) return 0L;

        String username = current.getUsername();
        return query.countUnchecked(username);
    }

}
