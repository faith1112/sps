package com.fengdu.dao;

import com.fengdu.dao.BaseDao;
import com.fengdu.entity.SearchHistoryVo;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
public interface ApiSearchHistoryMapper extends BaseDao<SearchHistoryVo> {
    int deleteByUserId(@Param("user_id") Long userId);
}
