package me.scene.dinner.board.magazine.application.command.event;

import me.scene.dinner.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.dinner.common.notification.event.NotificationEvent;
import me.scene.dinner.common.util.LinkConvertUtils;


public class MemberRemovedEvent extends NotificationEvent {

    private static final String TEMPLATE = "%s의 멤버에서 제외되었습니다.";

    public MemberRemovedEvent(ManagedMagazine magazine, String memberName) {
        super(
                memberName,
                String.format(TEMPLATE,
                        LinkConvertUtils.magazine(magazine.getId(), magazine.getTitle())
                )
        );
    }
}
