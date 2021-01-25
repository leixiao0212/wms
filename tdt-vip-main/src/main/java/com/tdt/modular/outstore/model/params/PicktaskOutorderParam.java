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
 * @since 2019-09-12
 */
@Data
public class PicktaskOutorderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 主表id
     */
    private Long pid;

    /**
     * 出库订单表id
     */
    private Long outorderid;

    /**
     * 出库订单编号
     */
    private String outorderno;

    @Override
    public String checkParam() {
        return null;
    }

}
