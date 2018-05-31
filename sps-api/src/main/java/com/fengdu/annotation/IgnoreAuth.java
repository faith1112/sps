package com.fengdu.annotation;

import java.lang.annotation.*;

/**
 * 忽略Token验证
 * @author wangzizhan  
 * @date 2018年5月31日  
 * @version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {

}
