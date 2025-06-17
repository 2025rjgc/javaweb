package com.example.demo.service.impl;

import com.example.demo.mapper.BookMapper;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.example.demo.filter.FileTools.isImageFile;

/**
 * 图书服务实现类，提供图书的增删改查业务逻辑。
 */
@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookMapper bookMapper;
    // 注入上传目录路径（application.yml 配置）
    @Value("${app.bookImg-upload-dir}")
    private String uploadDir;
    @Value("${app.bookImg-access-dir}")
    private String accessDir;

    @Autowired
    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * 获取所有图书信息。
     *
     * @return 返回图书列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Book> FindAll() {
        logger.info("开始获取所有图书信息...");
        try {
            List<Book> books = bookMapper.findAll();
            logger.info("成功获取到 {} 本图书", books.size());
            return books;
        } catch (Exception e) {
            logger.error("获取图书列表失败", e);
            throw new RuntimeException("数据库查询失败，请检查日志");
        }
    }

    /**
     * 多条件查询图书信息。
     *
     * @param book 查询条件对象
     * @return 匹配的图书列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Book> search(Book book) {
        if (book == null) {
            logger.warn("图书查询条件为空");
            return List.of();
        }

        logger.info("开始多条件查询图书: {}", book);
        try {
            List<Book> result = bookMapper.search(book);
            logger.info("查询完成，共找到 {} 本图书", result.size());
            return result;
        } catch (Exception e) {
            logger.error("图书查询失败", e);
            throw new RuntimeException("图书查询出错，请检查条件或联系管理员");
        }
    }

    /**
     * 删除指定ID的图书。
     *
     * @param id 要删除的图书ID
     * @return 删除成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            logger.warn("无效的图书ID");
            return false;
        }

        logger.info("开始删除图书，ID: {}", id);
        try {
            boolean success = bookMapper.delete(id);
            if (success) {
                logger.info("图书删除成功");
            } else {
                logger.warn("未找到对应的图书，删除失败");
            }
            return success;
        } catch (Exception e) {
            logger.error("删除图书失败", e);
            throw e;
        }
    }

    /**
     * 新增一本图书。
     *
     * @param book 要新增的图书对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBook(Book book) {
        if (book == null) {
            logger.warn("新增图书对象为空");
            return;
        }

        logger.info("开始新增图书: {}", book);
        try {
            LocalDateTime now = LocalDateTime.now();
            book.setCreated_at(now);
            book.setUpdated_at(now);

            boolean rowsAffected = bookMapper.addBook(book);
            if (rowsAffected) {
                logger.info("图书添加成功，ID: {}", book.getBookId());
            } else {
                logger.warn("图书添加失败");
            }
        } catch (Exception e) {
            logger.error("添加图书失败", e);
            throw e;
        }
    }

    /**
     * 修改已有图书信息。
     *
     * @param book 要修改的图书对象
     * @return 修改成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Book book) {
        if (book == null || Objects.isNull(book.getBookId())) {
            logger.warn("图书ID为空，无法更新");
            return false;
        }

        logger.info("开始更新图书: {}", book);
        try {
            book.setUpdated_at(LocalDateTime.now());
            boolean rowsAffected = bookMapper.update(book);
            if (rowsAffected) {
                logger.info("图书更新成功，ID: {}", book.getBookId());
            } else {
                logger.warn("图书更新失败，可能不存在该ID");
            }
            return rowsAffected;
        } catch (Exception e) {
            logger.error("更新图书失败", e);
            throw e;
        }
    }
    /**
     * 根据ID查询图书信息
     *
     * @param bookId 图书ID
     * @return 图书信息
     * */
    @Override
    public Book findById(Integer bookId) {
        return bookMapper.findById(bookId);
    }
    /**
     * 更新图书封面
     *
     * @param file 要上传的图片文件
     * @param filename 文件名
     * */

    @Override
    public String updateImage(MultipartFile file, String filename) {
        try {
            logger.info("开始上传封面: {}", filename);

            // 文件类型检查
            if (!isImageFile(file)) {
                logger.warn("不允许的文件类型: {}", filename);
                return null;
            }

            // 文件大小限制（5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                logger.warn("文件过大: {}", filename);
                return null;
            }

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 构建完整文件路径
            Path filePath = uploadPath.resolve(filename);
            logger.info("保存文件路径: {}", filePath);
            // 保存文件
            Files.write(filePath, file.getBytes());

            String ImageUrl = accessDir+ "/" + filename;

            logger.info("封面上传成功: {}", filename);
            return ImageUrl;

        } catch (IOException e) {
            logger.error("上传封面失败: {}", filename, e);
            return null;
        }
    }
}