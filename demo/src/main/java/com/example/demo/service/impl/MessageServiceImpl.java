package com.example.demo.service.impl;

import com.example.demo.entity.Message;
import com.example.demo.mapper.CircleMapper;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.service.BookService;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import com.example.demo.view.MessageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 新增事务管理

import java.util.ArrayList;
import java.util.List;

/**
 * 消息服务实现类
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final UserService userService;
    private final CircleMapper circleMapper;
    private final MessageMapper messageMapper;
    private final BookService bookService;

    @Autowired
    public MessageServiceImpl(UserService userService, CircleMapper circleMapper, MessageMapper messageMapper, BookService bookService) {
        this.userService = userService;
        this.circleMapper = circleMapper;
        this.messageMapper = messageMapper;
        this.bookService = bookService;
    }

    /**
     * 根据用户ID获取其导师的消息
     * @param userId 用户ID
     * @return 消息列表
     */
    @Override
    public List<MessageView> getMessagesByUserId(Integer userId) {
        try {
            // 获取用户信息-圈子id
            Integer circleId = userService.findById(userId).getCircleId();
            if (circleId == null) {
                return List.of(); // 没有圈子，返回空
            }
            // 获取圈子信息-导师用户名
            String owner = circleMapper.getInfo(circleId).getOwner();
            if (owner == null) {
                return List.of(); // 圈子不存在，返回空
            }
            // 获取导师用户ID
            Integer ownerId = userService.findByName(owner).getUserId();
            // 获取导师对应的消息
            List<Message> messages = messageMapper.getMessagesByUserId(ownerId);
            ArrayList<MessageView> messageViews = new ArrayList<>();
            for (Message message : messages){
                MessageView messageView = new MessageView(
                        message.getMessageId(),
                        message.getUserId(),
                        message.getBookId(),
                        userService.findById(message.getUserId()).getUsername(),
                        bookService.findById(message.getBookId()).getBookName(),
                        message.getText(),
                        message.getCreateTime(),
                        message.getUpdateTime());
                messageViews.add(messageView);
            }
            return messageViews;
        } catch (Exception e) {
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
            throw new RuntimeException("新增消息失败", e);
        }
    }

    /**
     * 删除一条消息（带事务管理）
     * @param messageId 消息ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Integer messageId) {
        try {
            messageMapper.deleteMessage(messageId);
        } catch (Exception e) {
            throw new RuntimeException("删除消息失败", e);
        }
    }

    /**
     * 更新一条消息（带事务管理）
     * @param message 消息对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMessage(Message message) {
        try {
            messageMapper.updateMessage(message);
        } catch (Exception e) {
            throw new RuntimeException("更新消息失败", e);
        }
    }

    /**
     * 多条件查询消息
     * @param userId 用户ID
     * @param bookId 书籍ID
     * @param text 查询文本
     * @return 消息列表
     */
    @Override
    public List<Message> searchMessages(String userId, String bookId, String text) {
        try {
            return messageMapper.searchMessages(userId, bookId, text);
        } catch (Exception e) {
            throw new RuntimeException("多条件查询消息失败", e);
        }
    }
}
