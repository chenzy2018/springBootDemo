package com.itczy.org.rokcetmq;

import com.itczy.org.dao.RocketmqTransactionLogMapper;
import com.itczy.org.domain.dto.ShareAuditDTO;
import com.itczy.org.domain.entity.RocketmqTransactionLog;
import com.itczy.org.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * rocketmq事务监听类
 */
//txProducerGroup需要和rocketMQTemplate.sendMessageInTransaction方法指定的txProducerGroup完全一样
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TestAddBonusTransactionListener implements RocketMQLocalTransactionListener {

    private final ShareService shareService;

    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    /**
     * 执行本地事务的方法
     * @param msg  rocketMQTemplate.sendMessageInTransaction方法传递的msg
     * @param arg  rocketMQTemplate.sendMessageInTransaction方法传递的arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        MessageHeaders headers = msg.getHeaders();

        //header的妙用
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.valueOf((String)headers.get("share_id"));

        try {
            //arg的妙用
            shareService.transactionLogByID(shareId, (ShareAuditDTO) arg, transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.error("本地执行事务异常:", e);
           return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 本地事务执行结果检查方法
     * 通过日志表记录的信息判断执行结果
     *
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {

        MessageHeaders headers = msg.getHeaders();

        //header的妙用
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);

        RocketmqTransactionLog rocketmqTransactionLog = rocketmqTransactionLogMapper.selectOne(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .build()
        );

        if(rocketmqTransactionLog != null) return RocketMQLocalTransactionState.COMMIT;

        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
