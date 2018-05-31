package com.fengdu.dao;

import com.fengdu.dao.BaseDao;
import com.fengdu.entity.UserCouponVo;

import org.apache.ibatis.annotations.Param;

/**
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
public interface ApiUserCouponMapper extends BaseDao<UserCouponVo> {
    UserCouponVo queryByCouponNumber(@Param("coupon_number") String coupon_number);
}
