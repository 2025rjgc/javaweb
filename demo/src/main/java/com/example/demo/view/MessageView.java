package com.example.demo.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 消息视图
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageView {
    private Integer messageId;  // 消息id
    private Integer userId;  // 用户id
    private Integer bookId;  // 图书id
    private String userName;    //  用户名
    private String bookName;    // 图书名
    private String text;    // 消息内容
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}
