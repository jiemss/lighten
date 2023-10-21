package com.jiem.lighten.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 捕获报错日志处理工具类
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:39
 */
public class ErrorUtil {

    /**
     * Exception出错的栈信息转成字符串
     * 用于打印到日志中
     */
    public static String errorInfoToString(Throwable e) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            return sw.toString();
        } catch (Exception e1) {
            throw new RuntimeException(e1.getMessage(), e1);
        }
    }
}
