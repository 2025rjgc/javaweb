package com.example.demo.service.impl;

import com.example.demo.mapper.BookMapper;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;

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

import static com.example.demo.utils.FileTools.isImageFile;

/**
 * 图书服务实现类，提供图书的增删改查业务逻辑。
 */
@Service
public class BookServiceImpl implements BookService {

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
        try {
            return bookMapper.findAll();
        } catch (Exception e) {
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
            return List.of();
        }

        try {
            return bookMapper.search(book);
        } catch (Exception e) {
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
            return false;
        }

        return bookMapper.delete(id);
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
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        book.setCreated_at(now);
        book.setUpdated_at(now);

        bookMapper.addBook(book);
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
            return false;
        }

        book.setUpdated_at(LocalDateTime.now());
        return bookMapper.update(book);
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

            // 文件类型检查
            if (!isImageFile(file)) {
                return null;
            }

            // 文件大小限制（5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                return null;
            }

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 构建完整文件路径
            Path filePath = uploadPath.resolve(filename);
            // 保存文件
            Files.write(filePath, file.getBytes());

            return accessDir+ "/" + filename;

        } catch (IOException e) {
            return null;
        }
    }
}