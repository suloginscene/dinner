package me.scene.dinner.account.application.query.dto;

import lombok.Data;
import me.scene.dinner.account.domain.account.model.Notification;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;


@Data
public class NotificationView {

    private final List<Notification> uncheckedList;
    private final List<Notification> checkedList;

    public NotificationView(List<Notification> notifications) {
        Map<Boolean, List<Notification>> map = notifications.stream()
                .collect(groupingBy(Notification::isChecked));

        uncheckedList = map.get(false);
        checkedList = map.get(true);
    }

}
