package com.tensquare.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.annotation
 * @date: 2020-07-21 23:14:54
 * @describe:
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminName {
}
