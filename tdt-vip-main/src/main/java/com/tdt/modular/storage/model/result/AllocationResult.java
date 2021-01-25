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
public class AllocationResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 编号
     */
    private Long id;

    /**
     * 调拨单号
     */
    private String allocationno;

    /**
     * 原仓库id
     */
    private Long bwarehouseid;

    /**
     * 原仓库名称
     */
    private String bwarehousename;

    /**
     * 目标仓库id
     */
    private Long awarehouseid;

    /**
     * 目标仓库名称
     */
    private String awarehousename;

    /**
     * 状态 0初始 1待审核 2已审核
     */
    private String state;

    /**
     * 备注
     */
    private String remark;

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
