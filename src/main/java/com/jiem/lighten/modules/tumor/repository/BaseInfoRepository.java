package com.jiem.lighten.modules.tumor.repository;

import com.jiem.lighten.common.base.repository.CommonRepository;
import com.jiem.lighten.modules.tumor.pojo.BaseInfo;
import org.springframework.stereotype.Repository;

/**
 * 基础信息 Repository
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:26
 */
@Repository
public interface BaseInfoRepository extends CommonRepository<BaseInfo, Long> {

    /**
     * 通过编号查询 基础信息
     *
     * @param registr 编号
     * @return 基础信息
     */
    BaseInfo findFirstByRegistr(String registr);
}

