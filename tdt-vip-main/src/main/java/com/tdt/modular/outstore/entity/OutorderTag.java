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
 * @since 2019-10-11
 */
@TableName("o_outorder_tag")
public class OutorderTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 主表id
     */
    @TableField("pid")
    private Long pid;

    /**
     * 出库订单编号
     */
    @TableField("outorderno")
    private String outorderno;

    /**
     * 当前仓库id
     */
    @TableField("warehouseid")
    private Long warehouseid;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 区
     */
    @TableField("county")
    private String county;

    /**
     * 出库订单类型（1：单品单件，2：单品多件，3：多品单件，4：多品多件）
     */
    @TableField("type")
    private String type;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OutorderTag{" +
        "id=" + id +
        ", pid=" + pid +
        ", outorderno=" + outorderno +
        ", warehouseid=" + warehouseid +
        ", province=" + province +
        ", city=" + city +
        ", county=" + county +
        ", type=" + type +
        "}";
    }
}
