package me.scene.dinner.integration.utils;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.integration.utils.aop.LogAround;
import me.scene.dinner.like.domain.LikeRepository;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LikeTestHelper {

    private final LikeRepository repository;


    @LogAround
    public void clearLikes() {
        repository.deleteAll();
    }

}
