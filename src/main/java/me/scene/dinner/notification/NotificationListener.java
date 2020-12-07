package me.scene.dinner.notification;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.domain.magazine.event.MemberAddedEvent;
import me.scene.dinner.board.domain.magazine.event.MemberAppliedEvent;
import me.scene.dinner.board.domain.magazine.event.MemberQuitEvent;
import me.scene.dinner.board.domain.magazine.event.MemberRemovedEvent;
import me.scene.dinner.like.LikedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component @Transactional
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationRepository notificationRepository;

    // TODO Link
    private static final String ON_LIKE_MSG_TEMPLATE = "%s가 %s를 좋아합니다.";

    private static final String ON_APPLIED_MSG_TEMPLATE = "%s가 %s에 지원했습니다.";

    private static final String ON_QUIT_MSG_TEMPLATE = "%s가 %s에서 탈퇴했습니다.";

    private static final String ON_ADDED_MSG_TEMPLATE = "%s의 멤버로 등록되었습니다.";

    private static final String ON_REMOVED_MSG_TEMPLATE = "%s의 멤버에서 제외되었습니다.";


    @EventListener @Async
    public void onLikedEvent(LikedEvent event) {
        String writer = event.getWriter();
        String user = event.getUser();
        String title = event.getTitle();
        String message = String.format(ON_LIKE_MSG_TEMPLATE, user, title);

        Notification notification = Notification.create(writer, message);
        notificationRepository.save(notification);
    }

    @EventListener
    public void onMemberAppliedEvent(MemberAppliedEvent event) {
        Long magazineId = event.getMagazineId();
        String magazineTitle = event.getMagazineTitle();
        String manager = event.getManager();
        String applicant = event.getApplicant();
        String message = String.format(ON_APPLIED_MSG_TEMPLATE, applicant, magazineTitle);

        Notification notification = Notification.create(manager, message);
        notificationRepository.save(notification);
    }

    @EventListener @Async
    public void onMemberQuitEvent(MemberQuitEvent event) {
        Long magazineId = event.getMagazineId();
        String magazineTitle = event.getMagazineTitle();
        String manager = event.getManager();
        String member = event.getMember();
        String message = String.format(ON_QUIT_MSG_TEMPLATE, member, magazineTitle);

        Notification notification = Notification.create(manager, message);
        notificationRepository.save(notification);
    }

    @EventListener @Async
    public void onMemberAddedEvent(MemberAddedEvent event) {
        Long magazineId = event.getMagazineId();
        String magazineTitle = event.getMagazineTitle();
        String member = event.getMember();
        String message = String.format(ON_ADDED_MSG_TEMPLATE, magazineTitle);

        Notification notification = Notification.create(member, message);
        notificationRepository.save(notification);
    }

    @EventListener @Async
    public void onMemberRemovedEvent(MemberRemovedEvent event) {
        Long magazineId = event.getMagazineId();
        String magazineTitle = event.getMagazineTitle();
        String member = event.getMember();
        String message = String.format(ON_REMOVED_MSG_TEMPLATE, magazineTitle);

        Notification notification = Notification.create(member, message);
        notificationRepository.save(notification);
    }

}
