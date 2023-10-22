package com.jiem.lighten.modules.tumor.repository;

import com.jiem.lighten.common.base.repository.CommonRepository;
import com.jiem.lighten.modules.tumor.pojo.AddressDict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 地址字典 Repository
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:26
 */
@Repository
public interface AddressDictRepository extends CommonRepository<AddressDict, Long> {

    /**
     * 通过值查询 地址字典
     *
     * @param value 值
     * @return 地址字典
     */
    AddressDict findFirstByValue(Long value);

    /**
     * 按级别查询
     *
     * @param level 级别
     * @return 字典
     */
    List<AddressDict> findByLevel(Integer level);

}

