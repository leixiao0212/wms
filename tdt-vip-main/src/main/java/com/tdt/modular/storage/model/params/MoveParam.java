package com.tdt.modular.storage.model.params;

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
 * @since 2019-08-28
 */
@Data
public class MoveParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 编号
     */
    private Long id;

    /**
     * 移库单号
     */
    private String moveno;

    /**
     * 商品id
     */
    private Long commodityid;

    /**
     * 商品编码
     */
    private String commoditybar;

    /**
     * 商品名称
     */
    private String commodityname;

    /**
     * 货位id
     */
    private Long locatorid;

    /**
     * 货位编码
     */
    private String locatorcode;

    /**
     * 货位名称
     */
    private String locatorname;

    /**
     * 源货位id
     */
    private Long oldlocatorid;

    /**
     * 源货位编码
     */
    private String oldlocatorcode;

    /**
     * 源货位名称
     */
    private String oldlocatorname;

    /**
     * 数量
     */
    private Integer qty;

    /**
     * 状态 1待审核 2已审核
     */
    private String state;

    /**
     * 所属仓库id
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
