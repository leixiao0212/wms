package com.tdt.modular.instorage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-08-21
 */
@TableName("i_receive")
public class Receive implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接货单号
     */
    @TableField("receiveno")
    private String receiveno;

    /**
     * 采购单号id
     */
    @TableField("purchaseid")
    private Long purchaseid;

    /**
     * 采购单号
     */
    @TableField("purchaseno")
    private String purchaseno;

    /**
     * 调拨单号id
     */
    @TableField("allocationid")
    private Long allocationid;

    /**
     * 调拨单号
     */
    @TableField("allocationno")
    private String allocationno;

    /**
     * 状态
     */
    @TableField("state")
    private String state;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 仓库id
     */
    @TableField("warehouseid")
    private Long warehouseid;

    /**
     * 创建人id
     */
    @TableField("createid")
    private Long createid;

    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;

    /**
     * 创建时间
     */
    @TableField("createtime")
    private Date createtime;

    /**
     * 修改人id
     */
    @TableField("updateid")
    private Long updateid;

    /**
     * 修改人
     */
    @TableField("updator")
    private String updator;

    /**
     * 修改时间
     */
    @TableField("updatetime")
    private Date updatetime;

    /**
     * 审核人id
     */
    @TableField("auditid")
    private Long auditid;

    /**
     * 审核人
     */
    @TableField("auditor")
    private String auditor;

    /**
     * 审核时间
     */
    @TableField("audittime")
    private Date audittime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiveno() {
        return receiveno;
    }

    public void setReceiveno(String receiveno) {
        this.receiveno = receiveno;
    }

    public Long getPurchaseid() {
        return purchaseid;
    }

    public void setPurchaseid(Long purchaseid) {
        this.purchaseid = purchaseid;
    }

    public String getPurchaseno() {
        return purchaseno;
    }

    public void setPurchaseno(String purchaseno) {
        this.purchaseno = purchaseno;
    }

    public Long getAllocationid() {
        return allocationid;
    }

    public void setAllocationid(Long allocationid) {
        this.allocationid = allocationid;
    }

    public String getAllocationno() {
        return allocationno;
    }

    public void setAllocationno(String allocationno) {
        this.allocationno = allocationno;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(Long warehouseid) {
        this.warehouseid = warehouseid;
    }

    public Long getCreateid() {
        return createid;
    }

    public void setCreateid(Long createid) {
        this.createid = createid;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getUpdateid() {
        return updateid;
    }

    public void setUpdateid(Long updateid) {
        this.updateid = updateid;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Long getAuditid() {
        return auditid;
    }

    public void setAuditid(Long Auditid) {
        this.auditid = Auditid;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String Auditor) {
        this.auditor = Auditor;
    }

    public Date getAudittime() {
        return audittime;
    }

    public void setAudittime(Date Audittime) {
        this.audittime = Audittime;
    }

    @Override
    public String toString() {
        return "Receive{" +
        "id=" + id +
        ", receiveno=" + receiveno +
        ", purchaseid=" + purchaseid +
        ", purchaseno=" + purchaseno +
        ", allocationid=" + allocationid +
        ", allocationno=" + allocationno +
        ", state=" + state +
        ", remarks=" + remarks +
        ", warehouseid=" + warehouseid +
        ", createid=" + createid +
        ", creator=" + creator +
        ", createtime=" + createtime +
        ", updateid=" + updateid +
        ", updator=" + updator +
        ", updatetime=" + updatetime +
        ", auditid=" + auditid +
        ", auditor=" + auditor +
        ", audittime=" + audittime +
        "}";
    }
}
