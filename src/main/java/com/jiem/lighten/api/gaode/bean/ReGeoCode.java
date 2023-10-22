package com.jiem.lighten.api.gaode.bean;

import lombok.Data;


/**
 * 根据坐标搜索地址
 *
 * @author: haojunjie
 * @date: 2023-10-21 13:48
 */
@Data
public class ReGeoCode {
    /**
     * 国家
     */
    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 坐标点所在区
     */
    private String district;
    /**
     * 坐标点所在乡镇/街道（此街道为社区街道，不是道路信息）
     */
    private String township;
    /**
     * 结构化地址信息
     */
    private String formattedAddress;

}
