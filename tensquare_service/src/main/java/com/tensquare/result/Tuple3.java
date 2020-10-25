package com.tensquare.result;

import lombok.Value;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.result
 * @date: 2020-10-25 16:28:47
 * @describe: 三元元祖
 */

@Value
public class Tuple3<T,R,U> {
    private T first;//第一个元素
    private R second;//第二各元素
    private U third;//第三个元素
}
