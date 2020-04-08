package com.itheima.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandle {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);
    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException(Exception ex) {
        logger.error(ex.getMessage());
        return "redirect:/noAuth";
    }

    //    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(Exception ex) {
        logger.error(ex.getMessage());
        return "redirect:/noAuth";
    }


}
