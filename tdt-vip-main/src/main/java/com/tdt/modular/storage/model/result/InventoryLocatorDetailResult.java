package com.tdt.modular.storage.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-08-28
 */
@Data
public class InventoryLocatorDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 上级ID
     */
    private Long pid;

    /**
     * 货位id
     */
    private Long locatorid;

    /**
     * 货位编码
     */
    private String locatorcode;

    /**
     * 货位名称
     */
    private String locatorname;

    /**
     * 商品id
     */
    private Long commodityid;

    /**
     * 商品编码
     */
    private String commoditybar;

    /**
     * 商品名称
     */
    private String commodityname;

    /**
     * 运单数量
     */
    private Integer qty;

    /**
     * 创建人id
     */
    private Long createid;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createtime;

}
