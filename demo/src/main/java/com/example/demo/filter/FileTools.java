package com.example.demo.filter;

import org.springframework.web.multipart.MultipartFile;

public class FileTools {
    /**
     * 根据文件扩展名返回对应的 MIME 类型。
     */
    public static String getContentTypeByExtension(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        switch (extension) {
            case "png": return "image/png";
            case "gif": return "image/gif";
            case "webp": return "image/webp";
            case "bmp": return "image/bmp";
            case "svg": return "image/svg+xml";
            case "jpg":
            case "jpeg":
            default: return "image/jpeg";
        }
    }

    /**
     * 获取文件扩展名。
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) return "";
        return fileName.substring(dotIndex + 1);
    }

    /**
     * 检查是否为图片文件。
     *
     * @param file 文件
     * @return 是图片返回 true，否则 false
     */
    public static boolean isImageFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return originalFilename != null && (
                originalFilename.toLowerCase().endsWith(".png") ||
                        originalFilename.toLowerCase().endsWith(".jpg") ||
                        originalFilename.toLowerCase().endsWith(".jpeg"));
    }
}
