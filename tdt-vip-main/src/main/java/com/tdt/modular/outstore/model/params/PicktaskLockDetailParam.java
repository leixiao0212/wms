package com.tdt.modular.outstore.model.params;

import lombok.Data;
import cn.stylefeng.roses.kernel.model.validator.BaseValidatingParam;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-09-26
 */
@Data
public class PicktaskLockDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 拣货任务id
     */
    private Long pid;

    /**
     * 拣货任务单号
     */
    private String picktaskno;

    /**
     * 出库订单id
     */
    private Long outorderid;

    /**
     * 出库订单单号
     */
    private String outorderno;

    /**
     * 出库订单明细表id
     */
    private Long outorderdetailid;

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

    @Override
    public String checkParam() {
        return null;
    }

}
