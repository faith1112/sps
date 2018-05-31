package com.fengdu.dao;

import com.fengdu.dao.BaseDao;
import com.fengdu.entity.CommentVo;

import java.util.Map;

/**
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
public interface ApiCommentMapper extends BaseDao<CommentVo> {
    int queryhasPicTotal(Map<String, Object> map);
}
