package com.example.demo.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/circles")
public class uploadFile    {
    // 指定本地保存路径
    @Value("${app.other-upload-dir}")
    private String uploadDir;

    @Value("${app.ip}")
    private String ip;
        @PostMapping("/upload")
        public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
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

                // 构建访问 URL，需要根据服务器的实际ip地址来构建
                String fileUrl = ip + newFileName;
                log.info("fileNmae: {}", newFileName);
                log.info("fileUrl: {}", fileUrl);
                return ResponseEntity.ok(fileUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("文件上传失败: " + e.getMessage());
            }
        }
    }
