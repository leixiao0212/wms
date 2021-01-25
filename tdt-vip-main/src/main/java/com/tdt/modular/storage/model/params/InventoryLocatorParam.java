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
public class InventoryLocatorParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 上级ID
     */
    private Long pid;

    /**
     * 盘点单号
     */
    private String inventoryno;

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
