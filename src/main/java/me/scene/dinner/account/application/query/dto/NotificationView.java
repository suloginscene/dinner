package me.scene.dinner.account.application.query.dto;

import lombok.Data;
import me.scene.dinner.account.domain.account.model.Notification;

import java.util.ArrayList;
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

        List<Notification> tempUnchecked = map.get(false);
        if (tempUnchecked == null) tempUnchecked = new ArrayList<>();
        uncheckedList = tempUnchecked.stream().map(NotificationData::new).collect(toList());

        List<Notification> tempChecked = map.get(true);
        if (tempChecked == null) tempChecked = new ArrayList<>();
        checkedList = tempChecked.stream().map(NotificationData::new).collect(toList());
    }

}
