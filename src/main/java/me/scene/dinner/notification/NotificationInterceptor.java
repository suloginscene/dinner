package me.scene.dinner.notification;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.query.dto.UserAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class NotificationInterceptor implements HandlerInterceptor {

    private final NotificationService notificationService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return;
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserAccount)) return;

        if (modelAndView == null) return;
        String viewName = modelAndView.getViewName();
        if ((viewName == null) || (viewName.startsWith("redirect:"))) return;


        long count = notificationService.countUnchecked(((UserAccount) principal).getUsername());
        modelAndView.addObject("notificationCount", count);

    }

}
