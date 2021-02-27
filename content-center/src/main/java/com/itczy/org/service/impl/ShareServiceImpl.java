package com.itczy.org.service.impl;

import com.itczy.org.dao.RocketmqTransactionLogMapper;
import com.itczy.org.dao.ShareMapper;
import com.itczy.org.domain.dto.ShareAuditDTO;
import com.itczy.org.domain.dto.UserAddBonusMagDTO;
import com.itczy.org.domain.entity.RocketmqTransactionLog;
import com.itczy.org.domain.entity.Share;
import com.itczy.org.domain.enums.AuditStatusEnum;
import com.itczy.org.feignClient.TestUserCenterFeignClient;
import com.itczy.org.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareMapper shareMapper;

    private final TestUserCenterFeignClient testUserCenterFeignClient;

    private final RocketMQTemplate rocketMQTemplate;

    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

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

        //2.审核资源，将状态设为PASS/REJECT，如果不开启事务就可以这样写
//        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
//        shareMapper.updateByPrimaryKey(share);


        //3.如果是PASS，那么为发布人添加积分
        //如果addBonus比较费时，就需要异步执行，减少耗时
        //testUserCenterFeignClient.addBonus(id, 500);

        //使用rocketmq发送消息，让消费者去消费，此种写法不支持分布式事务
        rocketMQTemplate.convertAndSend(
                "add-bonus",
                UserAddBonusMagDTO.builder()
                .userId(share.getUserId())
                .bonus(500)
                .build()
        );

        //事务支持方式
        if(AuditStatusEnum.PASS.equals(shareAuditDTO.getAuditStatusEnum())){
            //发送半消息
            String headerValue = UUID.randomUUID().toString();
            rocketMQTemplate.sendMessageInTransaction(
                    "tx-add-bonus-group",
                    "add-bonus",
                    MessageBuilder
                    .withPayload(
                            UserAddBonusMagDTO.builder()
                                    .userId(share.getUserId())
                                    .bonus(500)
                                    .build()
                    )
                    //header有大用处，，在RecketMQ事务监听类(AddBonusTransactionListener)中使用
                    .setHeader(RocketMQHeaders.TRANSACTION_ID, headerValue)
                    .setHeader("share_id", id)
                    .build(),
                    //args有大用处，在RecketMQ事务监听类(AddBonusTransactionListener)中使用
                    shareAuditDTO
            );
        }else if(AuditStatusEnum.REJECT.equals(shareAuditDTO.getAuditStatusEnum())){
            this.auditByID(id, shareAuditDTO);
        }

        return share;
    }

    /**
     * 事务支持，只要发生Exception异常，代码就回滚
     *
     * 类上注明了事务，那整个类的所有方法都纳入了事务
     * 方法标注了事务，会覆盖类上标注的事务
     */
    @Transactional(rollbackFor = Exception.class)
    public void auditByID(Integer id ,ShareAuditDTO shareAuditDTO) {
        Share share = Share.builder()
                .id(id)
                .auditStatus(shareAuditDTO.getAuditStatusEnum().toString())
                .reason(shareAuditDTO.getReason())
                .build();
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        shareMapper.updateByPrimaryKeySelective(share);

        //把share写入缓存
    }

    @Transactional(rollbackFor = Exception.class)
    public void transactionLogByID(Integer id ,ShareAuditDTO shareAuditDTO, String transactionId){
        auditByID(id, shareAuditDTO);

        rocketmqTransactionLogMapper.insertSelective(
            RocketmqTransactionLog.builder()
                    .transactionId(transactionId)
                    .log("审核分享")
                    .build()
        );
    }

}
