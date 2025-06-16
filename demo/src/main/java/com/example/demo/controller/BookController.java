package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.entity.Result;
import com.example.demo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 图书信息控制器，提供图书的增删改查及静态资源访问功能。
 */
@RestController
@RequestMapping("/book")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    @Value("${app.bookImg-upload-dir}")
    private String BOOK_IMAGE_PATH;

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
            // 安全检查：防止路径穿越
            if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
                logger.warn("非法文件名: {}", fileName);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("无效的文件名".getBytes(StandardCharsets.UTF_8));
            }

            // 规范化文件名并构建路径
            Path imagePath = Paths.get(BOOK_IMAGE_PATH).resolve(fileName).normalize();

            // 确保路径在指定目录下，防止跳出指定目录
            if (!imagePath.toRealPath().startsWith(Paths.get(BOOK_IMAGE_PATH).toRealPath())) {
                logger.warn("尝试访问受限目录外的文件: {}", fileName);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("无权访问该文件".getBytes(StandardCharsets.UTF_8));
            }

            logger.info("获取图书封面图片: {}", fileName);
            logger.info("图片路径: {}", imagePath);

            if (!Files.exists(imagePath)) {
                logger.warn("图片文件不存在: {}", imagePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("文件不存在".getBytes(StandardCharsets.UTF_8));
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);
            MediaType mediaType = getMediaTypeForImageFileName(fileName);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);

        } catch (Exception e) {
            logger.error("读取图片文件失败: {}", fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("读取文件出错: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }


    /**
     * 根据文件名判断图片的MIME类型。
     *
     * @param fileName 文件名
     * @return 对应的MediaType
     */
    private MediaType getMediaTypeForImageFileName(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        if (lowerCaseFileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (lowerCaseFileName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }
        // 默认返回 JPEG 类型
        return MediaType.IMAGE_JPEG;
    }
}