package com.jiem.lighten.modules.tumor.service;


import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.jiem.lighten.common.base.service.CommonService;
import com.jiem.lighten.modules.tumor.pojo.BaseInfo;
import com.jiem.lighten.modules.tumor.vo.BaseInfoVo;

import java.util.function.BiConsumer;

/**
 * 基础信息 Service
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:51
 */
public interface BaseInfoService extends CommonService<BaseInfoVo, BaseInfo, Long> {

    /**
     * 拉取并保存基础数据
     *
     * @param cookie cookie
     */
    void popAndSaveBaseInfo(String cookie);

    /**
     * 远程拉取地址字典
     *
     * @param cookie cookie
     */
    void popAndSaveAddressDict(String cookie);

    BiConsumer<ExcelWriter, WriteSheet> excel();
}

