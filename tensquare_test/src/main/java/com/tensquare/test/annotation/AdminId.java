package com.tensquare.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.annotation
 * @date: 2020-08-24 22:25:44
 * @describe:
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminId {
}
