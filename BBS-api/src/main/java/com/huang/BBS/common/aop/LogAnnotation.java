package com.huang.BBS.common.aop;

import java.lang.annotation.*;

/**
 * 日志注解
 */

//type 代表可以放在类上的Method 代表可以放在方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operation() default "";
}
