package com.example.demo.filter;

public class GetContentType {
    /**
     * �����ļ���չ�����ض�Ӧ�� MIME ���͡�
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
     * ��ȡ�ļ���չ����
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) return "";
        return fileName.substring(dotIndex + 1);
    }
}
