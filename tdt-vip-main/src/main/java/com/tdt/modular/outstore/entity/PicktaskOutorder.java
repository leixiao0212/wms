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
 * @since 2019-09-12
 */
@TableName("o_picktask_outorder")
public class PicktaskOutorder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 主表id
     */
    @TableField("pid")
    private Long pid;

    /**
     * 出库订单表id
     */
    @TableField("outorderid")
    private Long outorderid;

    /**
     * 出库订单编号
     */
    @TableField("outorderno")
    private String outorderno;


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

    @Override
    public String toString() {
        return "PicktaskOutorder{" +
        "id=" + id +
        ", pid=" + pid +
        ", outorderid=" + outorderid +
        ", outorderno=" + outorderno +
        "}";
    }
}
