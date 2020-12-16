package me.scene.dinner.common.config;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.notification.ui.NotificationInterceptor;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final NotificationInterceptor notificationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> staticResourcePaths = Arrays.stream(StaticResourceLocation.values())
                .flatMap(StaticResourceLocation::getPatterns)
                .collect(Collectors.toList());
        staticResourcePaths.add("/node_modules/**");
        staticResourcePaths.add("/fonts/**");

        registry.addInterceptor(notificationInterceptor)
                .excludePathPatterns(staticResourcePaths);
    }

}
