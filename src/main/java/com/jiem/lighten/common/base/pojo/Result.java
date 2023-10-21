package com.jiem.lighten.common.base.pojo;

import com.jiem.lighten.common.exceptionhandler.ErrorEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回对象
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:37
 */
@Data
public class Result<T> implements Serializable {
    /**
     * 通信数据
     */
    private T data;
    /**
     * 通信状态
     */
    private boolean flag = true;
    /**
     * 通信描述
     */
    private String msg = "操作成功";

    /**
     * 通过静态方法获取实例
     */
    public static <T> Result<T> of(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> of(T data, boolean flag) {
        return new Result<>(data, flag);
    }

    public static <T> Result<T> of(T data, boolean flag, String msg) {
        return new Result<>(data, flag, msg);
    }

    public static Result<String> error(ErrorEnum errorEnum) {
        return new Result<>(errorEnum.getCode(), false, errorEnum.getMsg());
    }

    private Result() {
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(T data, boolean flag) {
        this.data = data;
        this.flag = flag;
    }

    private Result(T data, boolean flag, String msg) {
        this.data = data;
        this.flag = flag;
        this.msg = msg;
    }

}
