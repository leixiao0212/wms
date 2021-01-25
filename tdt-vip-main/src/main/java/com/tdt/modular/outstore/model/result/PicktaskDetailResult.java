package com.tdt.modular.outstore.model.result;

import lombok.Data;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-09-12
 */
@Data
public class PicktaskDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 主表id
     */
    private Long pid;

    /**
     * 库存id
     */
    private Long stockid;

    /**
     * 库存日志id
     */
    private Long stocklogid;

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
     * 数量
     */
    private Integer qty;

}
