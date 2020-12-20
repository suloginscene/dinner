package me.scene.dinner.notification.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.domain.managed.ManagedEvent;
import me.scene.dinner.board.magazine.domain.managed.MemberAppliedEvent;
import me.scene.dinner.board.magazine.domain.managed.MemberQuitEvent;
import me.scene.dinner.like.domain.LikedEvent;
import me.scene.dinner.notification.domain.Notification;
import me.scene.dinner.notification.domain.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static me.scene.dinner.board.magazine.domain.managed.ManagedEvent.Action.ADD;


@Component
@Transactional
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationRepository repository;


    @Async
    @EventListener
    public void onUserLikeArticle(LikedEvent event) {
        String receiver = event.getArticleWriter();

        String user = accountWithLink(event.getUser());
        String article = articleWithLink(event.getArticleId(), event.getArticleTitle());

        String message = String.format("%s가 %s를 좋아합니다.", user, article);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }


    @EventListener
    public void onMemberApplyMagazine(MemberAppliedEvent event) {
        String receiver = event.getManager();

        String member = accountWithLink(event.getName());
        String magazine = magazineWithLink(event.getId(), event.getTitle());

        String message = String.format("%s가 %s에 지원했습니다.", member, magazine);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }

    @Async
    @EventListener
    public void onMemberQuitMagazine(MemberQuitEvent event) {
        String receiver = event.getManager();

        String member = accountWithLink(event.getName());
        String magazine = magazineWithLink(event.getId(), event.getTitle());

        String message = String.format("%s가 %s에서 탈퇴했습니다.", member, magazine);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }

    @EventListener
    public void onManagerManageMember(ManagedEvent event) {
        String receiver = event.getName();

        String magazine = magazineWithLink(event.getId(), event.getTitle());

        String message = String.format(
                (event.getAction() == ADD) ? "%s의 멤버로 등록되었습니다." : "%s의 멤버에서 제외되었습니다.", magazine);

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
