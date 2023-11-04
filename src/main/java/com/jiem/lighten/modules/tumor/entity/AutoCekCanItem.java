package com.jiem.lighten.modules.tumor.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 肿瘤登记报告 重复项
 *
 * @author: haojunjie
 * @date: 2023-11-04 9:54
 */
@Data
@Entity
@Table(name = "tumor_auto_cekcan_item")
public class AutoCekCanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelProperty("主键")
    private Long id;
    /**
     * 父id
     */
    @ExcelIgnore
    private Long cekCanId;

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
