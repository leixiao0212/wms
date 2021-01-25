package com.tdt.modular.outstore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-09-26
 */
@TableName("o_picktask_lock_detail")
public class PicktaskLockDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 拣货任务id
     */
    @TableField("pid")
    private Long pid;

    /**
     * 拣货任务单号
     */
    @TableField("picktaskno")
    private String picktaskno;

    /**
     * 出库订单id
     */
    @TableField("outorderid")
    private Long outorderid;

    /**
     * 出库订单单号
     */
    @TableField("outorderno")
    private String outorderno;

    /**
     * 出库订单明细表id
     */
    @TableField("outorderdetailid")
    private Long outorderdetailid;

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
     * 数量
     */
    @TableField("qty")
    private Integer qty;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPicktaskno() {
        return picktaskno;
    }

    public void setPicktaskno(String picktaskno) {
        this.picktaskno = picktaskno;
    }

    public Long getOutorderid() {
        return outorderid;
    }

    public void setOutorderid(Long outorderid) {
        this.outorderid = outorderid;
    }

    public String getOutorderno() {
        return outorderno;
    }

    public void setOutorderno(String outorderno) {
        this.outorderno = outorderno;
    }

    public Long getOutorderdetailid() {
        return outorderdetailid;
    }

    public void setOutorderdetailid(Long outorderdetailid) {
        this.outorderdetailid = outorderdetailid;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "PicktaskLockDetail{" +
        "id=" + id +
        ", pid=" + pid +
        ", picktaskno=" + picktaskno +
        ", outorderid=" + outorderid +
        ", outorderno=" + outorderno +
        ", outorderdetailid=" + outorderdetailid +
        ", commodityid=" + commodityid +
        ", commoditybar=" + commoditybar +
        ", commodityname=" + commodityname +
        ", locatorid=" + locatorid +
        ", locatorcode=" + locatorcode +
        ", locatorname=" + locatorname +
        ", qty=" + qty +
        "}";
    }
}
