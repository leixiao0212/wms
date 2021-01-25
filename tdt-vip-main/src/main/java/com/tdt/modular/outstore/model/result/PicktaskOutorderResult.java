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
public class PicktaskOutorderResult implements Serializable {

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

}
