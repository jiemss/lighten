package com.jiem.lighten.common.exception;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 自定义errorPage
 * 直接继承 BasicErrorController
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:32
 */
//@Controller
public class ErrorPageConfig extends BasicErrorController {

    public ErrorPageConfig() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    @RequestMapping(produces = {"text/html"})
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        doError(request);
        return null;
    }

    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        doError(request);
        return null;
    }

    private void doError(HttpServletRequest request) {
        ErrorAttributeOptions defaults = ErrorAttributeOptions.defaults();
        Map<String, Object> model = this.getErrorAttributes(request, defaults);

        //抛出ErrorPageException异常，方便被ExceptionHandlerConfig处理
        String path = model.get("path").toString();
        String status = model.get("status").toString();
        throw new ErrorPageException(status, path);
    }
}
