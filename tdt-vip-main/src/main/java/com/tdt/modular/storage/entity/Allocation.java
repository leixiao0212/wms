package com.tdt.modular.storage.entity;

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
 * @since 2019-08-28
 */
@TableName("s_allocation")
public class Allocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 调拨单号
     */
    @TableField("allocationno")
    private String allocationno;

    /**
     * 原仓库id
     */
    @TableField("bwarehouseid")
    private Long bwarehouseid;

    /**
     * 原仓库名称
     */
    @TableField("bwarehousename")
    private String bwarehousename;

    /**
     * 目标仓库id
     */
    @TableField("awarehouseid")
    private Long awarehouseid;

    /**
     * 目标仓库名称
     */
    @TableField("awarehousename")
    private String awarehousename;

    /**
     * 状态 0初始 1待审核 2已审核
     */
    @TableField("state")
    private String state;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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

    public String getAllocationno() {
        return allocationno;
    }

    public void setAllocationno(String allocationno) {
        this.allocationno = allocationno;
    }

    public Long getBwarehouseid() {
        return bwarehouseid;
    }

    public void setBwarehouseid(Long bwarehouseid) {
        this.bwarehouseid = bwarehouseid;
    }

    public String getBwarehousename() {
        return bwarehousename;
    }

    public void setBwarehousename(String bwarehousename) {
        this.bwarehousename = bwarehousename;
    }

    public Long getAwarehouseid() {
        return awarehouseid;
    }

    public void setAwarehouseid(Long awarehouseid) {
        this.awarehouseid = awarehouseid;
    }

    public String getAwarehousename() {
        return awarehousename;
    }

    public void setAwarehousename(String awarehousename) {
        this.awarehousename = awarehousename;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public void setAuditid(Long auditid) {
        this.auditid = auditid;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Date getAudittime() {
        return audittime;
    }

    public void setAudittime(Date audittime) {
        this.audittime = audittime;
    }

    @Override
    public String toString() {
        return "Allocation{" +
        "id=" + id +
        ", allocationno=" + allocationno +
        ", bwarehouseid=" + bwarehouseid +
        ", bwarehousename=" + bwarehousename +
        ", awarehouseid=" + awarehouseid +
        ", awarehousename=" + awarehousename +
        ", state=" + state +
        ", remark=" + remark +
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
