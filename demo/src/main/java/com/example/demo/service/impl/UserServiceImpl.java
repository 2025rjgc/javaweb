package com.example.demo.service.impl;

import com.example.demo.mapper.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户服务实现类，包含用户注册、登录、信息更新等业务逻辑。
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    // 注入上传目录路径（application.yml 配置）
    @Value("${app.userImg-upload-dir}")
    private String uploadDir;
    @Value("${app.userImg-access-dir}")
    private String accessDir;

    // 构造器注入 UserMapper
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户登录方法。
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功则返回包含用户信息和 token 的 Map，失败返回 null
     */
    @Override
    public Object login(String username, String password) {
        logger.info("用户尝试登录: {}", username);

        List<User> userData = userMapper.findByUsernameAndPassword(username, password);
        if (userData.isEmpty()) {
            logger.warn("用户名或密码错误: {}", username);
            return null;
        }

        User user = userData.get(0);
        String JWT_token = JWTUtils.createJWT(username, password, 1000 * 60 * 60 * 24 * 7); // 7天有效期

        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", user.getUserId());
        map.put("username", user.getUsername());
        map.put("role", user.getRole());
        map.put("token", JWT_token);

        logger.info("用户登录成功: {}", username);
        return map;
    }

    /**
     * 用户注册方法。
     *
     * @param user 用户对象，包含用户名和密码
     * @return 注册成功返回用户ID和用户名，失败返回 null
     */
    @Override
    @Transactional
    public Object register(User user) {
        logger.info("开始注册用户");

        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            logger.warn("用户名或密码为空");
            return null;
        }

        if (!userMapper.find(user).isEmpty()) {
            logger.warn("用户已存在: {}", user.getUsername());
            return null;
        }

        user.setRole(0);
        user.setAvatar(accessDir + "/" + "default.png"); // 设置默认头像
        userMapper.insert(user);

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", user.getUserId());
        map.put("username", user.getUsername());

        logger.info("用户注册成功: {}", user.getUsername());
        return map;
    }

    /**
     * 获取所有用户信息。
     *
     * @return 用户列表
     */
    @Override
    public List<User> getAllUserInfo() {
        logger.info("获取所有用户信息");
        return userMapper.findAll();
    }

    /**
     * 更新用户信息。
     *
     * @param user 用户对象
     * @return 是否更新成功
     */
    @Override
    @Transactional
    public boolean updateUserInfo(User user) {
        logger.info("更新用户信息: {}", user);
        int rowsAffected = userMapper.updateUserInfo(user);
        return rowsAffected > 0;
    }

    /**
     * 删除用户。
     *
     * @param username 用户名
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteUser(String username) {
        logger.info("删除用户: {}", username);
        int rowsAffected = userMapper.deleteUser(username);
        return rowsAffected > 0;
    }

    /**
     * 上传用户头像。
     *
     * @param file     头像文件
     * @param filename 文件名（含用户ID）
     * @return 是否上传成功
     */
    @Override
    @Transactional
    public boolean updateUserImage(MultipartFile file, String filename) {
        try {
            logger.info("开始上传头像: {}", filename);

            // 文件类型检查
            if (!isImageFile(file)) {
                logger.warn("不允许的文件类型: {}", filename);
                return false;
            }

            // 文件大小限制（5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                logger.warn("文件过大: {}", filename);
                return false;
            }

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 解析用户ID
            String userId = filename.split("_")[0];

            // 构建完整文件路径
            Path filePath = uploadPath.resolve(filename);
            logger.info("保存文件路径: {}", filePath);
            // 保存文件
            Files.write(filePath, file.getBytes());

            // 更新数据库中的头像路径
            User user = new User();
            user.setUserId(Integer.parseInt(userId));
            user.setAvatar(accessDir + "/" + filename);
            userMapper.updateUserInfo(user);

            logger.info("头像上传成功: {}", filename);
            return true;

        } catch (IOException e) {
            logger.error("上传头像失败: {}", filename, e);
            return false;
        }
    }

    /**
     * 检查是否为图片文件。
     *
     * @param file 文件
     * @return 是图片返回 true，否则 false
     */
    private boolean isImageFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return originalFilename != null && (
                originalFilename.toLowerCase().endsWith(".png") ||
                        originalFilename.toLowerCase().endsWith(".jpg") ||
                        originalFilename.toLowerCase().endsWith(".jpeg"));
    }

    /**
     * 获取用户头像字节流。
     *
     * @param fileName 文件名
     * @return 图片字节流
     * @throws IOException 文件不存在时抛出异常
     */
    @Override
    public byte[] getUserImage(String fileName) throws IOException {
        logger.info("获取用户头像: {}", fileName);
        Path imagePath = Paths.get(uploadDir, fileName);
        logger.info("图片路径: {}", imagePath);
        if (!Files.exists(imagePath)) {
            logger.warn("文件不存在: {}", fileName);
            throw new IOException("文件不存在: " + fileName);
        }
        return Files.readAllBytes(imagePath);
    }

    /**
     * 根据条件查询用户信息。
     *
     * @param user 查询条件
     * @return 匹配的用户列表
     */
    @Override
    public List<User> getUserInfo(User user) {
        logger.info("根据条件查询用户信息");
        return userMapper.find(user);
    }

    @Override
    public User findById(Integer userId) {
        return userMapper.findById(userId);
    }

    @Override
    public User findByName(String owner) {
        return userMapper.findByName(owner);
    }
}