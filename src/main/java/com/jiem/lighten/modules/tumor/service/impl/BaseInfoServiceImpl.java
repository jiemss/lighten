package com.jiem.lighten.modules.tumor.service.impl;

import com.jiem.lighten.common.base.service.CommonServiceImpl;
import com.jiem.lighten.modules.tumor.pojo.BaseInfo;
import com.jiem.lighten.modules.tumor.repository.BaseInfoRepository;
import com.jiem.lighten.modules.tumor.service.BaseInfoService;
import com.jiem.lighten.modules.tumor.vo.BaseInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 基础信息 ServiceImpl
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseInfoServiceImpl extends CommonServiceImpl<BaseInfoVo, BaseInfo, Long> implements BaseInfoService {


    @Resource
    private BaseInfoRepository baseInfoRepository;

}

