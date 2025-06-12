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

    /**
     * 删除消息
     * @param messageId 消息ID
     * @return 结果
     */
    @DeleteMapping("/delete")
    public Result deleteMessage(@RequestParam Integer messageId) {
        logger.info("删除消息: {}", messageId);
        try {
            messageService.deleteMessage(messageId);
            logger.info("删除成功");
            return Result.success();
        } catch (Exception e) {
            logger.error("删除消息失败: {}", e.getMessage(), e);
            return Result.error("删除消息失败: " + e.getMessage());
        }
    }

    /**
     * 修改消息
     * @param message 消息对象
     * @return 结果
     */
    @PutMapping("/update")
    public Result updateMessage(@RequestBody Message message) {
        logger.info("更新消息: {}", message);
        try {
            messageService.updateMessage(message);
            logger.info("更新成功");
            return Result.success(message);
        } catch (Exception e) {
            logger.error("更新消息失败: {}", e.getMessage(), e);
            return Result.error("更新消息失败: " + e.getMessage());
        }
    }

    /**
     * 多条件查询消息
     * @param userId 用户ID（可选）
     * @param bookId 图书ID（可选）
     * @param text 消息内容（可选）
     * @return 结果
     */
    @GetMapping("/search")
    public Result searchMessages(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String bookId,
            @RequestParam(required = false) String text
    ) {
        logger.info("多条件查询消息: userId={}, bookId={}, text={}", userId, bookId, text);
        try {
            List<Message> messages = messageService.searchMessages(userId, bookId, text);
            return Result.success(messages);
        } catch (Exception e) {
            logger.error("多条件查询消息失败: {}", e.getMessage(), e);
            return Result.error("多条件查询消息失败: " + e.getMessage());
        }
    }
}
