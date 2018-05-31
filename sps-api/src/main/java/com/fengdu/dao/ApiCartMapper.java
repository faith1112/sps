package com.fengdu.dao;

import com.fengdu.dao.BaseDao;
import com.fengdu.entity.CartVo;

import org.apache.ibatis.annotations.Param;

/**
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
public interface ApiCartMapper extends BaseDao<CartVo> {
    void updateCheck(@Param("productIds") String[] productIds,
                     @Param("isChecked") Integer isChecked, @Param("userId") Long userId);

    void deleteByProductIds(@Param("productIds") String[] productIds);

    void deleteByUserAndProductIds(@Param("user_id") Long user_id,@Param("productIds") String[] productIds);

    void deleteByCart(@Param("user_id") Long user_id, @Param("session_id") Integer session_id, @Param("checked") Integer checked);
}
