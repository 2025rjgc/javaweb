package com.example.demo.service;

import com.example.demo.entity.Message;
import java.util.List;

public interface MessageService {
    List<Message> getMessagesByUserId(Integer userId);

    void newMessage(Message message);

    void deleteMessage(Integer messageId);

    void updateMessage(Message message);

    List<Message> searchMessages(String userId, String bookId, String text);
}
