package com.jiem.lighten.modules.tumor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jiem.lighten.api.gaode.GaoDeMapApi;
import com.jiem.lighten.api.gaode.bean.GeoCode;
import com.jiem.lighten.api.gaode.bean.ReGeoCode;
import com.jiem.lighten.common.base.service.CommonServiceImpl;
import com.jiem.lighten.common.util.GsonUtils;
import com.jiem.lighten.common.util.JsonUtils;
import com.jiem.lighten.modules.tumor.pojo.AddressDict;
import com.jiem.lighten.modules.tumor.pojo.BaseInfo;
import com.jiem.lighten.modules.tumor.repository.AddressDictRepository;
import com.jiem.lighten.modules.tumor.repository.BaseInfoRepository;
import com.jiem.lighten.modules.tumor.service.BaseInfoService;
import com.jiem.lighten.modules.tumor.vo.BaseInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 基础信息 ServiceImpl
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:51
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseInfoServiceImpl extends CommonServiceImpl<BaseInfoVo, BaseInfo, Long> implements BaseInfoService {
    private static final String CONTEXT_URL = "http://218.28.13.204:35556/";

    private static final String BASE_URL = CONTEXT_URL + "/Program/BaseInfo/MsgBaseInfo";
    private static final String ADDRESS_DICT_URL = CONTEXT_URL + "/Dictionaries/Address";
    private static Map<String, List<AddressDict>> addressDictMap = null;

    @Resource
    private BaseInfoRepository baseInfoRepository;
    @Resource
    private AddressDictRepository addressDictRepository;
    @Resource
    private GaoDeMapApi gaoDeMapApi;

    @Override
    public void popAndSaveBaseInfo(String cookie) {
        int pageIndex = 0;
        int recordCount = 100;
        while (true) {
            log.info("拉取远程数据 pageIndex={} recordCount={}", pageIndex, recordCount);
            List<BaseInfo> baseInfos = popBaseInfo(pageIndex, recordCount, cookie);
            if (CollectionUtil.isEmpty(baseInfos)) {
                break;
            }
            pageIndex++;
            for (BaseInfo baseInfo : baseInfos) {
                BaseInfo dbBaseInfo = baseInfoRepository.findFirstByRegistr(baseInfo.getRegistr());
                if (dbBaseInfo != null) {
                    continue;
                }
                baseInfo.setId(null);
                baseInfoRepository.save(baseInfo);
            }
        }
        log.info("拉取远程数据 结束 ... ");
    }

    @Override
    public void popAndSaveAddressDict(String cookie) {
        List<AddressDict> addressDictList = popAddressDict(410105000000L, true, cookie);
        // 河南省
        for (AddressDict addressDict : addressDictList) {
            if (null == addressDictRepository.findFirstByValue(addressDict.getValue())) {
                addressDict.setId(null);
                addressDictRepository.save(addressDict);
            }
            // 郑州市
            List<AddressDict> firstAddressDictList = addressDict.getNodes();
            for (AddressDict firstAddressDict : firstAddressDictList) {
                if (null == addressDictRepository.findFirstByValue(firstAddressDict.getValue())) {
                    firstAddressDict.setId(null);
                    addressDictRepository.save(firstAddressDict);
                }
                // 金水区
                List<AddressDict> twoAddressDictList = popAddressDict(firstAddressDict.getValue(), false, cookie);
                for (AddressDict twoAddressDict : twoAddressDictList) {
                    if (null == addressDictRepository.findFirstByValue(twoAddressDict.getValue())) {
                        twoAddressDict.setId(null);
                        addressDictRepository.save(twoAddressDict);
                    }
                    // 街道
                    List<AddressDict> threeAddressDictList = popAddressDict(twoAddressDict.getValue(), false, cookie);
                    for (AddressDict threeAddress : threeAddressDictList) {
                        if (null == addressDictRepository.findFirstByValue(threeAddress.getValue())) {
                            threeAddress.setId(null);
                            addressDictRepository.save(threeAddress);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void convertJieDao() {
        int pageIndex = 1;
        int recordCount = 100;
        while (true) {
            PageRequest pageRequest = PageRequest.of(pageIndex - 1, recordCount, Sort.by("id"));
            Page<BaseInfo> baseInfos = baseInfoRepository.findAll(pageRequest);
            if (CollectionUtil.isEmpty(baseInfos) || baseInfos.isEmpty()) {
                break;
            }
            pageIndex++;
            for (BaseInfo baseInfo : baseInfos) {
                // 已结束
                Boolean isOk = baseInfo.getIsOk();
                Boolean isConvert = baseInfo.getIsConvert();
                if ((isConvert != null && isConvert) || (isOk != null && isOk)) {
                    continue;
                }

                String address = baseInfo.getAddress();
                if (isJinShuiNull(address)) {
                    baseInfo.setIsJinShuiNull(true);
                    baseInfoRepository.save(baseInfo);
                    continue;
                } else {
                    baseInfo.setIsJinShuiNull(false);
                }

                AddressDict addressDict = belongAddressDict(address);
                if (addressDict == null) {
                    List<GeoCode> geoCodeList = gaoDeMapApi.searchLocation("郑州", address);
                    if (CollectionUtil.isNotEmpty(geoCodeList)) {
                        GeoCode geoCode = geoCodeList.get(0);
                        ReGeoCode reGeoCode = gaoDeMapApi.searchAddressDetail(geoCode.getLocation());
                        addressDict = belongAddressDict2(reGeoCode.getTownship());
                        baseInfo.setCity(reGeoCode.getCity());
                        baseInfo.setDistrict(reGeoCode.getDistrict());
                        baseInfo.setStreet(reGeoCode.getTownship());
                    }
                }
                if (addressDict != null) {
                    baseInfo.setIsConvert(true);
                    baseInfo.setDistrictCode(addressDict.getUpId());
                    baseInfo.setStreetCode(addressDict.getValue());
                    baseInfo.setStreet(addressDict.getText());
                }
                baseInfoRepository.save(baseInfo);
            }
        }
    }


    @Override
    public BiConsumer<ExcelWriter, WriteSheet> excel() {
        return (excelWriter, writeSheet) -> {
            int pageIndex = 1;
            int recordCount = 100;
            while (true) {
                PageRequest pageRequest = PageRequest.of(pageIndex - 1, recordCount, Sort.by("id"));
                Page<BaseInfo> baseInfos = baseInfoRepository.findAll(pageRequest);
                if (CollectionUtil.isEmpty(baseInfos) || baseInfos.isEmpty()) {
                    break;
                }
                pageIndex++;
                List<BaseInfo> content = baseInfos.getContent();
                for (BaseInfo baseInfo : content) {
                    String address = removeMatch(baseInfo.getAddress());
                    if (StringUtils.hasText(address)) {
                        address = address.replaceAll("河南省", "");
                        address = address.replaceAll("郑州市", "");
                        address = address.replaceAll("金水区", "");
                        address = address.replaceAll("河南", "");
                        address = address.replaceAll("郑州", "");
                        address = address.replaceAll("金水", "");
                        address = address.replaceAll("市辖区", "");
                        address = address.replaceAll("不详", "");
                        address = address.replaceAll("镇", "");
                        address = address.replaceAll("乡", "");
                        address = address.replaceAll("郑", "");
                        address = address.replaceAll("河", "");
                        address = address.replaceAll("金", "");
                        if (!StringUtils.hasText(address)) {
                            excelWriter.write(Collections.singletonList(baseInfo), writeSheet);
                            continue;
                        }
                    }
                    //excelWriter.write(Collections.singletonList(baseInfo), writeSheet);
                }

            }
        };
    }


    /**
     * 分页查询
     *
     * @param pageNumber 页码
     * @param pageSize   每页个数
     * @return 分页结果集合
     */
    public Page<BaseInfo> getListPage(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        return baseInfoRepository.findAll(pageRequest);
    }

    /**
     * 远程拉取基础信息
     *
     * @param pageIndex   第几页
     * @param recordCount 每页大小
     * @param cookie      cookie
     * @return 基础信息
     */
    private List<BaseInfo> popBaseInfo(Integer pageIndex, Integer recordCount, String cookie) {
        HttpRequest request = HttpUtil.createGet(BASE_URL);
        request.form("handler", "Control");
        request.form("Key", "%7B%22uState%22%3A%22true%22%2C%22uLimit%22%3A%22true%22%2C%22nAddCode%22%3A%22410105000000%22%2C%22nKeyType%22%3A%220%22%2C%22Key%22%3A%22%22%7D");
        request.form("Type", "Q");
        request.form("PageIndex", pageIndex);
        request.form("RecordCount", recordCount);

        request.header("Accept", "application/json, text/javascript, */*; q=0.01");
        request.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        request.header("Cookie", cookie);

        try (HttpResponse execute = request.execute()) {
            String body = execute.body();
            log.info("远程拉取基础信息 结果：{}", body);
            JsonObject resultJson = GsonUtils.parse(body);
            if (resultJson != null && "000".equals(resultJson.get("code").getAsString())) {
                return GsonUtils.gson().fromJson(resultJson.get("dataset"), new TypeToken<List<BaseInfo>>() {
                }.getType());
            }
            return ListUtil.empty();
        }
    }


    /**
     * 远程拉取地址字典
     *
     * @param msg    值
     * @param root   是否根节点
     * @param cookie cookie
     * @return 地址字典
     */
    private List<AddressDict> popAddressDict(Long msg, Boolean root, String cookie) {
        Map<String, Object> keyMap = new HashMap<>(3);
        if (root != null && root) {
            keyMap.put("root", "000000000000");
            keyMap.put("type", "INIT");
        } else {
            keyMap.put("type", "SUB");
        }
        keyMap.put("msg", msg);

        HttpRequest request = HttpUtil.createGet(ADDRESS_DICT_URL).form("Key", JsonUtils.toJson(keyMap));
        request.header("Accept", "application/json, text/javascript, */*; q=0.01");
        request.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        request.header("Cookie", cookie);

        try (HttpResponse execute = request.execute()) {
            String body = execute.body();
            if (StrUtil.isNotBlank(body)) {
                body = body.substring(1, body.length() - 1);
            }
            body = UnicodeUtil.toString(body);
            log.info("远程拉取地址字典 结果：{}", body);
            List<AddressDict> result;
            if (root != null && root) {
                result = GsonUtils.gson().fromJson(body, new TypeToken<List<AddressDict>>() {
                }.getType());
            } else {
                JsonObject resultJson = GsonUtils.parse(body);
                result = GsonUtils.gson().fromJson(resultJson.get("nodes"), new TypeToken<List<AddressDict>>() {
                }.getType());
            }
            for (AddressDict addressDict : result) {
                addressDict.setText(addressDict.getText().replaceAll("[^\u4e00-\u9fa5]", ""));
            }
            return result;
        }

    }

    /**
     * 是否为空地址
     *
     * @param address 地址
     * @return 结果
     */
    private boolean isJinShuiNull(String address) {
        if (StringUtils.hasText(address)) {
            address = address.replaceAll("河南省", "");
            address = address.replaceAll("郑州市", "");
            address = address.replaceAll("金水区", "");
            address = address.replaceAll("河南", "");
            address = address.replaceAll("郑州", "");
            address = address.replaceAll("金水", "");
            address = address.replaceAll("市辖区", "");
            address = address.replaceAll("不详", "");
            address = address.replaceAll("镇", "");
            address = address.replaceAll("乡", "");
            address = address.replaceAll("郑", "");
            address = address.replaceAll("河", "");
            address = address.replaceAll("金", "");
        }
        return StrUtil.isEmpty(address);
    }


    /**
     * 所属街道
     *
     * @param address 地址
     * @return 地址
     */
    private AddressDict belongAddressDict(String address) {
        if (addressDictMap == null) {
            List<AddressDict> addressDictList = addressDictRepository.findByLevel(3);
            addressDictMap = addressDictList.stream().collect(Collectors.groupingBy(AddressDict::getText));
        }
        for (String key : addressDictMap.keySet()) {
            if (address.contains(key)) {
                List<AddressDict> addressDictList = addressDictMap.get(key);
                if (CollectionUtil.isNotEmpty(addressDictList)) {
                    return addressDictList.get(0);
                }
            }
        }
        return null;
    }

    /**
     * 所属街道
     *
     * @param address 地址
     * @return 地址
     */
    private AddressDict belongAddressDict2(String address) {
        if (addressDictMap == null) {
            List<AddressDict> addressDictList = addressDictRepository.findByLevel(3);
            addressDictMap = addressDictList.stream().collect(Collectors.groupingBy(AddressDict::getText));
        }
        for (String key : addressDictMap.keySet()) {
            if (key.contains(address)) {
                List<AddressDict> addressDictList = addressDictMap.get(key);
                if (CollectionUtil.isNotEmpty(addressDictList)) {
                    return addressDictList.get(0);
                }
            }
        }
        return null;
    }

    /**
     * 字符串只保留数字、字母、中文
     */
    private static String removeMatch(String str) {
        if (!StringUtils.hasText(str)) {
            return str;
        }
        return str.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5]", "");
    }
}

