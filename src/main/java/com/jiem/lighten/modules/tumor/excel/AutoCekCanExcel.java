package com.jiem.lighten.modules.tumor.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 肿瘤登记报告查重
 *
 * @author: haojunjie
 * @date: 2023-11-04 9:54
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(20)
public class AutoCekCanExcel {

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

    @ExcelProperty("原始卡")
    private String reco;

    @ExcelProperty("发病部位")
    private String topo;

    @ExcelProperty("病理类型")
    private String morp;

    @ExcelProperty("行为")
    private String beha;

    @ExcelProperty("分级")
    private String grad;

    @ExcelProperty("分期")
    private String stage;

    @ExcelProperty("tnm")
    private String tnm;

    @ExcelProperty("诊断依据")
    private String basi;

    @ExcelProperty("icd10")
    private String icd10;

    @ExcelProperty("确诊日期")
    private String inciden;

    @ExcelProperty("hosp")
    private String hosp;

    @ExcelProperty("门诊号")
    private String caseno;

    @ExcelProperty("诊断结果")
    private String diagnosisResult;

    @ExcelProperty("详细诊断结果")
    private String detailedDiagnosisResult;

    @ExcelProperty("其他")
    private String treaInfo;

    @ExcelProperty("报告日期")
    private String reportd;

    @ExcelProperty("报告医生")
    private String repDoct;
}
