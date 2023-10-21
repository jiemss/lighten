package com.jiem.lighten.common.exceptionhandler;

/**
 * 登录异常
  * @author: haojunjie
  * @date: 2023-10-21 14:35
  */
public class LoginException extends ServiceException {

    public LoginException(String msg) {
        super("20001",msg);
    }
}
