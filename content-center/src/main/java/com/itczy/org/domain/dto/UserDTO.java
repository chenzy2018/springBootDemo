package com.itczy.org.domain.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Lombok的组合注解
 *
 * AllArgsConstructor:生成带所有参数的构造函数
 * NoArgsConstructor:生成无参构造函数
 * RequiredArgsConstructor:对final类型的数据生成构造函数（Data组合注解包含）
 * Builder:提供构造器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    /**
     * Id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 微信id
     */
    @Column(name = "wx_id")
    private String wxId;

    /**
     * 微信昵称
     */
    @Column(name = "wx_nickname")
    private String wxNickname;

    /**
     * 角色
     */
    private String roles;

    /**
     * 头像地址
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 积分
     */
    private Integer bonus;
}
