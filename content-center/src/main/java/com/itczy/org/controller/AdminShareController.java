package com.itczy.org.controller;

import com.itczy.org.domain.dto.ShareAuditDTO;
import com.itczy.org.domain.entity.Share;
import com.itczy.org.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminShareController {

    private final ShareService shareService;

    /**
     * 文件审核接口
     *
     * @param id
     * @param shareAuditDTO
     * @return
     */
    @PutMapping("/audit/{id}")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO shareAuditDTO){
        // TODO 认证，授权

        return shareService.auditById(id, shareAuditDTO);
    }
}
