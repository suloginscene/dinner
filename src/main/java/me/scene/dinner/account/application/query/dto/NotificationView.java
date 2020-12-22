package me.scene.dinner.account.application.query.dto;

import lombok.Data;
import me.scene.dinner.account.domain.account.model.Notification;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;


@Data
public class NotificationView {

    private final List<Notification> unchecked;
    private final List<Notification> checked;

    public NotificationView(List<Notification> notifications) {
        Map<Boolean, List<Notification>> map = notifications.stream()
                .collect(groupingBy(Notification::isChecked));

        unchecked = map.get(false);
        checked = map.get(true);
    }

}
