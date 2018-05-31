package com.fengdu.dao;

import com.fengdu.dao.BaseDao;
import com.fengdu.entity.KeywordsVo;

import java.util.List;
import java.util.Map;

/**
 * 热闹关键词表
 * 
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
public interface ApiKeywordsMapper extends BaseDao<KeywordsVo> {
    List<Map> hotKeywordList(Map param);
}
