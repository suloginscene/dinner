package me.scene.dinner.board.article.application.command.event;

import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.common.notification.event.NotificationEvent;
import me.scene.dinner.common.util.LinkConvertUtils;


public class LikedEvent extends NotificationEvent {

    private static final String TEMPLATE = "%s가 %s를 좋아합니다.";


    public LikedEvent(String reader, Article article) {
        super(
                article.getOwner().name(),
                String.format(TEMPLATE,
                        LinkConvertUtils.account(reader),
                        LinkConvertUtils.article(article.getId(), article.getTitle())
                )
        );
    }

}
