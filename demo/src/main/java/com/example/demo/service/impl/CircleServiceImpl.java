package com.example.demo.service.impl;


import com.example.demo.entity.Circle;
import com.example.demo.entity.Members;
import com.example.demo.mapper.CircleMapper;
import com.example.demo.service.CircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CircleServiceImpl implements CircleService {
    @Autowired
    private CircleMapper circleMapper;

    //  查询所有圈子
    @Override
    public List<Circle> selectCircleList(Circle circle) {
        return circleMapper.selectCircleList(circle);
    }

    //  根据id查询圈子信息
    @Override
    public Circle getInfo(Integer id) {
        return circleMapper.getInfo(id);
    }

    // 根据用户id查询圈子信息
    @Override
    public List<Circle> selectCircleByUserId(Integer userId) {
        return circleMapper.selectCircleByUserId(userId);
    }

    //  删除圈子
    @Override
    public void deleteCircle(Integer id) {
        //先删除用户圈子关系
        circleMapper.deleteUserCircle(id);

        //删除圈子以及帖子、评论
        circleMapper.deleteCircle(id);
    }

    //  创建圈子
    @Transactional //开启事务回滚
    @Override
    public void createCircle(Circle circle) {
        // 设置初始成员数和帖子数
        circle.setMembers(1);
        circle.setPosts(0);
        String  username=circle.getOwner();
        String  title=circle.getTitle();

        // 插入数据库
        circleMapper.createCircle(circle);
        // 更新导师的circle_id
        Integer circle_id = circleMapper.selectId(username, title);
        if (circle_id == null || username.isEmpty()){
            throw new RuntimeException("用户不存在");
        }
        int rowsAffected = circleMapper.updateUserCircleId(circle_id, username);
        if (rowsAffected == 0) {
            throw new RuntimeException("更新用户 circle_id 失败"); // 主动抛出运行时异常
        }
    }

    //查询圈子成员
    @Override
    public List<Members> getMembers(Integer id) {
        return circleMapper.selectMembersList(id);
    }

    //查询可以邀请成员
    @Override
    public List<Members> getInvite(Integer id) {
        // 根据圈子id查询没有加入圈子的成员
        return circleMapper.selectNoMembersList(id);
    }
    //邀请成员
    @Override
    public void inviteMember(Integer id,  Members member) {
        Integer userId=member.getUserId();
        circleMapper.inviteMember(id,userId);
        int memberCount = circleMapper.countMembersByCircleId(id);
        circleMapper.updateMembers(id, memberCount);
    }

    //删除成员
    @Override
    public void deleteMember(Integer userId) {
        circleMapper.deleteMember(userId);
        int memberCount = circleMapper.countMembersByCircleId(userId);
        circleMapper.updateMembers(userId, memberCount);
    }

}
