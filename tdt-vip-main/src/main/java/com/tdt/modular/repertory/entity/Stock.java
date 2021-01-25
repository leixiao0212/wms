package com.tdt.modular.repertory.entity;

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
@TableName("b_stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据来源单号
     */
    @TableField("billno")
    private String billno;

    /**
     * 货位id
     */
    @TableField("locatorid")
    private Long locatorid;

    /**
     * 货位
     */
    @TableField("locatorname")
    private String locatorname;

    /**
     * 货位编码
     */
    @TableField("locatorcode")
    private String locatorcode;

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
     * 总数量
     */
    @TableField("cqty")
    private Integer cqty;

    /**
     * 可用数量
     */
    @TableField("uqty")
    private Integer uqty;

    /**
     * 锁定数量
     */
    @TableField("lqty")
    private Integer lqty;

    /**
     * 库位状态 0 正常  1失效，2锁定
     */
    @TableField("locatorstate")
    private String locatorstate;

    /**
     * 仓库id
     */
    @TableField("warehouseid")
    private Long warehouseid;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public Long getLocatorid() {
        return locatorid;
    }

    public void setLocatorid(Long locatorid) {
        this.locatorid = locatorid;
    }

    public String getLocatorname() {
        return locatorname;
    }

    public void setLocatorname(String locatorname) {
        this.locatorname = locatorname;
    }

    public String getLocatorcode() {
        return locatorcode;
    }

    public void setLocatorcode(String locatorcode) {
        this.locatorcode = locatorcode;
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

    public Integer getCqty() {
        return cqty;
    }

    public void setCqty(Integer cqty) {
        this.cqty = cqty;
    }

    public Integer getUqty() {
        return uqty;
    }

    public void setUqty(Integer uqty) {
        this.uqty = uqty;
    }

    public Integer getLqty() {
        return lqty;
    }

    public void setLqty(Integer lqty) {
        this.lqty = lqty;
    }

    public String getLocatorstate() {
        return locatorstate;
    }

    public void setLocatorstate(String locatorstate) {
        this.locatorstate = locatorstate;
    }

    public Long getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(Long warehouseid) {
        this.warehouseid = warehouseid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    @Override
    public String toString() {
        return "Stock{" +
        "id=" + id +
        ", billno=" + billno +
        ", locatorid=" + locatorid +
        ", locatorname=" + locatorname +
        ", locatorcode=" + locatorcode +
        ", commodityid=" + commodityid +
        ", commoditybar=" + commoditybar +
        ", commodityname=" + commodityname +
        ", cqty=" + cqty +
        ", uqty=" + uqty +
        ", lqty=" + lqty +
        ", locatorstate=" + locatorstate +
        ", warehouseid=" + warehouseid +
        ", remarks=" + remarks +
        ", createid=" + createid +
        ", creator=" + creator +
        ", createtime=" + createtime +
        ", updateid=" + updateid +
        ", updator=" + updator +
        ", updatetime=" + updatetime +
        "}";
    }
}
