package me.scene.dinner.notification.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.domain.ManagedMagazine;
import me.scene.dinner.like.domain.LikedEvent;
import me.scene.dinner.notification.domain.Notification;
import me.scene.dinner.notification.domain.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationRepository repository;


    @Async
    @EventListener
    public void onLikedEvent(LikedEvent event) {
        String receiver = event.getArticleWriter();

        String user = accountWithLink(event.getUser());
        String article = articleWithLink(event.getArticleId(), event.getArticleTitle());

        String message = String.format("%s가 %s를 좋아합니다.", user, article);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }


    @EventListener
    public void notifyManager(ManagedMagazine.Member.ManagerEvent event) {
        String receiver = event.getManagerName();

        String member = accountWithLink(event.getMemberName());
        String magazine = magazineWithLink(event.getMagazineId(), event.getMagazineTitle());

        String template;
        switch (event.getStatus()) {
            case APPLIED:
                template = "%s가 %s에 지원했습니다.";
                break;
            case QUIT:
                template = "%s가 %s에서 탈퇴했습니다.";
                break;
            default:
                throw new IllegalStateException("ManagerEvent status should be APPLIED or QUIT.");
        }
        String message = String.format(template, member, magazine);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }

    @EventListener
    public void notifyMember(ManagedMagazine.Member.MemberEvent event) {
        String receiver = event.getMemberName();

        String magazine = magazineWithLink(event.getMagazineId(), event.getMagazineTitle());

        String template;
        switch (event.getStatus()) {
            case ADDED:
                template = "%s의 멤버로 등록되었습니다.";
                break;
            case REMOVED:
                template = "%s의 멤버에서 제외되었습니다.";
                break;
            default:
                throw new IllegalStateException("MemberEvent status should be ADDED or REMOVED.");
        }
        String message = String.format(template, magazine);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }


    private String accountWithLink(String username) {
        return String.format("<a href=\"/@%s\">%s</a>", username, username);
    }

    private String articleWithLink(Long id, String title) {
        return String.format("<a href=\"/articles/%s\">%s</a>", id, title);
    }

    private String magazineWithLink(Long id, String title) {
        return String.format("<a href=\"/magazines/%s\">%s</a>", id, title);
    }

}
