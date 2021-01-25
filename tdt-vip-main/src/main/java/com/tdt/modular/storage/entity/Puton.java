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
@TableName("s_puton")
public class Puton implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上架单号
     */
    @TableField("putonno")
    private String putonno;

    /**
     * 商品id
     */
    @TableField("commodityid")
    private Long commodityid;

    /**
     * 商品编码
     */
    @TableField("commoditybar")
    private String commoditybar;

    /**
     * 商品名称
     */
    @TableField("commodityname")
    private String commodityname;

    /**
     * 货位id
     */
    @TableField("locatorid")
    private Long locatorid;

    /**
     * 货位编码
     */
    @TableField("locatorcode")
    private String locatorcode;

    /**
     * 货位名称
     */
    @TableField("locatorname")
    private String locatorname;

    /**
     * 源货位id
     */
    @TableField("oldlocatorid")
    private Long oldlocatorid;

    /**
     * 源货位编码
     */
    @TableField("oldlocatorcode")
    private String oldlocatorcode;

    /**
     * 源货位名称
     */
    @TableField("oldlocatorname")
    private String oldlocatorname;

    /**
     * 数量
     */
    @TableField("qty")
    private Integer qty;

    /**
     * 状态 1待审核 2已审核
     */
    @TableField("state")
    private String state;

    /**
     * 当前仓库id
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

    public String getPutonno() {
        return putonno;
    }

    public void setPutonno(String putonno) {
        this.putonno = putonno;
    }

    public Long getCommodityid() {
        return commodityid;
    }

    public void setCommodityid(Long commodityid) {
        this.commodityid = commodityid;
    }

    public String getCommoditybar() {
        return commoditybar;
    }

    public void setCommoditybar(String commoditybar) {
        this.commoditybar = commoditybar;
    }

    public String getCommodityname() {
        return commodityname;
    }

    public void setCommodityname(String commodityname) {
        this.commodityname = commodityname;
    }

    public Long getLocatorid() {
        return locatorid;
    }

    public void setLocatorid(Long locatorid) {
        this.locatorid = locatorid;
    }

    public String getLocatorcode() {
        return locatorcode;
    }

    public void setLocatorcode(String locatorcode) {
        this.locatorcode = locatorcode;
    }

    public String getLocatorname() {
        return locatorname;
    }

    public void setLocatorname(String locatorname) {
        this.locatorname = locatorname;
    }

    public Long getOldlocatorid() {
        return oldlocatorid;
    }

    public void setOldlocatorid(Long oldlocatorid) {
        this.oldlocatorid = oldlocatorid;
    }

    public String getOldlocatorcode() {
        return oldlocatorcode;
    }

    public void setOldlocatorcode(String oldlocatorcode) {
        this.oldlocatorcode = oldlocatorcode;
    }

    public String getOldlocatorname() {
        return oldlocatorname;
    }

    public void setOldlocatorname(String oldlocatorname) {
        this.oldlocatorname = oldlocatorname;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
        return "Puton{" +
        "id=" + id +
        "putonno=" + putonno +
        ", commodityid=" + commodityid +
        ", commoditybar=" + commoditybar +
        ", commodityname=" + commodityname +
        ", locatorid=" + locatorid +
        ", locatorcode=" + locatorcode +
        ", locatorname=" + locatorname +
        ", oldlocatorid=" + oldlocatorid +
        ", oldlocatorcode=" + oldlocatorcode +
        ", oldlocatorname=" + oldlocatorname +
        ", qty=" + qty +
        ", state=" + state +
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
