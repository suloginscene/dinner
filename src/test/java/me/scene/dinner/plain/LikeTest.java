package me.scene.dinner.plain;

import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.Type;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.like.domain.Like;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Like")
class LikeTest {

    Article target;

    @BeforeEach
    void create() {
        Magazine magazine = Magazine.create(Type.OPEN, "owner", "magazine", "short", "long");
        Topic topic = new Topic(magazine, "owner", "topic", "short", "long");
        target = new Article(topic, "user", "article", "content", true);
    }


    @Nested class OnCreate {
        @Test
        void likes_article() {
            Like.create("reader", target);
            assertThat(target.getLike()).isEqualTo(1);
        }
    }

    @Nested class OnDestruct {
        @Test
        void disLike_article() {
            Like like = Like.create("reader", target);

            like.destruct();
            assertThat(target.getLike()).isEqualTo(0);
        }

    }

}
