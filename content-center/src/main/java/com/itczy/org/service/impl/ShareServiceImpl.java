package com.itczy.org.service.impl;

import com.itczy.org.domain.dto.ShareAuditDTO;
import com.itczy.org.service.ShareService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ShareServiceImpl implements ShareService {
    @Override
    public String auditById(Integer id, ShareAuditDTO shareAuditDTO) {

        //1.查询share是否存在，不存在或者当前的audit_status != NOT_YET，那么抛异常
        String share = "";//this,shareMapper.selectByPrimareKey  mapper层还没实现，此处应该查库的
        if(share == null){
            throw new IllegalArgumentException("参数非法，该分享不存在");
        }
        if(!Objects.equals("NOT_YET","NOT_YET")){
            throw new IllegalArgumentException("参数非法，该分享已完成审核");
        }
        //2.审核资源，将状态设为PASS/REJECT
        //3.如果是PASS，那么为发布人添加积分
        //异步执行，减少耗时


        return "dsf";
    }
}
