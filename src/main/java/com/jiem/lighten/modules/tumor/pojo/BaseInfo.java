package com.jiem.lighten.modules.tumor.pojo;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 基础信息表
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:25
 */
@Data
@Entity
@Table(name = "tumor_base_info")
public class BaseInfo implements Serializable {

    @Id
    private Long id;

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

