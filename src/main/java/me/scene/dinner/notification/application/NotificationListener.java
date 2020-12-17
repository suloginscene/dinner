package me.scene.dinner.notification.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.magazine.application.member.MemberAddedEvent;
import me.scene.dinner.board.magazine.application.member.MemberAppliedEvent;
import me.scene.dinner.board.magazine.application.member.MemberQuitEvent;
import me.scene.dinner.board.magazine.application.member.MemberRemovedEvent;
import me.scene.dinner.like.domain.LikedEvent;
import me.scene.dinner.notification.domain.Notification;
import me.scene.dinner.notification.domain.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component @Transactional
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationRepository repository;

    private static final String ON_LIKE_MSG_TEMPLATE = "%s가 %s를 좋아합니다.";
    private static final String ON_APPLIED_MSG_TEMPLATE = "%s가 %s에 지원했습니다.";
    private static final String ON_QUIT_MSG_TEMPLATE = "%s가 %s에서 탈퇴했습니다.";
    private static final String ON_ADDED_MSG_TEMPLATE = "%s의 멤버로 등록되었습니다.";
    private static final String ON_REMOVED_MSG_TEMPLATE = "%s의 멤버에서 제외되었습니다.";

    private static final String ACCOUNT_LINK = "<a href=\"/@%s\">%s</a>";
    private static final String ARTICLE_LINK = "<a href=\"/articles/%s\">%s</a>";
    private static final String MAGAZINE_LINK = "<a href=\"/magazines/%s\">%s</a>";

    private String accountWithLink(String username) {
        return String.format(ACCOUNT_LINK, username, username);
    }

    private String articleWithLink(Long id, String title) {
        return String.format(ARTICLE_LINK, id, title);
    }

    private String magazineWithLink(Long id, String title) {
        return String.format(MAGAZINE_LINK, id, title);
    }


    @Async
    @EventListener
    public void onLikedEvent(LikedEvent event) {
        String receiver = event.getArticleWriter();

        String user = accountWithLink(event.getUser());
        String article = articleWithLink(event.getArticleId(), event.getArticleTitle());
        String message = String.format(ON_LIKE_MSG_TEMPLATE, user, article);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }

    @EventListener
    public void onMemberAppliedEvent(MemberAppliedEvent event) {
        String receiver = event.getManager();

        String applicant = accountWithLink(event.getApplicant());
        String magazine = magazineWithLink(event.getMagazineId(), event.getMagazineTitle());
        String message = String.format(ON_APPLIED_MSG_TEMPLATE, applicant, magazine);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }

    @Async
    @EventListener
    public void onMemberQuitEvent(MemberQuitEvent event) {
        String receiver = event.getManager();

        String member = accountWithLink(event.getMember());
        String magazine = magazineWithLink(event.getMagazineId(), event.getMagazineTitle());
        String message = String.format(ON_QUIT_MSG_TEMPLATE, member, magazine);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }

    @Async
    @EventListener
    public void onMemberAddedEvent(MemberAddedEvent event) {
        String receiver = event.getMember();

        String magazine = magazineWithLink(event.getMagazineId(), event.getMagazineTitle());
        String message = String.format(ON_ADDED_MSG_TEMPLATE, magazine);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }

    @Async
    @EventListener
    public void onMemberRemovedEvent(MemberRemovedEvent event) {
        String receiver = event.getMember();

        String magazine = magazineWithLink(event.getMagazineId(), event.getMagazineTitle());
        String message = String.format(ON_REMOVED_MSG_TEMPLATE, magazine);

        Notification notification = new Notification(receiver, message);
        repository.save(notification);
    }

}
