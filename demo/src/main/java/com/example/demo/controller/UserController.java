package com.example.demo.controller;

import com.example.demo.entity.Result;
import com.example.demo.entity.User;
import com.example.demo.filter.GetContentType;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户控制器，处理用户登录、注册、信息更新、头像上传等请求。
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录接口。
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户信息和 token，失败返回错误信息
     */
    @PostMapping("/login")
    public Result login(String username, String password) {
        logger.info("用户尝试登录: {}", username);
        Object login_data = userService.login(username, password);
        if (login_data != null) {
            logger.info("登录成功: {}", username);
            return Result.success(login_data);
        } else {
            logger.warn("用户名或密码错误: {}", username);
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 用户注册接口。
     *
     * @param user 用户对象
     * @return 注册成功返回用户ID和用户名，失败返回错误信息
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        logger.info("开始注册用户: {}", user.getUsername());
        Object register_data = userService.register(user);
        if (register_data != null) {
            logger.info("注册成功: {}", user.getUsername());
            return Result.success(register_data);
        } else {
            logger.warn("注册失败: {}", user.getUsername());
            return Result.error("注册失败");
        }
    }

    /**
     * 根据条件查询用户信息。
     *
     * @param user 查询条件（如用户名、角色等）
     * @return 匹配的用户列表，若无匹配则返回错误信息
     */
    @PostMapping("/getUserInfo")
    public Result getAllUserInfo(@RequestBody User user) {
        logger.info("根据条件查询用户信息: {}", user);
        List<User> userList = userService.getUserInfo(user);
        if (userList != null && !userList.isEmpty()) {
            logger.info("查询成功: {}", userList);
            return Result.success(userList);
        } else {
            logger.warn("未找到符合条件的用户: {}", user);
            return Result.error("用户不存在");
        }
    }

    /**
     * 修改用户信息接口。
     *
     * @param user 用户对象
     * @return 修改成功返回 success，失败返回错误信息
     */
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody User user) {
        logger.info("修改用户信息: {}", user.getUsername());
        boolean success = userService.updateUserInfo(user);
        if (success) {
            return Result.success();
        } else {
            logger.warn("修改用户信息失败: {}", user.getUsername());
            return Result.error("修改失败");
        }
    }

    /**
     * 删除用户接口。
     *
     * @param username 用户名
     * @return 删除成功返回 success，失败返回错误信息
     */
    @PostMapping("/deleteUser")
    public Result deleteUser(String username) {
        logger.info("删除用户: {}", username);
        boolean success = userService.deleteUser(username);
        if (success) {
            return Result.success();
        } else {
            logger.warn("删除用户失败: {}", username);
            return Result.error("删除失败");
        }
    }

    /**
     * 设置用户头像接口。
     *
     * @param file     头像文件
     * @param filename 文件名（含用户ID）
     * @return 上传成功返回 success，失败返回错误信息
     */
    @PostMapping("/setImage")
    public Result setImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename) {
        logger.info("设置用户头像: {}", filename);

        if (file.isEmpty()) {
            logger.warn("上传文件为空");
            return Result.error("文件为空");
        }

        if (filename == null || filename.trim().isEmpty()) {
            logger.warn("文件名为空");
            return Result.error("文件名为空");
        }

        // 文件类型检查
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !(originalFilename.toLowerCase().endsWith(".png")
                || originalFilename.toLowerCase().endsWith(".jpg")
                || originalFilename.toLowerCase().endsWith(".jpeg"))) {
            logger.warn("不允许的文件类型: {}", filename);
            return Result.error("不允许的文件类型");
        }

        // 文件大小限制（5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            logger.warn("文件过大: {}", filename);
            return Result.error("文件大小不能超过5MB");
        }

        if (!userService.updateUserImage(file, filename)) {
            logger.error("上传头像失败: {}", filename);
            return Result.error("上传失败");
        }

        return Result.success("上传成功");
    }

    /**
     * 获取用户头像接口。
     *
     * @param fileName 文件名
     * @return 返回图片字节流或错误信息
     */
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            logger.info("获取用户头像: {}", fileName);
            byte[] imageBytes = userService.getUserImage(fileName);

            String contentType = GetContentType.getContentTypeByExtension(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + fileName + "\"")
                    .body(imageBytes);
        } catch (IOException e) {
            logger.error("获取头像失败: {}", fileName, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}