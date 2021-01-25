package com.tdt.modular.instorage.model.params;

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
 * @since 2019-08-21
 */
@Data
public class PurchaseParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 编号
     */
    private Long id;

    /**
     * 采购订单编号
     */
    private String purchaseno;

    /**
     * 供应商id
     */
    private Long supplierid;

    /**
     * 供应商名称
     */
    private String suppliername;

    /**
     * 商品总数量
     */
    private Integer commoditynum;

    /**
     * 预计到货时间
     */
    private Date estimatearrivaltime;

    /**
     * 仓库id
     */
    private Long warehouseid;

    /**
     * 仓库名称
     */
    private String warehousename;

    /**
     * 状态 0未审核 1已审核
     */
    private String state;

    /**
     * 备注
     */
    private String remark;

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
