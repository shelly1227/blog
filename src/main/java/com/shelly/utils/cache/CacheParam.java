package com.shelly.utils.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记方法参数生成缓存键的注解，可以指定参数名
 *
 * @author shelly
 * @date 2024/07/21
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheParam {

    /**
      * 参数名称
      */
    String value() default "";
}
