package me.scene.dinner.account.application.query.dto;

import lombok.Data;
import me.scene.dinner.account.domain.account.model.Notification;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


@Data
public class NotificationView {

    private final List<NotificationData> uncheckedList;
    private final List<NotificationData> checkedList;

    public NotificationView(List<Notification> notifications) {
        Map<Boolean, List<Notification>> map = notifications.stream()
                .collect(groupingBy(Notification::isChecked));

        uncheckedList = map.get(false).stream().map(NotificationData::new).collect(toList());
        checkedList = map.get(true).stream().map(NotificationData::new).collect(toList());
    }

}
