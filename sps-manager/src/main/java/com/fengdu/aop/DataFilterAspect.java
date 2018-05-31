package com.fengdu.aop;

import com.fengdu.annotation.DataFilter;
import com.fengdu.entity.SysUserEntity;
import com.fengdu.service.SysRoleDeptService;
import com.fengdu.utils.Constant;
import com.fengdu.utils.RRException;
import com.fengdu.utils.ShiroUtils;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据过滤，切面处理类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017年10月23日 下午13:33:35
 */
@Aspect
@Component
public class DataFilterAspect {
    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Pointcut("@annotation(com.fengdu.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        //获取参数
        Object params = point.getArgs()[0];
        if (params != null && params instanceof Map) {
            SysUserEntity user = ShiroUtils.getUserEntity();

            //如果不是超级管理员，则只能查询本部门及子部门数据
            if (user.getUserId() != Constant.SUPER_ADMIN) {
                Map map = (Map) params;
                map.put("filterSql", getFilterSQL(user, point));
            }

            return;
        }

        throw new RRException("数据权限接口的参数必须为Map类型，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getFilterSQL(SysUserEntity user, JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);

        String userAlias = dataFilter.userAlias();
        String deptAlias = dataFilter.deptAlias();

        StringBuilder filterSql = new StringBuilder();
        filterSql.append(" and ( ");
        if (StringUtils.isNotEmpty(deptAlias) || StringUtils.isNotBlank(userAlias)){
        //    filterSql.append(" 1 = 1 ");
            if (StringUtils.isNotEmpty(deptAlias)) {
                //取出登录用户部门权限
                String alias = getAliasByUser(user.getUserId());
            //    filterSql.append(" and ");
                filterSql.append(deptAlias);
                filterSql.append(" in ");
                filterSql.append(" ( ");
                filterSql.append(alias);
                filterSql.append(" ) ");
                if (StringUtils.isNotBlank(userAlias)) {
                    if (dataFilter.self()) {
                        filterSql.append(" or ");

                    } else {
                        filterSql.append(" and ");
                    }
                }
            }
            if (StringUtils.isNotBlank(userAlias)) {
                //没有部门数据权限，也能查询本人数据
                filterSql.append(userAlias);
                filterSql.append(" = ");
                filterSql.append(user.getUserId());
                filterSql.append(" ");
            }
        }  else {
            return "";
        }
        filterSql.append(" ) ");
        return filterSql.toString();
    }

    /**
     * 取出用户权限
     *
     * @param userId 登录用户Id
     * @return 权限
     */
    private String getAliasByUser(Long userId) {
        @SuppressWarnings("unchecked")
        List<Long> roleOrglist = sysRoleDeptService.queryDeptIdListByUserId(userId);
        StringBuilder roleStr = new StringBuilder();
        String alias = "";
        if (roleOrglist != null && !roleOrglist.isEmpty()) {
            for (Long roleId : roleOrglist) {
                roleStr.append(",");
                roleStr.append("'");
                roleStr.append(roleId);
                roleStr.append("'");
            }
            alias = roleStr.toString().substring(1, roleStr.length());
        }
        return alias;
    }
}
