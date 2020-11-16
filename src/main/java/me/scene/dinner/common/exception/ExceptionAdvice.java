package me.scene.dinner.common.exception;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.account.application.AlreadyVerifiedException;
import me.scene.dinner.account.domain.VerificationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    private static final String templateMessage = "{}({}) when request \"{}\"";

    private String extractExceptionClass(Exception e) {
        String exceptionClass = e.getClass().toString();
        String[] hierarchy = exceptionClass.split("\\.");
        return hierarchy[hierarchy.length - 1];
    }

    private void warn(HttpServletRequest req, Exception e) {
        log.warn(templateMessage, extractExceptionClass(e), e.getMessage(), req.getRequestURI());
    }

    private void error(HttpServletRequest req, Exception e) {
        log.error(templateMessage, extractExceptionClass(e), e.getMessage(), req.getRequestURI());
    }


    // standard exception ----------------------------------------------------------------------------------------------

    @ExceptionHandler
    public String usernameNotFoundException(HttpServletRequest req, UsernameNotFoundException e) {
        warn(req, e);
        return "error/user_not_found";
    }

    @ExceptionHandler
    public String messagingException(HttpServletRequest req, MessagingException e) {
        warn(req, e);
        return "error/messaging";
    }

    @ExceptionHandler
    public String accessDeniedException(HttpServletRequest req, AccessDeniedException e) {
        warn(req, e);
        return "error/access";
    }


    // custom exception ------------------------------------------------------------------------------------------------

    @ExceptionHandler
    public String boardNotFoundException(HttpServletRequest req, BoardNotFoundException e) {
        warn(req, e);
        return "error/board_not_found";
    }

    @ExceptionHandler
    public String verificationException(HttpServletRequest req, VerificationException e) {
        warn(req, e);
        return "error/verification";
    }

    @ExceptionHandler
    public String alreadyVerifiedException(HttpServletRequest req, AlreadyVerifiedException e) {
        warn(req, e);
        return "error/already_verified";
    }

    @ExceptionHandler
    public String NotDeletableException(HttpServletRequest req, NotDeletableException e) {
        warn(req, e);
        return "error/not_deletable";
    }

    // unspecified exception -----------------------------------------------------------------------------------------------

    @ExceptionHandler
    public String runtimeException(HttpServletRequest req, RuntimeException e) {
        error(req, e);
        return "error/unknown";
    }

}
