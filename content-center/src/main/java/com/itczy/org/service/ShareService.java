package com.itczy.org.service;

import com.itczy.org.domain.dto.ShareAuditDTO;

public interface ShareService {

    public String auditById(Integer id, ShareAuditDTO shareAuditDTO);

}
