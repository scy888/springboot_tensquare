package com.tensquare.req;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.req
 * @date: 2020-08-03 21:41:33
 * @describe:
 */
@Data
@Accessors(chain = true)
public class UserDtoReq implements Serializable {
    private static final long serialVersionUID = -811880025667192613L;
    private Integer userId;
    private String name;
    private String sex;
    private Integer age;
    private String context;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
