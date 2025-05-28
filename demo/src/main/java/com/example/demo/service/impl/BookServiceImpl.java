package com.example.demo.service.impl;

import com.example.demo.mapper.BookMapper;
import com.example.demo.pojo.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    // 构造器注入
    private final BookMapper bookMapper;
    @Autowired
    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public List<Book> FindAll() {
        // 调用mapper层
        return bookMapper.findAll();
    }

    @Override
    public List<Book> search(Book book) {
        return bookMapper.search(book);
    }

    @Override
    public boolean delete(String id) {
        return bookMapper.delete(id);
    }

    @Override
    public boolean addBook(Book book) {
        book.setCreated_at(LocalDateTime.now());
        book.setUpdated_at(LocalDateTime.now());
        return bookMapper.addBook(book);
    }

    @Override
    public boolean update(Book book) {
        book.setCreated_at(LocalDateTime.now());
        return bookMapper.update(book);
    }
}
