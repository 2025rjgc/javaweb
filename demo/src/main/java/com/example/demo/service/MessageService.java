package com.example.demo.service;

import com.example.demo.entity.Message;
import java.util.List;

public interface MessageService {
    List<Message> getMessagesByUserId(Integer userId);

    void newMessage(Message message);
}
