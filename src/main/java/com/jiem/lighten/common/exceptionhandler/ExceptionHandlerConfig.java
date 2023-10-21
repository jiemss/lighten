package com.jiem.lighten.common.exceptionhandler;

import com.jiem.lighten.common.base.pojo.Result;
import com.jiem.lighten.common.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 统一异常处理
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:34
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerConfig {

    /**
     * 业务异常，错误页面异常，登录异常
     * 统一处理
     */
    @ExceptionHandler(value = {ServiceException.class, ErrorPageException.class, LoginException.class})
    @ResponseBody
    public Object exceptionHandler400(ServiceException e) {
        ErrorEnum errorEnum = e.getErrorEnum();
        if (null == errorEnum) {
            return returnResult(e, Result.of(e.getCode(), false, e.getErrorMsg()));
        }
        return returnResult(e, Result.error(errorEnum));
    }

    /**
     * 空指针异常 统一处理
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Object exceptionHandler500(NullPointerException e) {
        return returnResult(e, Result.error(ErrorEnum.INTERNAL_SERVER_ERROR));
    }

    /**
     * 其他异常 统一处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exceptionHandler(Exception e) {
        return returnResult(e, Result.of(ErrorEnum.UNKNOWN.getCode(), false, "【" + e.getClass().getName() + "】" + e.getMessage()));
    }

    /**
     * 是否为ajax请求
     * ajax请求，响应json格式数据，否则应该响应html页面
     */
    private Object returnResult(Exception e, Result errorResult) {
        //把错误信息输入到日志中
        log.error(ErrorUtil.errorInfoToString(e));

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        //判断是否为ajax请求
        if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
            //重新设置http响应状态
            response.setStatus(200);
            return errorResult;
        } else {
            ModelAndView modelAndView = new ModelAndView("common/error", "msg", errorResult.getMsg());
            //重新设置状态码，例如：404
            modelAndView.setStatus(HttpStatus.valueOf(response.getStatus()));
            return modelAndView;
        }
    }
}
