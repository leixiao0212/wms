package com.tdt.modular.outstore.model.params;

import lombok.Data;
import cn.stylefeng.roses.kernel.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenc
 * @since 2019-09-12
 */
@Data
public class PicktaskParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 拣货任务单号
     */
    private String picktaskno;

    /**
     * 领取者id
     */
    private Long receiverid;

    /**
     * 领取者
     */
    private String receiver;

    /**
     * 领取时间
     */
    private Date receivetime;

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

    @Override
    public String checkParam() {
        return null;
    }

}
