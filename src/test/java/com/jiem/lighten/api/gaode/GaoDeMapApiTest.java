package com.jiem.lighten.api.gaode;


import com.jiem.lighten.api.gaode.bean.GeoCode;
import com.jiem.lighten.api.gaode.bean.ReGeoCode;
import com.jiem.lighten.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
public class GaoDeMapApiTest {

    @Test
    public void searchLocation() {
        GaoDeMapApi gaoDeMapApi = new GaoDeMapApi();
        List<GeoCode> geoCodes = gaoDeMapApi.searchLocation("郑州", "河南省郑州市金水区阳光嘉园12号楼3单元401");
        log.info(JsonUtils.toJson(geoCodes));
        Assert.notEmpty(geoCodes, "根据地址搜索坐标 查询结果不为空");
    }

    @Test
    public void searchAddressDetail() {
        GaoDeMapApi gaoDeMapApi = new GaoDeMapApi();
        ReGeoCode reGeoCodes = gaoDeMapApi.searchAddressDetail("113.640913,34.817664");
        log.info(JsonUtils.toJson(reGeoCodes));
        Assert.notNull(reGeoCodes, "根据坐标搜索地址 查询结果不为空");
    }
}
