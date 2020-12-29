package me.scene.dinner.board.magazine.application.command.event;

import me.scene.dinner.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.dinner.common.notification.event.NotificationEvent;
import me.scene.dinner.common.util.LinkConvertUtils;


public class MemberAddedEvent extends NotificationEvent {

    private static final String TEMPLATE = "%s의 멤버로 등록되었습니다.";

    public MemberAddedEvent(ManagedMagazine magazine, String memberName) {
        super(
                memberName,
                String.format(TEMPLATE,
                        LinkConvertUtils.magazine(magazine.getId(), magazine.getTitle())
                )
        );
    }
}
