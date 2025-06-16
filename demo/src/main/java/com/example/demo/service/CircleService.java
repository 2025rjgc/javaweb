package com.example.demo.service;


import com.example.demo.entity.Circle;
import com.example.demo.entity.Members;

import java.util.List;



public interface CircleService {
    //获取圈子列表
    List<Circle> selectCircleList(Circle quanzi);

    //根据id获取圈子信息
    Circle getInfo(Integer id);

    //创建圈子
    void createCircle(Circle quanzi);

    //删除圈子
    void deleteCircle(Integer id);

    // 获取成员列表
    List<Members> getMembers(Integer id);

    // 获取邀请列表
    List<Members> getInvite(Integer id);

    // 邀请成员
    void inviteMember(Integer id,Members member);
}
