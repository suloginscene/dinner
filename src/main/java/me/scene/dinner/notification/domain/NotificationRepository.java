package me.scene.dinner.notification.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverAndChecked(String receiver, boolean checked);

    long countByReceiverAndChecked(String receiver, boolean checked);

}
