package com.tdt.core.common.constant;

/**
 * Created by liu.min on 2018/5/8.
 * 业务常量
 */
public interface Constants {

    final class ImgageMateType {

        public static final String JPG = "image/jpeg";

        public static final String PNG = "image/png";

        public static final String GIF = "image/gif";

        public static final String MP4 = "video/mp4";

    }

    /**
     * 支持的图片类型
     */
    String[] ALLOW_PIC_TYPES = new String[] { ImgageMateType.JPG, ImgageMateType.PNG,
            ImgageMateType.GIF, ImgageMateType.MP4 };

    interface PREFIX_NO {
        String PURCHASE_NO = "IP";
        String WAREHOUSING_NO= "IW";
        String INVENTORY_NO = "IN";
        String ALLOCATION_NO = "AL";
        String OUTORDER_NO = "OT";
        String PICKTASK_NO = "PI";
        String PICK_NO = "PN";
    }

    //库位状态
    interface LocatorState {
        //正常
        String NORMAL = "0";
        //失效
        String INVALID = "9";
        //锁定
        String LOCK = "2";
    }
    //库位类型
    interface LocatorType {
        //普通货位
        String COMMON = "1";
        //入库区
        String INSTORAGE = "2";
        //大货区
        String BIG = "3";
        //散货区
        String SCATTER = "4";
        //残次品
        String IMPERFECTIONS = "5";
        //退货区
        String RETURN = "6";
        //出库区
        String OUTSTORAGE = "7";
    }
    //采购订单状态
    interface PurchaseState {
        //未审核
        String UNFINISH = "0";
        //待审核
        String UNREVIEW = "1";
        //已审核
        String REVIEWED = "2";
    }
    // 接货状态
    interface ReceiveState {
        //初始状态
        String NEW = "0";
        //接货中
        String receiving = "1";
        //已审核
        String auditoed = "2";
    }
    // 入库状态 0入库中 1入库完成待审核 2已审核
    interface WarehousingState {
        //入库中
        String UNFINISH = "0";
        //待审核
        String UNREVIEW = "1";
        //已审核
        String REVIEWED = "2";
    }
    //其他入库状态
    interface OtherinState {
        //未审核
        String UNREVIEW = "0";
        //已审核
        String REVIEWED = "1";
    }
    //库存状态
    interface StockState {
        //正常
        String NORMAL = "0";
        //锁定
        String LOCK = "1";
    }

    //移库操作类型
    interface MoveType {
        int MOVEADD = 1;
        int MOVEDEL = 2;
    }

    //盘点状态
    interface InventoryState {
        //初始
        String INIT = "0";
        //盘点中
        String START = "1";
        //盘点结束
        String END = "2";
    }

    //上架状态
    interface PutonState {
        //待审核
        String UNREVIEW = "1";
        //已审核
        String REVIEW = "2";
    }

    //移库状态
    interface MoveState {
        //待审核
        String UNREVIEW = "1";
        //已审核
        String REVIEW = "2";
    }

    //调拨状态
    interface AllocationState {
        //初始
        String INIT = "0";
        //待审核
        String UNREVIEW = "1";
        //已审核
        String REVIEW = "2";
    }

    //库存日志状态
    interface StockLogState {
        //正常
        String NORMAL = "0";
        //失效
        String INVALID = "1";
    }

    //库存日志类型
    interface StockLogType {
        //入
        String INSTOCK = "1";
        //上架
        String PUTONSTOCK = "2";
        //锁定
        String LOCKSTOCK = "3";
        //下架
        String PUTOUTSTOCK = "4";
        //出
        String OUTSTOCK = "5";
    }

    //出库订单状态
    interface OutorderState {
        //初始
        String INIT = "0";
        //待审核
        String UNREVIEW = "1";
        //已审核
        String REVIEW = "2";
        //已分配（生成拣货任务）
        String DISTRIBUTED = "3";
        //拣货中（拣货任务被领取）
        String PICKING = "4";
        //已出库（出库复核完成）
        String OUTSTORED = "5";
        //已失效（出库订单已生成拣货任务并被领取，点击取消订单）
        String INVALID = "9";
    }

    //出库订单标签类型
    interface OutorderTagType {
        //单品单件
        String ONEANDONE = "1";
        //单品多件
        String ONEANDANY = "2";
        //多品单件
        String ANYANDONE = "3";
        //多品多件
        String ANYANDANY = "4";
    }

    //拣货状态
    interface PickState {
        //初始
        String INIT = "0";
        //拣货中
        String PICKING = "1";
        //已完成
        String PICKED = "2";
        //异常待处理
        String EXCEPTIONTODO = "3";
    }

    //出库复核状态
    interface ReviewState {
        //初始
        String INIT = "1";
        //复核中
        String REVIEWING = "2";
        //完成
        String REVIEWED = "3";
        //异常
        String EXCEPTIONTODO = "4";
    }

    //其他出库状态
    interface OtheroutState {
        //未审核
        String UNREVIEW = "0";
        //已审核
        String REVIEWED = "1";
    }
}
