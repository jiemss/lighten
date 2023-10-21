package com.jiem.lighten.common.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 通用Repository
 *
 * @param <E> 实体类
 * @param <T> id主键类型
 *            <br/>
 * @author: haojunjie
 * @date: 2023-10-21 15:11
 */
@NoRepositoryBean
public interface CommonRepository<E, T> extends JpaRepository<E, T>, JpaSpecificationExecutor<E> {

}
