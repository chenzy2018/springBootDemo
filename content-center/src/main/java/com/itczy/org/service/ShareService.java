package com.itczy.org.service;

import com.itczy.org.domain.dto.ShareAuditDTO;
import com.itczy.org.domain.dto.ShareDTO;
import com.itczy.org.domain.entity.Share;

public interface ShareService {

    ShareDTO findById(Integer id);

    Share auditById(Integer id, ShareAuditDTO shareAuditDTO);

    void transactionLogByID(Integer id ,ShareAuditDTO shareAuditDTO, String transactionId);

}
