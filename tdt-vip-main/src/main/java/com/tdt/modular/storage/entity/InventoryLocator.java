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
@TableName("s_inventory_locator")
public class InventoryLocator implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上级ID
     */
    @TableField("pid")
    private Long pid;

    /**
     * 盘点单号
     */
    @TableField("inventoryno")
    private String inventoryno;

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

    public String getInventoryno() {
        return inventoryno;
    }

    public void setInventoryno(String inventoryno) {
        this.inventoryno = inventoryno;
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

    @Override
    public String toString() {
        return "InventoryLocator{" +
        "id=" + id +
        ", pid=" + pid +
        ", inventoryno=" + inventoryno +
        ", locatorid=" + locatorid +
        ", locatorcode=" + locatorcode +
        ", locatorname=" + locatorname +
        ", createid=" + createid +
        ", creator=" + creator +
        ", createtime=" + createtime +
        "}";
    }
}
