package me.scene.dinner.notification.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.common.security.CurrentUser;
import me.scene.dinner.notification.domain.Notification;
import me.scene.dinner.notification.application.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String notification(@CurrentUser Account current, Model model) {
        List<Notification> unchecked = notificationService.findUnchecked(current.getUsername());
        List<Notification> checked = notificationService.findChecked(current.getUsername());
        notificationService.check(unchecked);
        model.addAttribute("uncheckedList", unchecked);
        model.addAttribute("checkedList", checked);
        return "page/notification/view";
    }

    @GetMapping("/sent-to-manager")
    public String sentToManager(@RequestParam Long magazineId, Model model) {
        model.addAttribute("magazineId", magazineId);
        return "page/notification/manager";
    }

}
