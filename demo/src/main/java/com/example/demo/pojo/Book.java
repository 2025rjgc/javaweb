package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer bookId;
    private String bookName;    // 书名
    private String bookPre; // 简介
    private String author;  // 作者
    private String publisher;   // 出版社
    private String genre;   // 类别
    private String bookImg; // 封面图片
    private String bookUrl; // 图书链接
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
