package com.fengdu.dao;

import com.fengdu.dao.BaseDao;
import com.fengdu.entity.TokenEntity;

import org.apache.ibatis.annotations.Param;

/**
 * 用户Token
 *
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
public interface ApiTokenMapper extends BaseDao<TokenEntity> {

    TokenEntity queryByUserId(@Param("userId") Long userId);

    TokenEntity queryByToken(@Param("token") String token);

}
