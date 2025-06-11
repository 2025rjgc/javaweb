package com.example.demo.service.impl;

import com.example.demo.mapper.PostMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.Post;
import com.example.demo.pojo.User;
import com.example.demo.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 帖子服务实现类，提供书评帖子的增删改查功能。
 */
@Service
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Value("${app.postTxt-upload-dir}")
    private String postTxtUploadDir;

    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Autowired
    public PostServiceImpl(PostMapper postMapper, UserMapper userMapper) {
        this.postMapper = postMapper;
        this.userMapper = userMapper;
    }

    /**
     * 创建一个新的书评帖子。
     *
     * @param post 包含用户ID、图书ID、标题、内容等字段的帖子对象
     * @return 创建成功返回包含帖子ID的对象，失败返回 null
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Post createPost(Post post) {
        try {
            logger.info("尝试创建新书评: {}", post);

            if (post == null || post.getUserId() == null || post.getUserId() <= 0) {
                logger.warn("无效的用户ID");
                return null;
            }

            // 查询用户是否存在
            User userCriteria = new User();
            userCriteria.setUserId(post.getUserId());
            List<User> users = userMapper.find(userCriteria);
            if (users == null || users.isEmpty()) {
                logger.warn("用户不存在，无法发帖");
                return null;
            }

            User user = users.get(0);
            post.setUserId(user.getUserId());

            // 保存原始内容到文件
            String originalContent = post.getContent();
            String fileName = "post_" + System.currentTimeMillis() + ".txt";
            String filePath = "uploads/text/post/" + fileName;
            
            try {
                java.nio.file.Path dirPath = java.nio.file.Paths.get("uploads/text/post");
                if (!java.nio.file.Files.exists(dirPath)) {
                    java.nio.file.Files.createDirectories(dirPath);
                }
                java.nio.file.Files.write(java.nio.file.Paths.get(filePath), originalContent.getBytes());
                
                // 将文件URL存入content字段
                post.setContent("/post/content?file=" + fileName);
                
                postMapper.insertPost(post);
                logger.info("书评创建成功，postId={}", post.getPostId());
                return post;
            } catch (Exception e) {
                logger.error("保存内容文件失败", e);
                return null;
            }

        } catch (Exception e) {
            logger.error("创建书评时发生异常", e);
            throw e; // 抛出异常以便事务回滚
        }
    }

    /**
     * 根据书籍ID获取所有相关的书评帖子。
     *
     * @param bookId 书籍ID
     * @return 返回该书籍对应的所有书评列表
     */
    @Override
    public List<Post> getPostsByBookId(Integer bookId) {
        try {
            logger.info("获取书籍 [{}] 的书评", bookId);

            if (bookId == null || bookId <= 0) {
                logger.warn("无效的书籍ID");
                return List.of(); // 返回空列表而不是 null
            }

            List<Post> posts = postMapper.selectPostsByBookId(bookId);
            logger.info("找到 {} 条书评", Optional.ofNullable(posts).map(List::size).orElse(0));
            return posts != null ? posts : List.of();

        } catch (Exception e) {
            logger.error("获取书评列表时发生异常", e);
            return List.of();
        }
    }

    /**
     * 删除指定ID的书评。
     *
     * @param postId 书评ID
     * @return 删除成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePost(Integer postId) {
        try {
            logger.info("尝试删除书评: postId={}", postId);

            if (postId == null || postId <= 0) {
                logger.warn("无效的书评ID");
                return false;
            }

            // 先获取post记录以获取文件路径
            Post post = postMapper.selectPostById(postId);
            if (post == null) {
                logger.warn("书评不存在");
                return false;
            }

            // 删除关联的文件
            String contentUrl = post.getContent();
            if (contentUrl != null && contentUrl.startsWith("/post/content?file=")) {
                String fileName = contentUrl.substring("/post/content?file=".length());
                java.nio.file.Path filePath = java.nio.file.Paths.get(postTxtUploadDir + "/" + fileName);
                try {
                    java.nio.file.Files.deleteIfExists(filePath);
                    logger.info("删除关联文件: {}", filePath);
                } catch (Exception e) {
                    logger.error("删除文件失败", e);
                }
            }

            int rowsAffected = postMapper.deletePostById(postId);
            if (rowsAffected <= 0) {
                logger.warn("删除失败，可能书评不存在");
                return false;
            }

            logger.info("书评删除成功: postId={}", postId);
            return true;

        } catch (Exception e) {
            logger.error("删除书评时发生异常", e);
            throw e;
        }
    }

    /**
     * 更新已有的书评内容。
     *
     * @param post 包含 postId、title、content 等字段的书评对象
     * @return 修改成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePost(Post post) {
        try {
            logger.info("尝试修改书评: {}", post);

            if (post == null || post.getPostId() == null || post.getPostId() <= 0) {
                logger.warn("缺少必要参数：postId");
                return false;
            }

            // 获取旧post记录以获取旧文件路径
            Post oldPost = postMapper.selectPostById(post.getPostId());
            if (oldPost == null) {
                logger.warn("书评不存在");
                return false;
            }

            // 保存新内容到文件
            String originalContent = post.getContent();
            String fileName = "post_" + System.currentTimeMillis() + ".txt";
            String filePath = postTxtUploadDir + "/" + fileName;
            
            try {
                java.nio.file.Path dirPath = java.nio.file.Paths.get(postTxtUploadDir);
                if (!java.nio.file.Files.exists(dirPath)) {
                    java.nio.file.Files.createDirectories(dirPath);
                }
                java.nio.file.Files.write(java.nio.file.Paths.get(filePath), originalContent.getBytes());
                
                // 更新content字段为新文件URL
                post.setContent("/post/content?file=" + fileName);
                
                // 删除旧文件
                String oldContentUrl = oldPost.getContent();
                if (oldContentUrl != null && oldContentUrl.startsWith("/post/content?file=")) {
                    String oldFileName = oldContentUrl.substring("/post/content?file=".length());
                    java.nio.file.Path oldFilePath = java.nio.file.Paths.get(postTxtUploadDir + "/" + oldFileName);
                    try {
                        java.nio.file.Files.deleteIfExists(oldFilePath);
                        logger.info("删除旧文件: {}", oldFilePath);
                    } catch (Exception e) {
                        logger.error("删除旧文件失败", e);
                    }
                }
            } catch (Exception e) {
                logger.error("保存新内容文件失败", e);
                return false;
            }

            int rowsAffected = postMapper.updatePost(post);
            if (rowsAffected <= 0) {
                logger.warn("更新失败，可能书评不存在");
                return false;
            }

            logger.info("书评修改成功: postId={}", post.getPostId());
            return true;

        } catch (Exception e) {
            logger.error("修改书评时发生异常", e);
            throw e;
        }
    }

    /**
     * 获取指定ID的书评详情。
     *
     * @param postId 书评ID
     * @return 成功返回书评对象，失败返回 null
     */
    @Override
    public Post getPostById(Integer postId) {
        try {
            logger.info("获取书评详情: postId={}", postId);

            if (postId == null || postId <= 0) {
                logger.warn("无效的书评ID");
                return null;
            }

            Post post = postMapper.selectPostById(postId);
            if (post == null) {
                logger.warn("未找到该书评");
                return null;
            }
            logger.info("找到书评: {}", post);
            return post;

        } catch (Exception e) {
            logger.error("获取书评详情时发生异常", e);
            return null;
        }
    }
}