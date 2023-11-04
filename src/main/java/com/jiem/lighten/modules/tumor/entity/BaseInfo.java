package com.jiem.lighten.modules.tumor.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 基础信息表
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:25
 */
@Data
@Entity
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(20)
@Table(name = "tumor_base_info")
public class BaseInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ExcelProperty("主键")
    private Long id;

    @ExcelProperty("编号")
    private String registr;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("身份证号")
    private String indno;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("出生年月")
    private String birthda;

    @ExcelProperty("联系电话")
    private String phone;

    @ExcelProperty("婚姻状况")
    private String mari;

    @ExcelProperty("民族")
    private String trib;

    @ExcelProperty("工作单位")
    private String occu;

    @ExcelProperty("职业")
    private String workcom;

    @ExcelProperty("户籍地址")
    private String address;

    @ExcelProperty("常见地址")
    private String conservation;

    @ExcelProperty("登记时间")
    private String recodate;

    @ExcelProperty("更新时间")
    private String updated;

    @ExcelProperty("是否空地址")
    private Boolean isJinShuiNull;

    @ExcelProperty("是否已转化")
    private Boolean isConvert;

    @ExcelProperty("所属城市")
    private String city;

    @ExcelProperty("所属区编码")
    private Long districtCode;

    @ExcelProperty("所属区")
    private String district;

    @ExcelProperty("所属街道编码")
    private Long streetCode;

    @ExcelProperty("所属街道")
    private String street;

    @ExcelProperty("是否已结束")
    private Boolean isOk;
}

