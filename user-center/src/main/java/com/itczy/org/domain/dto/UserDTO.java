package com.itczy.org.domain.dto;

import lombok.*;

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
    private String name;
    private int age;
    private String address;
}
