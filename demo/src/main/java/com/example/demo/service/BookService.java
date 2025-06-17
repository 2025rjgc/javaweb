package com.example.demo.service;

import com.example.demo.entity.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    List<Book> FindAll();

    List<Book> search(Book book);

    boolean delete(String id);

    void addBook(Book book);

    boolean update(Book book);

    Book findById(Integer bookId);

    String updateImage(MultipartFile file, String filename);
}
