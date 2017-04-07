package com.hyperchain.common.annotation;

import java.lang.annotation.*;

/**
 * Created by ldy on 2017/4/5.
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenAuth {
    boolean validate() default true;
}
