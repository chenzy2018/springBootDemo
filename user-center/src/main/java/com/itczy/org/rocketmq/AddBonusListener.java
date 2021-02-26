package com.itczy.org.rocketmq;

import com.itczy.org.dao.BonusEventLogMapper;
import com.itczy.org.dao.UserMapper;
import com.itczy.org.domain.dto.UserAddBonusMagDTO;
import com.itczy.org.domain.entity.BonusEventLog;
import com.itczy.org.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
//必须指定consumerGroup，值为生产者配置中的group，topic为消息主题
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "test-group", topic ="add-bonus")
public class AddBonusListener implements RocketMQListener<UserAddBonusMagDTO> {

    private final UserMapper userMapper;

    private final BonusEventLogMapper bonusEventLogMapper;

    @Override
    public void onMessage(UserAddBonusMagDTO userAddBonusMagDTO) {
        //1.为用户加积分
        Integer userId = userAddBonusMagDTO.getUserId();
        Integer bonus = userAddBonusMagDTO.getBonus();
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateByPrimaryKeySelective(user);

        //2.记录日志到bonus_event_log表里
        bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .event("CONETIBUTE")
                        .createTime(new Date())
                        .description("投稿积分加积分")
                        .build()
        );
    }
}
