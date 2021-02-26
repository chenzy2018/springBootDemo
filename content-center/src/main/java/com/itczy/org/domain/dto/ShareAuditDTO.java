package com.itczy.org.domain.dto;

import com.itczy.org.domain.enums.AuditStatusEnum;
import lombok.Data;

@Data
public class ShareAuditDTO {

    /**
     * 审核状态
     */
    private AuditStatusEnum auditStatusEnum;

    /**
     * 原因
     */
    private String reason;
}
