package com.itczy.org.service;

import com.itczy.org.domain.dto.ShareAuditDTO;
import com.itczy.org.domain.entity.Share;

public interface ShareService {

    Share auditById(Integer id, ShareAuditDTO shareAuditDTO);

}
