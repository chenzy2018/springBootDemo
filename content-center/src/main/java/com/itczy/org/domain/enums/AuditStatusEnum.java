package com.itczy.org.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {

    /**
     * 待审核
     * 审核通过
     * 审核不通过
     */
    MOT_YET,
    PASS,
    REJECT
}
