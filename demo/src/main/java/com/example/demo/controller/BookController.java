package com.example.demo.controller;

import com.example.demo.pojo.Book;
import com.example.demo.pojo.Result;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class BookController {
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book")
    /*获取book表全部数据并返回给前端*/
    public Result list(){
        System.out.println("book 查询全部数据");
        List<Book> books = bookService.FindAll();
        return Result.success(books);
    }

    /*多条件查询*/
    @PostMapping("/book/search")
    public Result search(@RequestBody Book book) {
        System.out.println("book 多条件查询:" + book);
        List<Book> books = bookService.search(book);
        return Result.success(books);
    }

    /*删除指定book*/
    @DeleteMapping("/book/{id}")
    public Result delete(@PathVariable String id) {
        System.out.println("book 删除指定数据:" + id);
        bookService.delete(id);
        return Result.success();
    }

    /*增加新用户*/
    @PostMapping("/book/add")
    public Result addBook(@RequestBody Book book) {
        System.out.println("book 添加数据:" + book);
        bookService.addBook(book);
        return Result.success();
    }

    /*修改*/
    @PostMapping("/book")
    public Result update(@RequestBody Book book) {
        System.out.println("book 修改数据:" + book);
        bookService.update(book);
        return Result.success();
    }

    @GetMapping("/txt/{fileName:.+}")
    public Result getFile(@PathVariable String fileName) {
        try {
            // 尝试从classpath下的txt目录加载文件
            ClassPathResource resource = new ClassPathResource("txt/" + fileName);
            if (!resource.exists()) {
                // 如果文件不存在
                return Result.error("文件不存在");
            }

            // 使用UTF-8编码读取文件内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder contentBuilder = new StringBuilder();
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
            String content = contentBuilder.toString();

            // 返回文件内容
            return Result.success(content);

        } catch (Exception e) {
            // 处理异常情况，比如IO错误等
            return Result.error("Error reading file: " + e.getMessage());
        }
    }
}

