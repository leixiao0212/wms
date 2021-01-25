package com.tdt.modular.repertory.model.params;

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
public class StockParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 数据来源单号
     */
    private String billno;

    /**
     * 货位id
     */
    private Long locatorid;

    /**
     * 货位
     */
    private String locatorname;

    /**
     * 货位编码
     */
    private String locatorcode;

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
     * 总数量
     */
    private Integer cqty;

    /**
     * 可用数量
     */
    private Integer uqty;

    /**
     * 锁定数量
     */
    private Integer lqty;

    /**
     * 库位状态 0 正常  1锁定
     */
    private String locatorstate;

    /**
     * 仓库id
     */
    private Long warehouseid;

    /**
     * 备注
     */
    private String remarks;

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

    @Override
    public String checkParam() {
        return null;
    }

}
