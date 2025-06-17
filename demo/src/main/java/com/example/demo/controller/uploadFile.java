package com.example.demo.controller;
import com.example.demo.filter.FileTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/circles")
public class uploadFile{
    // 指定本地保存路径
    @Value("${app.other-upload-dir}")
    private String uploadDir;
    // 访问路径
    @Value("${app.other-access-dir}")
    private String accessDir;

    /**
     * 上传图片
     *
     * @param file 图片文件名，包含文件扩展名
     * @return 返回一个包含图片URL的JSON对象
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        log.info("file: {}", file);
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件为空，请选择一个文件上传。");
        }
        try {
            // 创建目标目录（如果不存在）
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成新的文件名（避免重复）
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                originalFilename = originalFilename.replace(fileExtension, "");
            }
            String newFileName = originalFilename + "_" + System.currentTimeMillis() + fileExtension;

            // 文件保存路径
            Path filePath = uploadPath.resolve(newFileName);

            // 保存文件到服务器
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("fileNmae: {}", newFileName);
            String FileUrl = accessDir + "/" +newFileName;
            log.info("fileUrl: {}", FileUrl);
            return ResponseEntity.ok(FileUrl);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);  // 记录异常信息和堆栈
            return ResponseEntity.status(500).body("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 通过文件名获取图片
     *
     * @param fileName 图片文件名，包含文件扩展名
     * @return 返回一个ResponseEntity对象，其中包含图片的字节数组和相关HTTP头信息
     */
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            // 校验文件名是否合法
            if (fileName.contains("..") || fileName.startsWith("/")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("非法文件名".getBytes(StandardCharsets.UTF_8));
            }

            // 构建图片的完整路径
            Path imagePath = Paths.get(uploadDir).resolve(fileName).normalize();

            // 确保解析后的路径在允许的目录下
            if (!imagePath.toRealPath().startsWith(Paths.get(uploadDir).toRealPath())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("不允许访问外部路径".getBytes(StandardCharsets.UTF_8));
            }

            // 读取图片内容到字节数组
            byte[] imageBytes = Files.readAllBytes(imagePath);
            // 根据文件名获取内容类型
            String contentType = FileTools.getContentTypeByExtension(fileName);

            // 返回包含图片内容和HTTP头信息的ResponseEntity对象
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + fileName + "\"")
                    .body(imageBytes);
        } catch (IOException e) {
            // 记录错误日志
            log.error("获取图片失败: {}", fileName, e);
            // 返回错误信息
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}
