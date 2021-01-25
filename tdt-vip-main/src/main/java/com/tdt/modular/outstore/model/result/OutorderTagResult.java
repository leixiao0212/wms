package com.tdt.modular.outstore.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-10-11
 */
@Data
public class OutorderTagResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 主表id
     */
    private Long pid;

    /**
     * 出库订单编号
     */
    private String outorderno;

    /**
     * 当前仓库id
     */
    private Long warehouseid;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String county;

    /**
     * 出库订单类型（1：单品单件，2：单品多件，3：多品单件，4：多品多件）
     */
    private String type;

}
