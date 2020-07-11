package com.tensquare.batch.pojo;

import lombok.Data;

import java.util.Collection;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.pojo
 * @date: 2020-07-11 23:00:27
 * @describe:
 */
@Data
public class Instance {
    private Long id;
    private String jobName;
    private Collection<Job> jobCollection;
}
