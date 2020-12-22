package me.scene.dinner.util;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.account.domain.tempaccount.exception.VerificationException;
import me.scene.dinner.board.common.domain.exception.NotDeletableException;
import me.scene.dinner.board.common.domain.exception.NotOwnerException;
import me.scene.dinner.board.magazine.domain.magazine.exception.AuthorizationException;
import me.scene.dinner.board.magazine.domain.magazine.exception.TypeMismatchException;
import me.scene.dinner.common.mail.service.sender.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;


@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    private static final String templateMessage = "{}({}) when request \"{}\"";


//    @ExceptionHandler
//    public String usernameNotFoundException(HttpServletRequest req, UsernameNotFoundException e) {
//        warn(req, e);
//        return "error/user_not_found";
//    }
//
//
//    @ExceptionHandler
//    public String verificationException(HttpServletRequest req, VerificationException e) {
//        warn(req, e);
//        return "error/verification";
//    }
//
//
//    @ExceptionHandler
//    public String noSuchElementException(HttpServletRequest req, NoSuchElementException e) {
//        warn(req, e);
//        return "error/not_found";
//    }
//
//    @ExceptionHandler
//    public String notOwnerException(HttpServletRequest req, NotOwnerException e) {
//        warn(req, e);
//        return "error/not_owner";
//    }
//
//    @ExceptionHandler
//    public String notDeletableException(HttpServletRequest req, NotDeletableException e) {
//        warn(req, e);
//        return "error/not_deletable";
//    }
//
//
//    @ExceptionHandler
//    public String typeMismatchException(HttpServletRequest req, TypeMismatchException e) {
//        warn(req, e);
//        return "error/type_mismatch";
//    }
//
//    @ExceptionHandler
//    public String authorizationException(HttpServletRequest req, AuthorizationException e) {
//        warn(req, e);
//        return "error/access";
//    }
//
//
//    @ExceptionHandler
//    public String runtimeMessagingException(HttpServletRequest req, MailException e) {
//        warn(req, e);
//        return "error/messaging";
//    }
//
//
//    @ExceptionHandler
//    public String runtimeException(HttpServletRequest req, RuntimeException e) {
//        error(req, e);
//        return "error/unknown";
//    }
//
//
//    private void warn(HttpServletRequest req, Exception e) {
//        log.warn(templateMessage, extractExceptionClass(e), e.getMessage(), req.getRequestURI());
//    }
//
//    private void error(HttpServletRequest req, Exception e) {
//        log.error(templateMessage, extractExceptionClass(e), e.getMessage(), req.getRequestURI());
//    }
//
//
//    private String extractExceptionClass(Exception e) {
//        String exceptionClass = e.getClass().toString();
//        String[] hierarchy = exceptionClass.split("\\.");
//        return hierarchy[hierarchy.length - 1];
//    }
//
}
