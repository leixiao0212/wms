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
 * @since 2019-09-10
 */
@Data
public class OutorderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 出库单号
     */
    private String outorderno;

    /**
     * 仓库id
     */
    private Long warehouseid;

    /**
     * 种类
     */
    private Integer category;

    /**
     * 商品总数量
     */
    private Integer qty;

    /**
     * 状态（1：初始，2：待审核，3：已审核）
     */
    private String state;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 收件人
     */
    private String receiver;

    /**
     * 所在省
     */
    private String province;

    /**
     * 所在市
     */
    private String city;

    /**
     * 所在区/县
     */
    private String county;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 出库时间
     */
    private Date depottime;

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

    /**
     * 审核人id
     */
    private Long auditid;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核时间
     */
    private Date audittime;

    @Override
    public String checkParam() {
        return null;
    }

}
