package com.example.demo.controller;

import com.example.demo.entity.Message;
import com.example.demo.entity.Result;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 消息相关接口
 */
@RestController
@RequestMapping("/message")
public class messageController {
    private static final Logger logger = LoggerFactory.getLogger(messageController.class);
    private final MessageService messageService;

    @Autowired
    public messageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 新增一条消息
     * @param message 消息对象
     * @return 结果
     */
    @PostMapping("/newMessage")
    public Result newMessage(@RequestBody Message message) {
        logger.info("插入消息: {}", message);
        try {
            messageService.newMessage(message);
            logger.info("插入成功");
            return Result.success(message);
        } catch (Exception e) {
            logger.error("插入消息失败: {}", e.getMessage(), e);
            return Result.error("插入消息失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户收到的消息
     * @param userId 用户ID
     * @return 结果
     */
    @PostMapping("/getMessage")
    public Result getMessage(@RequestParam Integer userId) {
        logger.info("获取用户 {} 收到的消息", userId);
        try {
            List<Message> messages = messageService.getMessagesByUserId(userId);
            if (messages.isEmpty()){
                return Result.error("没有找到消息");
            }
            logger.info("用户 {} 获取的消息为: {}", userId, messages);
            return Result.success(messages);
        } catch (Exception e) {
            logger.error("获取消息失败: {}", e.getMessage(), e);
            return Result.error("获取消息失败: " + e.getMessage());
        }
    }
}
