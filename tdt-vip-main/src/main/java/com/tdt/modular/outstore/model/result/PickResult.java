package com.tdt.modular.outstore.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-09-17
 */
@Data
public class PickResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 拣货单号
     */
    private String pickno;

    /**
     * 拣货任务id
     */
    private Long picktaskid;

    /**
     * 拣货任务单号
     */
    private String picktaskno;

    /**
     * 状态（0：初始，1：拣货中，2：已完成，3：异常待处理）
     */
    private String state;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 仓库id
     */
    private Long warehouseid;

    /**
     * 创建人id
     */
    private Long createid;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改人id
     */
    private Long updateid;

    /**
     * 修改人
     */
    private String updator;

    /**
     * 修改时间
     */
    private Date updatetime;

}
