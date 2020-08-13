package com.tensquare.batch.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.pojo
 * @date: 2020-07-11 23:40:01
 * @describe:
 */
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = "tb_student")
public class Student implements Serializable {
    private static final long serialVersionUID = 5277608371921074698L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;
    private String name;
    private String sex;
    private Integer age;
    private String address;
    private LocalDate birthday;
    @CreatedDate
    @Column(updatable=false)
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime lastUpDate;

}