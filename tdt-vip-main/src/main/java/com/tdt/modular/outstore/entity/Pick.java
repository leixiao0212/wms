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
 * @since 2019-09-17
 */
@TableName("o_pick")
public class Pick implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 拣货单号
     */
    @TableField("pickno")
    private String pickno;

    /**
     * 拣货任务id
     */
    @TableField("picktaskid")
    private Long picktaskid;

    /**
     * 拣货任务单号
     */
    @TableField("picktaskno")
    private String picktaskno;

    /**
     * 状态（0：初始，1：拣货中，2：已完成，3：异常待处理）
     */
    @TableField("state")
    private String state;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPickno() {
        return pickno;
    }

    public void setPickno(String pickno) {
        this.pickno = pickno;
    }

    public Long getPicktaskid() {
        return picktaskid;
    }

    public void setPicktaskid(Long picktaskid) {
        this.picktaskid = picktaskid;
    }

    public String getPicktaskno() {
        return picktaskno;
    }

    public void setPicktaskno(String picktaskno) {
        this.picktaskno = picktaskno;
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

    @Override
    public String toString() {
        return "Pick{" +
        "id=" + id +
        ", pickno=" + pickno +
        ", picktaskid=" + picktaskid +
        ", picktaskno=" + picktaskno +
        ", state=" + state +
        ", remarks=" + remarks +
        ", warehouseid=" + warehouseid +
        ", createid=" + createid +
        ", creator=" + creator +
        ", createtime=" + createtime +
        ", updateid=" + updateid +
        ", updator=" + updator +
        ", updatetime=" + updatetime +
        "}";
    }
}
