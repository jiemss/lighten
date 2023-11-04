package com.jiem.lighten.modules.tumor.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 肿瘤登记报告查重
 *
 * @author: haojunjie
 * @date: 2023-11-04 9:54
 */
@Data
@Entity
@Table(name = "tumor_auto_cekcan")
public class AutoCekCan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelProperty("主键")
    private Long id;

    @ExcelProperty("编号")
    private String registr;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("身份证")
    private String indno;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("年龄")
    private String age;

    @ExcelProperty("出生日期")
    private String birthda;

    @ExcelProperty("婚姻状况")
    private String mari;

    @ExcelProperty("民族")
    private String trib;

    @ExcelProperty("职业")
    private String occu;

    @ExcelProperty("工作地")
    private String workcom;

    @ExcelProperty("住址")
    private String address;

    @ExcelProperty("所属街道")
    private String conservation;

    @ExcelProperty("填报时间")
    private String dlc;

    @ExcelProperty("状态")
    private String state;

    @ExcelProperty("caus")
    private String caus;

    @ExcelProperty("deathda")
    private String deathda;

    @ExcelProperty("deadplace")
    private String deadplace;

    @ExcelProperty("surmonth")
    private String surmonth;

    @ExcelProperty("repdoct")
    private String repdoct;
    /**
     * 子项
     */
    @Transient
    @ExcelIgnore
    private List<AutoCekCanItem> dataset;

}
