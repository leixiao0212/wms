package com.tdt.modular.base.model.params;

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
 * @since 2019-08-16
 */
@Data
public class CommodityParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 条码
     */
    private String bar;

    /**
     * 别名
     */
    private String alias;

    /**
     * 名称
     */
    private String name;

    /**
     * 长
     */
    private Double length;

    /**
     * 宽
     */
    private Double width;

    /**
     * 高
     */
    private Double height;

    /**
     * 价值
     */
    private Double worth;

    /**
     * 净重
     */
    private Double nweight;

    /**
     * 毛重
     */
    private Double gweight;

    /**
     * 成分
     */
    private String component;

    /**
     * 品类
     */
    private String category;

    /**
     * 所属仓库id
     */
    private Long warehouseid;

    /**
     * 所属仓库名称
     */
    private String warehousename;

    /**
     * 是否唯一（0否 1是）
     */
    private String isunique;

    /**
     * 有效天数
     */
    private Integer validday;

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
    private Date creatime;

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
