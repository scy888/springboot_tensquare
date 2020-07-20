package com.tensquare.test.pojo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-07-20 21:28:26
 * @describe:
 */
@Data
public class AdminReq {
    @NotBlank(message = "管理员用户名不能为空")
    private String admin;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String authCode;
}
