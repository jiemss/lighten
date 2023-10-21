package com.jiem.lighten.modules.tumor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jiem.lighten.common.base.service.CommonServiceImpl;
import com.jiem.lighten.common.util.ExcelUtils;
import com.jiem.lighten.common.util.GsonUtils;
import com.jiem.lighten.modules.tumor.pojo.BaseInfo;
import com.jiem.lighten.modules.tumor.repository.BaseInfoRepository;
import com.jiem.lighten.modules.tumor.service.BaseInfoService;
import com.jiem.lighten.modules.tumor.vo.BaseInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

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

    private static final String BASE_URL = "http://218.28.13.204:35556/Program/BaseInfo/MsgBaseInfo";
    private static final String COOKIE = ".AspNetCore.Antiforgery.3UQ283wQwb4=CfDJ8AMr_7fNIK1Bpq1z65p4ZsQAS-nvbY1TNBT3dZxVZ6hjWBG5pIwezj94IZkoStAsCqz_aiP7H3Ja8HSNzWk7pNHwm3rRm7H6BmDPGnXRVA4v51BTCM7qNVg6nln5oReKmBtsMj58TtpGv0ff-NslJl4; .AspNetCore.Session=CfDJ8AMr%2F7fNIK1Bpq1z65p4ZsSlIqzSr5yKe7ifr3Bv1NTLGcqCmmYZYuw43Kz8rjRRHcMneKKd3OqeSh79L7H5G9wixkMIe042L%2FuflpWfUqfLjZR0LOElMl%2Fi7JAqQe2ufUG5QFGiMy6jAd4Mh5JuvVhia52brOpymBLDpZiZrO9q";

    @Resource
    private BaseInfoRepository baseInfoRepository;

    @Override
    public void popAndSaveBaseInfo(String cookie) {
        int pageIndex = 1;
        int recordCount = 100;
        while (true) {
            log.info("拉取远程数据 pageIndex={} recordCount={}", pageIndex, recordCount);
            List<BaseInfo> baseInfos = popBaseInfo(pageIndex, recordCount, cookie);
            if (CollectionUtil.isEmpty(baseInfos)) {
                break;
            }
            pageIndex++;
            baseInfoRepository.saveAll(baseInfos);
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
     * 远程拉取
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
            log.info("根据地址搜索坐标 结果：{}", body);
            JsonObject resultJson = GsonUtils.parse(body);
            if (resultJson != null && "000".equals(resultJson.get("code").getAsString())) {
                return GsonUtils.gson().fromJson(resultJson.get("dataset"), new TypeToken<List<BaseInfo>>() {
                }.getType());
            }
        }
        return ListUtil.empty();
    }

    public static void main(String[] args) {
        System.out.println(removeMatch("swerwe1213撒1啊a啊122----12===啊"));
    }


    /**
     * 字符串只保留数字、字母、中文
     */
    public static String removeMatch(String str) {
        if (!StringUtils.hasText(str)) {
            return str;
        }
        return str.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5]", "");
    }
}

