package com.jiem.lighten.modules.tumor.vo;


import cn.hutool.core.date.DateTime;
import com.jiem.lighten.common.base.pojo.PageCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基础信息 Vo
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseInfoVo extends PageCondition implements Serializable {
    private String id;

    private String registr;

    private String name;

    private String indno;

    private String sex;

    private String age;

    private DateTime birthda;

    private String phone;

    private String mari;

    private String trib;

    private String occu;

    private String workcom;

    private String address;

    private String conservation;

    private DateTime recodate;

    private DateTime updated;


}

