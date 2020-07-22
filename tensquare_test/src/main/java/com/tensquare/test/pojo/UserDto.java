package com.tensquare.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.pojo
 * @date: 2020-07-19 16:17:41
 * @describe:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Table(indexes = {@Index(name = "index_name", columnList = "name", unique = true),
        @Index(name = "index_age", columnList = "age", unique = true)})
@org.hibernate.annotations.Table(appliesTo = "user_dto", comment = "校验参数信息表")
public class UserDto implements Serializable {
    private static final long serialVersionUID = 7729098393490305945L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(columnDefinition = "varchar(10) null comment '用户名'")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @Column(columnDefinition = "varchar(2) null comment '性别'")
    @NotBlank(message = "性别不能为空")
    private String sex;
    @Column(columnDefinition = "int null comment '年龄'")
   // @NotEmpty( message = "年龄不能为空")
    private Integer age;
    @Column(columnDefinition = "varchar(225) null comment '内容信息'")
    @NotEmpty(message = "内容信息不能为空")
    private String context;
    @Column(columnDefinition = "datetime null comment '创建时间'")
    @CreatedDate
    private LocalDateTime createDate;
    @Column(columnDefinition = "datetime null comment '最后修改时间'")
    @LastModifiedDate
    private LocalDateTime updateDate;

    public UserDto(@NotBlank(message = "姓名不能为空") String name, @NotBlank(message = "性别不能为空") String sex, Integer age, LocalDateTime createDate) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.createDate = createDate;
    }

}
