package com.tdt.modular.outstore.entity;

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
 * @since 2019-09-10
 */
@TableName("o_outorder")
public class Outorder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 出库单号
     */
    @TableField("outorderno")
    private String outorderno;

    /**
     * 仓库id
     */
    @TableField("warehouseid")
    private Long warehouseid;

    /**
     * 种类
     */
    @TableField("category")
    private Integer category;

    /**
     * 商品总数量
     */
    @TableField("qty")
    private Integer qty;

    /**
     * 状态（1：初始，2：待审核，3：已审核）
     */
    @TableField("state")
    private String state;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 收件人
     */
    @TableField("receiver")
    private String receiver;

    /**
     * 所在省
     */
    @TableField("province")
    private String province;

    /**
     * 所在市
     */
    @TableField("city")
    private String city;

    /**
     * 所在区/县
     */
    @TableField("county")
    private String county;

    /**
     * 详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 出库时间
     */
    @TableField("depottime")
    private Date depottime;

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

    public String getOutorderno() {
        return outorderno;
    }

    public void setOutorderno(String outorderno) {
        this.outorderno = outorderno;
    }

    public Long getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(Long warehouseid) {
        this.warehouseid = warehouseid;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDepottime() {
        return depottime;
    }

    public void setDepottime(Date depottime) {
        this.depottime = depottime;
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
        return "Outorder{" +
        "id=" + id +
        ", outorderno=" + outorderno +
        ", warehouseid=" + warehouseid +
        ", category=" + category +
        ", qty=" + qty +
        ", state=" + state +
        ", remarks=" + remarks +
        ", receiver=" + receiver +
        ", province=" + province +
        ", city=" + city +
        ", county=" + county +
        ", address=" + address +
        ", depottime=" + depottime +
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
