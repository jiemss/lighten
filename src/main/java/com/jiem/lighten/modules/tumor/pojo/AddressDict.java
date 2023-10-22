package com.jiem.lighten.modules.tumor.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 地址字典
 *
 * @author: haojunjie
 * @date: 2023-10-22 14:16
 */
@Data
@Entity
@Table(name = "tumor_address_dict")
public class AddressDict {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 显示文本
     */
    private String text;

    /**
     * 编码
     */
    private Long value;

    /**
     * 上级编码
     */
    @SerializedName("Upid")
    private Long upId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 名称
     */
    @SerializedName("alltext")
    private String allText;

    /**
     * 下级
     */
    @Transient
    @Column(updatable = false, insertable = false)
    private List<AddressDict> nodes;
}
