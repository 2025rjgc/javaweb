package com.example.demo.service.impl;

import com.example.demo.entity.Message;
import com.example.demo.mapper.CircleMapper;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 新增事务管理
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 消息服务实现类
 */
@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final UserService userService;
    private final CircleMapper circleMapper;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(UserService userService, CircleMapper circleMapper, MessageMapper messageMapper) {
        this.userService = userService;
        this.circleMapper = circleMapper;
        this.messageMapper = messageMapper;
    }

    /**
     * 根据用户ID获取其导师的消息
     * @param userId 用户ID
     * @return 消息列表
     */
    @Override
    public List<Message> getMessagesByUserId(Integer userId) {
        try {
            // 获取用户信息-圈子id
            Integer circleId = userService.findById(userId).getCircleId();
            if (circleId == null) {
                logger.warn("用户 {} 没有加入圈子", userId);
                return List.of(); // 没有圈子，返回空
            }
            // 获取圈子信息-导师id
            Integer ownerId = circleMapper.getInfo(circleId).getOwner();
            if (ownerId == null) {
                logger.warn("圈子 {} 不存在", circleId);
                return List.of(); // 圈子不存在，返回空
            }
            // 获取导师对应的消息
            return messageMapper.getMessagesByUserId(ownerId);
        } catch (Exception e) {
            logger.error("获取用户 {} 消息时发生异常: {}", userId, e.getMessage(), e);
            throw new RuntimeException("获取消息失败", e);
        }
    }

    /**
     * 新增一条消息（带事务管理）
     * @param message 消息对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newMessage(Message message) {
        try {
            messageMapper.newMessage(message);
        } catch (Exception e) {
            logger.error("新增消息时发生异常: {}", e.getMessage(), e);
            throw new RuntimeException("新增消息失败", e);
        }
    }
}
