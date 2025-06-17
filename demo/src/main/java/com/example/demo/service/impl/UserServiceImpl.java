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


import static com.example.demo.utils.FileTools.isImageFile;

/**
 * 用户服务实现类，包含用户注册、登录、信息更新等业务逻辑。
 */
@Service
public class UserServiceImpl implements UserService {


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

        List<User> userData = userMapper.findByUsernameAndPassword(username, password);
        if (userData.isEmpty()) {
            return null;
        }

        User user = userData.get(0);
        String JWT_token = JWTUtils.createJWT(username, password, 1000 * 60 * 60 * 24 * 7); // 7天有效期

        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", user.getUserId());
        map.put("username", user.getUsername());
        map.put("role", user.getRole());
        map.put("token", JWT_token);

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

        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            return null;
        }

        if (!userMapper.find(user).isEmpty()) {
            return null;
        }

        user.setRole(0);
        user.setAvatar(accessDir + "/" + "default.png"); // 设置默认头像
        userMapper.insert(user);

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", user.getUserId());
        map.put("username", user.getUsername());

        return map;
    }

    /**
     * 获取所有用户信息。
     *
     * @return 用户列表
     */
    @Override
    public List<User> getAllUserInfo() {
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

            // 文件类型检查
            if (!isImageFile(file)) {
                return false;
            }

            // 文件大小限制（5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
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
            // 保存文件
            Files.write(filePath, file.getBytes());

            // 更新数据库中的头像路径
            User user = new User();
            user.setUserId(Integer.parseInt(userId));
            user.setAvatar(accessDir + "/" + filename);
            userMapper.updateUserInfo(user);

            return true;

        } catch (IOException e) {
            return false;
        }
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
        Path imagePath = Paths.get(uploadDir, fileName);
        if (!Files.exists(imagePath)) {
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