package com.jiem.lighten.modules.tumor.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jiem.lighten.common.util.GsonUtils;
import com.jiem.lighten.common.util.JsonUtils;
import com.jiem.lighten.modules.tumor.entity.AutoCekCan;
import com.jiem.lighten.modules.tumor.entity.AutoCekCanItem;
import com.jiem.lighten.modules.tumor.entity.QAutoCekCan;
import com.jiem.lighten.modules.tumor.entity.QAutoCekCanItem;
import com.jiem.lighten.modules.tumor.excel.AutoCekCanExcel;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 肿瘤登记报告查重
 *
 * @author: haojunjie
 * @date: 2023-11-04 10:18
 */
@Slf4j
@Service
public class AutoCekCanService {

    private static final String CONTEXT_URL = "http://218.28.13.204:35556/";
    private static final String BASE_URL = CONTEXT_URL + "/Program/Heavy/AutoCekCanReg";

    @Resource
    private JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * 拉取并保存
     *
     * @param cookie cookie
     */
    @Transactional(rollbackFor = Exception.class)
    public void popAndSave(String cookie) {
        int pageIndex = 0;
        int recordCount = 100;
        QAutoCekCan qAutoCekCan = QAutoCekCan.autoCekCan;
        while (true) {
            log.info("拉取远程数据 pageIndex={} recordCount={}", pageIndex, recordCount);
            List<AutoCekCan> autoCekCans = popAutoCekCan(pageIndex, recordCount, cookie);
            if (CollectionUtil.isEmpty(autoCekCans)) {
                break;
            }
            pageIndex++;
            for (AutoCekCan autoCekCan : autoCekCans) {
                AutoCekCan autoCekCanDb = queryFactory.selectFrom(qAutoCekCan).where(qAutoCekCan.registr.eq(autoCekCan.getRegistr())).limit(1).fetchFirst();
                if (autoCekCanDb != null) {
                    continue;
                }
                autoCekCan.setId(null);
                entityManager.persist(autoCekCan);
                List<AutoCekCanItem> dataset = autoCekCan.getDataset();
                for (AutoCekCanItem autoCekCanItem : dataset) {
                    autoCekCanItem.setId(null);
                    autoCekCanItem.setCekCanId(autoCekCan.getId());
                    entityManager.persist(autoCekCanItem);
                }
            }
        }
        log.info("拉取远程数据 结束 ... ");
    }

    /**
     * 导出数据
     *
     * @param type 类
     * @return 函数
     */
    public BiConsumer<ExcelWriter, WriteSheet> excel(Integer type) {
        final QAutoCekCan qAutoCekCan = QAutoCekCan.autoCekCan;
        final QAutoCekCanItem qAutoCekCanItem = QAutoCekCanItem.autoCekCanItem;
        return (excelWriter, writeSheet) -> {
            int pageIndex = 0;
            int recordCount = 1000;
            while (true) {
                JPAQuery<AutoCekCan> autoCekCanJpaQuery = queryFactory.selectFrom(qAutoCekCan);
                // 全量
                if (type == 0) {
                    autoCekCanJpaQuery.where(qAutoCekCan.id.eq(-1L));
                }
                autoCekCanJpaQuery.orderBy(qAutoCekCan.id.desc());
                List<AutoCekCan> autoCekCanList = autoCekCanJpaQuery.offset(pageIndex * recordCount).limit(recordCount).fetch();
                if (CollectionUtil.isEmpty(autoCekCanList)) {
                    break;
                }
                pageIndex++;

                List<AutoCekCanExcel> excelList = new LinkedList<>();
                for (AutoCekCan autoCekCan : autoCekCanList) {
                    List<AutoCekCanItem> autoCekCanItems = queryFactory.selectFrom(qAutoCekCanItem).where(qAutoCekCanItem.cekCanId.eq(autoCekCan.getId())).fetch();
                    if (type == 2 && !autoCekCanItemsCompare(autoCekCanItems)) {
                        continue;
                    } else if (type == 3 && autoCekCanItemsCompare(autoCekCanItems)) {
                        continue;
                    }

                    if (CollectionUtil.isNotEmpty(autoCekCanItems)) {
                        for (AutoCekCanItem autoCekCanItem : autoCekCanItems) {
                            AutoCekCanExcel autoCekCanExcel = new AutoCekCanExcel();
                            BeanUtil.copyProperties(autoCekCan, autoCekCanExcel);
                            BeanUtil.copyProperties(autoCekCanItem, autoCekCanExcel);
                            excelList.add(autoCekCanExcel);
                        }
                    } else {
                        AutoCekCanExcel autoCekCanExcel = new AutoCekCanExcel();
                        BeanUtil.copyProperties(autoCekCan, autoCekCanExcel);
                        excelList.add(autoCekCanExcel);
                    }
                }
                if (CollectionUtil.isNotEmpty(excelList)) {
                    excelWriter.write(excelList, writeSheet);
                }
            }
        };
    }

    private boolean autoCekCanItemsCompare(List<AutoCekCanItem> autoCekCanItems) {
        if (CollectionUtil.isEmpty(autoCekCanItems)) {
            return false;
        }
        AutoCekCanItem firstAutoCekCanItem = autoCekCanItems.get(0);
        for (AutoCekCanItem autoCekCanItem : autoCekCanItems) {
            if (!autoCekCanItem.getMorp().equals(firstAutoCekCanItem.getMorp())) {
                return false;
            }
            if (!autoCekCanItem.getBeha().equals(firstAutoCekCanItem.getBeha())) {
                return false;
            }
            if (!autoCekCanItem.getTopo().equals(firstAutoCekCanItem.getTopo())) {
                return false;
            }
        }
        return true;
    }


    /**
     * 远程拉取基础信息
     *
     * @param pageIndex   第几页
     * @param recordCount 每页大小
     * @param cookie      cookie
     * @return 基础信息
     */
    private List<AutoCekCan> popAutoCekCan(Integer pageIndex, Integer recordCount, String cookie) {
        // {"nAddCode":"410105000000","nKeyType":"0","Key":""}
        Map<String, Object> keyMap = new HashMap<>(3);
        keyMap.put("Key", "");
        keyMap.put("nKeyType", "0");
        keyMap.put("nAddCode", 410105000000L);

        HttpRequest request = HttpUtil.createGet(BASE_URL).form("Key", JsonUtils.toJson(keyMap));
        request.form("handler", "Control");
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
                return GsonUtils.gson().fromJson(resultJson.get("dataset"), new TypeToken<List<AutoCekCan>>() {
                }.getType());
            }
            return ListUtil.empty();
        }
    }


}
