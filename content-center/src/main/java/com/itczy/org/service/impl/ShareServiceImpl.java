package com.itczy.org.service.impl;

import com.itczy.org.dao.ShareMapper;
import com.itczy.org.domain.dto.ShareAuditDTO;
import com.itczy.org.domain.dto.UserAddBonusMagDTO;
import com.itczy.org.domain.entity.Share;
import com.itczy.org.feignClient.TestUserCenterFeignClient;
import com.itczy.org.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareMapper shareMapper;

    private final TestUserCenterFeignClient testUserCenterFeignClient;

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public Share auditById(Integer id, ShareAuditDTO shareAuditDTO) {

        //1.查询share是否存在，不存在或者当前的audit_status != NOT_YET，那么抛异常
        Share share = shareMapper.selectByPrimaryKey(id);//  mapper层还没实现，此处应该查库的
        if(share == null){
            throw new IllegalArgumentException("参数非法，该分享不存在");
        }
        if(!Objects.equals("NOT_YET",share.getAuditStatus())){
            throw new IllegalArgumentException("参数非法，该分享已完成审核");
        }

        //2.审核资源，将状态设为PASS/REJECT
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        shareMapper.updateByPrimaryKey(share);

        //3.如果是PASS，那么为发布人添加积分
        //如果addBonus比较费时，就需要异步执行，减少耗时
        testUserCenterFeignClient.addBonus(id, 500);

        rocketMQTemplate.convertAndSend(
                "add-bonus",
                UserAddBonusMagDTO.builder()
                .userId(share.getUserId())
                .bonus(500)
                .build()
        );

        return share;
    }
}
