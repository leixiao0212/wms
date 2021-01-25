package com.tdt.modular.instorage.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-08-21
 */
@Data
public class ReceiveResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 接货单号
     */
    private String receiveno;

    /**
     * 采购单号id
     */
    private Long purchaseid;

    /**
     * 采购单号
     */
    private String purchaseno;

    /**
     * 调拨单号id
     */
    private Long allocationid;

    /**
     * 调拨单号
     */
    private String allocationno;

    /**
     * 状态
     */
    private String state;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 仓库id
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

    /**
     * 修改人id
     */
    private Long updateid;

    /**
     * 修改人
     */
    private String updator;

    /**
     * 修改时间
     */
    private Date updatetime;

    /**
     * 审核人id
     */
    private Long auditid;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核时间
     */
    private Date audittime;

}
