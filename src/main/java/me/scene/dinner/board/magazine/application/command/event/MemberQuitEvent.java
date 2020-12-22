package me.scene.dinner.board.magazine.application.command.event;

import me.scene.dinner.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.dinner.common.notification.event.NotificationEvent;
import me.scene.dinner.util.LinkUtils;


public class MemberQuitEvent extends NotificationEvent {

    private static final String TEMPLATE = "%s가 %s에서 탈퇴했습니다.";


    public MemberQuitEvent(ManagedMagazine magazine, String memberName) {
        super(
                magazine.getOwner().name(),
                String.format(TEMPLATE,
                        LinkUtils.accountLink(memberName),
                        LinkUtils.magazineLink(magazine.getId(), magazine.getTitle())
                )

        );
    }

}
