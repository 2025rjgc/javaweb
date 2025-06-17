package com.example.demo.mapper;


import com.example.demo.entity.Circle;
import com.example.demo.entity.Members;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CircleMapper {

    //查询所有圈子
    List<Circle> selectCircleList(Circle circle);

    //更新导师圈子id
    int updateUserCircleId(@Param("circle_id") Integer circle_id,@Param("username") String username);

    //删除圈子
    void deleteCircle(Integer id);

    //查询圈子信息
    @Select("select * from circles where id = #{id}")
    Circle getInfo(Integer id);

    //创建圈子
    @Insert("INSERT INTO circles (title, owner, cover, members, posts) " +
            "VALUES (#{title}, #{owner}, #{cover}, #{members}, #{posts})")
    void createCircle(Circle circle);

    // 删除圈子内所有用户
    @Update("update user set circle_id = null where circle_id = #{id}")
    void deleteUserCircle(Integer id);

    //查询圈子成员
    @Select("select * from user where circle_id = #{id}")
    List<Members> selectMembersList(Integer id);

    //查询可邀请成员
    @Select("select user_id as userId,username from user where (circle_id is null or circle_id != #{id}) and role=0")
    List<Members> selectNoMembersList(Integer id);

    //邀请成员,更改用户的circle_id
    @Update("update user set circle_id = #{id} where user_id = #{userId}")
    void inviteMember(@Param("id") Integer id,@Param("userId") Integer userId);

    //更新圈子内帖子数
    @Update("update circles set posts = posts + 1 where id = #{circleId}")
    void updatePosts(Integer circleId);

    //更新圈子内成员数
    @Update("update circles set members = members + 1 where id = #{id}")
    void updateMember(Integer id);

    //查询圈子id
    @Select("select id from circles where owner = #{username} and title = #{title}")
    Integer selectId(@Param("username") String username,@Param("title") String title);
}
