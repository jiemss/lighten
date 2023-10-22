package com.jiem.lighten.api.gaode;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jiem.lighten.api.gaode.bean.ReGeoCode;
import com.jiem.lighten.common.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 高德地址API
 *
 * @author: haojunjie
 * @date: 2023-10-21 10:30
 */
@Slf4j
@Component
public class GaoDeMapApi {
    private static final String SEARCH_LOCATION_URL = "https://restapi.amap.com/v3/geocode/geo";
    private static final String SEARCH_ADDRESS_DETAIL_URL = "https://restapi.amap.com/v3/geocode/regeo";


    /**
     * 根据地址详细信息
     *
     * @param city    城市
     * @param address 详细地址
     */
    public ReGeoCode searchAddressDetail(String city, String address) {
        String location = searchLocation(city, address);
        if (StrUtil.isNotBlank(location)) {
            return searchAddressDetail(location);
        }
        return null;
    }


    /**
     * 根据地址搜索坐标
     *
     * @param city    城市
     * @param address 详细地址
     */
    private String searchLocation(String city, String address) {
        log.info("根据地址搜索坐标 入参：city=[{}] address=[{}]", city, address);
        HttpRequest request = HttpUtil.createGet(SEARCH_LOCATION_URL);
        request.form("key", getKey());
        request.form("city", city);
        request.form("address", address);
        try (HttpResponse execute = request.execute()) {
            String body = execute.body();
            log.info("根据地址搜索坐标 结果：{}", body);
            JsonObject resultJson = GsonUtils.parse(body);
            if (resultJson != null && "1".equals(resultJson.get("status").getAsString())) {
                JsonElement geocodesJsonElement = resultJson.get("geocodes");
                if (geocodesJsonElement != null) {
                    JsonArray geocodesJsonArray = geocodesJsonElement.getAsJsonArray();
                    JsonElement jsonElement = geocodesJsonArray.get(0);
                    if (jsonElement != null) {
                        JsonElement location = jsonElement.getAsJsonObject().get("location");
                        if (location != null) {
                            return location.getAsString();
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * 根据坐标搜索地址
     *
     * @param location 经纬度坐标点
     */
    private ReGeoCode searchAddressDetail(String location) {
        log.info("根据坐标搜索地址 入参：location=[{}]", location);
        HttpRequest request = HttpUtil.createGet(SEARCH_ADDRESS_DETAIL_URL);
        request.form("key", getKey());
        request.form("radius", 50);
        request.form("batch", false);
        request.form("roadlevel", 0);
        request.form("extensions", "all");
        request.form("location", location);
        try (HttpResponse execute = request.execute()) {
            String body = execute.body();
            log.info("根据坐标搜索地址 结果：{}", body);
            JsonObject resultJson = GsonUtils.parse(body);
            if (resultJson != null && "1".equals(resultJson.get("status").getAsString())) {
                JsonElement reGeoCodeJsonElement = resultJson.get("regeocode");
                if (reGeoCodeJsonElement != null) {
                    JsonObject reGeoCodeJsonObject = reGeoCodeJsonElement.getAsJsonObject();
                    if (reGeoCodeJsonObject != null) {
                        ReGeoCode reGeoCode = GsonUtils.gson().fromJson(reGeoCodeJsonObject.getAsJsonObject().get("addressComponent"), ReGeoCode.class);
                        reGeoCode.setFormattedAddress(reGeoCodeJsonObject.get("formatted_address").getAsString());
                        return reGeoCode;
                    }
                }
            }
        }
        return new ReGeoCode();
    }

    /**
     * 获取授权key
     *
     * @return key
     */
    private String getKey() {
        return "ca1754d49d6de4202fefb064f2a23364";
//        return "5036988c951dceb125dd80e279dc13e1";
    }


}
