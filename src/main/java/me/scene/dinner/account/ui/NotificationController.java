package me.scene.dinner.account.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.notification.NotificationService;
import me.scene.dinner.account.application.query.NotificationQueryService;
import me.scene.dinner.account.application.query.dto.NotificationView;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.common.security.Current;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    private final NotificationQueryService query;


    @GetMapping("/notifications")
    public String notification(@Current Account current, Model model) {
        String username = current.getUsername();
        NotificationView notificationView = query.find(username);
        service.check(username);

        model.addAttribute("notifications", notificationView);
        return "page/notification/view";
    }

}
