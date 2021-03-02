package com.itczy.org.controller;

import com.itczy.org.aspect.CheckAuthorization;
import com.itczy.org.domain.dto.ShareAuditDTO;
import com.itczy.org.domain.entity.Share;
import com.itczy.org.service.ShareService;
import org.apache.commons.lang.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    @CheckAuthorization("SystemAdmin")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO shareAuditDTO, HttpServletRequest request){
        // TODO 认证，授权

        /**
         * 1.土办法
         *
         * 使用Spring给方法注入HttpServletRequest
         * 然后通过传入的角色，判断是否是SystemAdmin
         * 是就放行，不是就拦截返回
         */
        String role =(String) request.getAttribute("role");
        if(!(StringUtils.isNotBlank(role) && "SystemAdmin".equals(role))){
            return new Share();
        }

        /**
         * 2,使用自定义注解和AOP实现拦截
         *
         * 见CheckAuthorizationAspect.java
         */
        return shareService.auditById(id, shareAuditDTO);
    }
}
