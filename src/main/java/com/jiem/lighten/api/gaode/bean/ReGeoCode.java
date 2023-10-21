package com.jiem.lighten.api.gaode.bean;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;


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
     * 省
     */
    private String city;
    /**
     * 坐标点所在区
     */
    private String district;

    /**
     * 门牌信息列表
     */
    private StreetNumber streetNumber;

    /**
     * 坐标点所在乡镇/街道（此街道为社区街道，不是道路信息）
     */
    private String township;
    /**
     * 结构化地址信息
     */
    private String formattedAddress;
    /**
     * 城市编码
     */
    @SerializedName("citycode")
    private String cityCode;
    /**
     * 乡镇街道编码
     */
    @SerializedName("towncode")
    private String townCode;
    /**
     * 行政区编码
     */
    @SerializedName("adcode")
    private String adCode;

    /**
     * 其他信息
     */
    private List<Poi> pois;

    @Data
    public static class StreetNumber {
        /**
         * 门牌号
         */
        private String number;
        /**
         * 经纬度坐标
         */
        private String location;
        /**
         * 坐标点所处街道方位
         */
        private String direction;
        /**
         * 门牌地址到请求坐标的距离
         */
        private String distance;
        /**
         * 街道名称
         */
        private String street;
    }

    @Data
    public static class Poi {
        /**
         * id
         */
        private String id;
        /**
         * 名称
         */
        private String name;
        /**
         * 类型
         */
        private String type;
        /**
         * 电话
         */
        private List<String> tel;
        /**
         * 方向
         */
        private String direction;
        /**
         * 所在商圈名称
         */
        @SerializedName("businessarea")
        private String businessArea;
        /**
         * 地址信息
         */
        private String address;

        @SerializedName("poiweight")
        private String poiWeight;

        /**
         * 坐标点
         */
        private String location;
        /**
         * 请求坐标的距离
         */
        private String distance;


    }

}
