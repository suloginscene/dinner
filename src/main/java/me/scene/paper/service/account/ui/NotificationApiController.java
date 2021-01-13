package me.scene.paper.service.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.account.application.query.NotificationQueryService;
import me.scene.paper.common.framework.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationQueryService query;


    @GetMapping("/api/notifications/count")
    public long countUnchecked(@Principal String username) {
        return query.countUnchecked(username);
    }

}
