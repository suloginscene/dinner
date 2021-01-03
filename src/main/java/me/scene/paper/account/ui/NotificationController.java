package me.scene.paper.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.account.application.command.NotificationService;
import me.scene.paper.account.application.query.NotificationQueryService;
import me.scene.paper.account.application.query.dto.NotificationView;
import me.scene.paper.common.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    private final NotificationQueryService query;


    @GetMapping("/notifications")
    public String notification(@Principal String username, Model model) {
        NotificationView notificationView = query.find(username);
        service.check(username);

        model.addAttribute("notifications", notificationView);
        return "page/account/notification";
    }

}
