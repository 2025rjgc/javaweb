package com.example.demo.service;

import com.example.demo.entity.Message;
import com.example.demo.view.MessageView;

import java.util.List;

public interface MessageService {
    List<MessageView> getMessagesByUserId(Integer userId);

    void newMessage(Message message);

    void deleteMessage(Integer messageId);

    void updateMessage(Message message);

    List<Message> searchMessages(String userId, String bookId, String text);
}
