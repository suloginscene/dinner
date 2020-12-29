package me.scene.dinner.board.magazine.application.command.event;

import me.scene.dinner.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.dinner.common.notification.event.NotificationEvent;
import me.scene.dinner.common.util.LinkConvertUtils;


public class MemberAppliedEvent extends NotificationEvent {

    private static final String TEMPLATE = "%s가 %s에 지원했습니다.";

    public MemberAppliedEvent(ManagedMagazine magazine, String memberName) {
        super(
                magazine.getOwner().name(),
                String.format(TEMPLATE,
                        LinkConvertUtils.account(memberName),
                        LinkConvertUtils.magazine(magazine.getId(), magazine.getTitle())
                )
        );
    }

}
