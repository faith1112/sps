package com.fengdu.dao;

import com.fengdu.dao.BaseDao;
import com.fengdu.entity.CouponVo;

import java.util.List;
import java.util.Map;

/**
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
public interface ApiCouponMapper extends BaseDao<CouponVo> {
    /**
     * 按条件查询用户优惠券
     *
     * @param params
     * @return
     */
    List<CouponVo> queryUserCoupons(Map<String, Object> params);

    /**
     * 按类型查询
     *
     * @param params
     * @return
     */
    CouponVo queryMaxUserEnableCoupon(Map<String, Object> params);

    /**
     * sendType = 1或4 的优惠券
     *
     * @param params
     * @return
     */
    List<CouponVo> queryUserCouponList(Map<String, Object> params);
}
