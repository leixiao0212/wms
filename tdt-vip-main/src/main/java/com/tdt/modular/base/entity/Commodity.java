package com.tdt.modular.base.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-08-16
 */
@TableName("b_commodity")
@Data
public class Commodity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Excel(name = "编号")
    private Long id;

    /**
     * 条码
     */
    @TableField("bar")
    @Excel(name = "条码")
    private String bar;

    /**
     * 别名
     */
    @TableField("alias")
    @Excel(name = "别名")
    private String alias;

    /**
     * 名称
     */
    @TableField("name")
    @Excel(name = "商品名")
    private String name;

    /**
     * 长
     */
    @TableField("length")
    @Excel(name = "长")
    private Double length;

    /**
     * 宽
     */
    @TableField("width")
    @Excel(name = "宽")
    private Double width;

    /**
     * 高
     */
    @TableField("height")
    @Excel(name = "高")
    private Double height;

    /**
     * 价值
     */
    @TableField("worth")
    @Excel(name = "价值")
    private Double worth;

    /**
     * 净重
     */
    @TableField("nweight")
    @Excel(name = "净重")
    private Double nweight;

    /**
     * 毛重
     */
    @TableField("gweight")
    @Excel(name = "毛重")
    private Double gweight;

    /**
     * 成分
     */
    @TableField("component")
    @Excel(name = "成分")
    private String component;

    /**
     * 品类
     */
    @TableField("category")
    @Excel(name = "品类")
    private String category;

    /**
     * 所属仓库id
     */
    @TableField("warehouseid")
    private Long warehouseid;

    /**
     * 所属仓库名称
     */
    @TableField("warehousename")
    private String warehousename;

    /**
     * 是否唯一（0否 1是）
     */
    @TableField("isunique")
    private String isunique;

    /**
     * 有效天数
     */
    @TableField("validday")
    @Excel(name = "有效天数")
    private Integer validday;

    /**
     * 备注
     */
    @TableField("remarks")
    @Excel(name = "备注")
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
    @TableField("creatime")
    private Date creatime;

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

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWorth() {
        return worth;
    }

    public void setWorth(Double worth) {
        this.worth = worth;
    }

    public Double getNweight() {
        return nweight;
    }

    public void setNweight(Double nweight) {
        this.nweight = nweight;
    }

    public Double getGweight() {
        return gweight;
    }

    public void setGweight(Double gweight) {
        this.gweight = gweight;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(Long warehouseid) {
        this.warehouseid = warehouseid;
    }

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    public String getIsunique() {
        return isunique;
    }

    public void setIsunique(String isunique) {
        this.isunique = isunique;
    }

    public Integer getValidday() {
        return validday;
    }

    public void setValidday(Integer validday) {
        this.validday = validday;
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

    public Date getCreatime() {
        return creatime;
    }

    public void setCreatime(Date creatime) {
        this.creatime = creatime;
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
        return "Commodity{" +
        "id=" + id +
        ", bar=" + bar +
        ", alias=" + alias +
        ", name=" + name +
        ", length=" + length +
        ", width=" + width +
        ", height=" + height +
        ", worth=" + worth +
        ", nweight=" + nweight +
        ", gweight=" + gweight +
        ", component=" + component +
        ", category=" + category +
        ", warehouseid=" + warehouseid +
        ", warehousename=" + warehousename +
        ", isunique=" + isunique +
        ", validday=" + validday +
        ", remarks=" + remarks +
        ", createid=" + createid +
        ", creator=" + creator +
        ", creatime=" + creatime +
        ", updateid=" + updateid +
        ", updator=" + updator +
        ", updatetime=" + updatetime +
        "}";
    }
}
