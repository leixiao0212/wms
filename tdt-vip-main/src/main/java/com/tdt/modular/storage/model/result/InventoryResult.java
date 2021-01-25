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
public class InventoryResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 盘点单号
     */
    private String inventoryno;

    /**
     * 状态 0:初始,1:盘点中,2:盘点结束
     */
    private String state;

    /**
     * 当前仓库id
     */
    private Long warehouseid;

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
