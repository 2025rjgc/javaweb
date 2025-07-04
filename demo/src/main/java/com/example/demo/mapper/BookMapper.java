package com.example.demo.mapper;

import com.example.demo.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {

    // 查询所有
    List<Book> findAll();

    // 根据条件查询
    List<Book> search(Book book);

    // 根据id查询
    Book findById(Integer id);

    // 根据id删除
    boolean delete(String id);

    // 添加
    boolean addBook(Book book);

    // 更新
    boolean update(Book book);

}
