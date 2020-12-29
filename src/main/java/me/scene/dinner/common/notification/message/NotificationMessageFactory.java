package me.scene.dinner.common.notification.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NotificationMessageFactory {

    private final LinkConverter converter;


    public String memberApplied(String username, Long id, String title) {
        String account = converter.account(username);
        String magazine = converter.magazine(id, title);

        return account + "가 " + magazine + "에 지원했습니다.";
    }

    public String memberQuit(String username, Long id, String title) {
        String account = converter.account(username);
        String magazine = converter.magazine(id, title);

        return account + "가 " + magazine + "에서 탈퇴했습니다.";
    }

    public String memberAdded(Long id, String title) {
        String magazine = converter.magazine(id, title);

        return magazine + "의 멤버로 등록되었습니다.";
    }

    public String memberRemoved(Long id, String title) {
        String magazine = converter.magazine(id, title);

        return magazine + "의 멤버에서 제외되었습니다.";
    }

    public String articleLiked(String username, Long id, String title) {
        String account = converter.account(username);
        String article = converter.article(id, title);

        return account + "가 " + article + "를 좋아합니다.";
    }

}
