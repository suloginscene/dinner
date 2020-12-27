package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.NotificationQueryService;
import me.scene.dinner.common.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationQueryService query;


    @GetMapping("/api/notifications/count-unchecked")
    public long countUnchecked(@Principal String username) {
        if (username == null) return 0L;

        return query.countUnchecked(username);
    }

}
