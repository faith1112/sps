package com.fengdu.dao;

import com.fengdu.dao.BaseDao;
import com.fengdu.entity.FootprintVo;

import java.util.List;
import java.util.Map;

/**
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
public interface ApiFootprintMapper extends BaseDao<FootprintVo> {
    int deleteByParam(Map<String, Object> map);

    List<FootprintVo> shareList(Map<String, Object> map);
}
