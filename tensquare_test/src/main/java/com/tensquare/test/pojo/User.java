package com.tensquare.test.pojo;

import com.tensquare.test.enums.StatusMent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-07-04 12:53:48
 * @describe:
 */
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = "tb_user_dto",
        indexes = {@Index(name="index_name",columnList = "name",unique = false),
                   @Index(name="index_age",columnList = "age",unique = false)})
@org.hibernate.annotations.Table(appliesTo = "tb_user_dto",comment = "用户信息列表")
public class User implements Serializable {

    private static final long serialVersionUID = -1699425269559042856L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(columnDefinition = "varchar(10) not null comment '用户姓名'")
    private String  name;
    @Column(columnDefinition = "varchar(2) not null comment '用户性别'")
    private String  sex;
    @Column(columnDefinition = "int null comment '用户年龄'")
    private Integer age;
    @Column(columnDefinition = "varchar(10) null comment '用户地址'")
    private String address;
    @Column(columnDefinition = "decimal(5,2) null comment '用户薪资'")
    private BigDecimal userPay;
    @Enumerated(value=EnumType.STRING)
    @Column(columnDefinition = "varchar(10) null comment '支付状态'")
    private StatusMent statusMent;
    @Column(columnDefinition = "date null comment '出生日期'")
    private LocalDate birthday;
    @CreatedDate
    @Column(columnDefinition = "datetime null comment '创建时间'",updatable = false)
    private Date createDate;
    @LastModifiedDate
    @Column(columnDefinition = "datetime null comment '修改时间'")
    private LocalDateTime updateDate;
}
