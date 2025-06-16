package com.example.demo.mapper;

import com.example.demo.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    // 根据用户id获取所有消息
    List<Message> getMessagesByUserId(Integer ownerId);

    // 新增消息
    void newMessage(Message message);

    // 删除消息
    void deleteMessage(Integer messageId);

    // 修改消息
    void updateMessage(Message message);

    // 搜索消息
    List<Message> searchMessages(@Param("userId") String userId, @Param("bookId") String bookId, @Param("text") String text);
}
