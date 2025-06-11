package com.example.demo.controller;

import com.example.demo.pojo.Book;
import com.example.demo.pojo.Result;
import com.example.demo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 图书信息控制器，提供图书的增删改查及静态资源访问功能。
 */
@RestController
@RequestMapping("/book")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 获取所有图书信息。
     *
     * @return 返回图书列表
     */
    @GetMapping("")
    public Result list() {
        logger.info("获取所有图书信息");
        List<Book> books = bookService.FindAll();
        return Result.success(books);
    }

    /**
     * 多条件查询图书信息。
     *
     * @param book 查询条件对象
     * @return 匹配的图书列表
     */
    @PostMapping("/search")
    public Result search(@RequestBody Book book) {
        logger.info("多条件查询图书: {}", book);
        List<Book> books = bookService.search(book);
        return Result.success(books);
    }

    /**
     * 删除指定ID的图书。
     *
     * @param id 要删除的图书ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        logger.info("尝试删除图书 ID: {}", id);
        if (id == null || id.isEmpty()) {
            logger.warn("无效的图书ID");
            return Result.error("图书ID不能为空");
        }
        try {
            bookService.delete(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            logger.error("删除图书失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 新增一本图书。
     *
     * @param book 要新增的图书对象
     * @return 新增结果
     */
    @PostMapping("/add")
    public Result addBook(@RequestBody Book book) {
        logger.info("添加新图书: {}", book);
        if (book == null) {
            logger.warn("图书对象为空");
            return Result.error("图书信息不能为空");
        }
        try {
            bookService.addBook(book);
            return Result.success("添加成功");
        } catch (Exception e) {
            logger.error("添加图书失败", e);
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 修改已有图书信息。
     *
     * @param book 要修改的图书对象
     * @return 修改结果
     */
    @PostMapping("")
    public Result update(@RequestBody Book book) {
        logger.info("更新图书信息: {}", book);
        if (book == null || book.getBookId() == null) {
            logger.warn("图书ID为空");
            return Result.error("图书ID不能为空");
        }
        try {
            bookService.update(book);
            return Result.success("更新成功");
        } catch (Exception e) {
            logger.error("更新图书失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取图书封面图片。
     *
     * @param fileName 图片文件名
     * @return 返回图片字节流
     */
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("images/book/" + fileName);
            if (!resource.exists()) {
                logger.warn("图片文件不存在: {}", fileName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("文件不存在".getBytes(StandardCharsets.UTF_8));
            }

            byte[] imageBytes = resource.getInputStream().readAllBytes();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);

        } catch (Exception e) {
            logger.error("读取图片文件失败: {}", fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("读取文件出错: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 获取图书介绍文本内容。
     *
     * @param fileName 文本文件名
     * @return 返回文本内容字符串
     */
    @GetMapping("/txt/{fileName:.+}")
    public Result getFile(@PathVariable String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("txt/" + fileName);
            if (!resource.exists()) {
                logger.warn("文本文件不存在: {}", fileName);
                return Result.error("文件不存在");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder contentBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }

            return Result.success(contentBuilder.toString());

        } catch (Exception e) {
            logger.error("读取文本文件失败: {}", fileName, e);
            return Result.error("读取文件失败：" + e.getMessage());
        }
    }
}