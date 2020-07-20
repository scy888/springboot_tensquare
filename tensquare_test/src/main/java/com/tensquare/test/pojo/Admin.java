package com.tensquare.test.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-07-20 21:34:32
 * @describe:
 */
@Entity
@Data
@Accessors(chain = true)
@Table(name = "tb_admin")
@org.hibernate.annotations.Table(appliesTo = "tb_admin",comment = "管理员用户")
public class Admin implements Serializable {
    private static final long serialVersionUID = 7580850237104983976L;
    @Id
    @Column(columnDefinition = "varchar(20) not null comment '管理员ID'")
    private String adminId;
    @Column(columnDefinition = "varchar(10) not null comment '用户名'")
    private String admin;
    @Column(columnDefinition = "varchar(100) not null comment '密码'")
    private String password;
    @Column(columnDefinition = "varchar(4) not null comment '验证码'")
    private String authCode;
    @Column(columnDefinition = "boolean not null comment '是否有效'")
    private Boolean isEffetive;
}
