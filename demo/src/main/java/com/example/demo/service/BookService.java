package com.example.demo.service;

import com.example.demo.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> FindAll();

    List<Book> search(Book book);

    boolean delete(String id);

    boolean addBook(Book book);

    boolean update(Book book);

    Book findById(Integer bookId);
}
