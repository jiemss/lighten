package com.jiem.lighten.api.gaode.bean;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 地理编码
 *
 * @author: haojunjie
 * @date: 2023-10-21 11:00
 */
@Data
public class GeoCode {

    /**
     * 结构化地址信息
     */
    @SerializedName("formatted_address")
    private String formattedAddress;

    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市编码
     */
    @SerializedName("citycode")
    private String cityCode;
    /**
     * 城市
     */
    private String city;
    /**
     * 所属区域 - 金水区
     */
    private String district;
//    /**
//     * 所属街道
//     */
//    private List<String> street;
//    /**
//     * 门牌
//     */
//    private List<String> number;
    /**
     * 经纬度坐标点
     */
    private String location;
    /**
     * 匹配级别
     */
    private String level;
    /**
     * 区域编码
     */
    @SerializedName("adcode")
    private String adCode;
//
//    private List<String> township;


}
