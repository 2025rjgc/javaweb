package com.example.demo.mapper;

import com.example.demo.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> getMessagesByUserId(Integer ownerId);

    void newMessage(Message message);

    void deleteMessage(Integer messageId);

    void updateMessage(Message message);

    List<Message> searchMessages(@Param("userId") String userId, @Param("bookId") String bookId, @Param("text") String text);
}
