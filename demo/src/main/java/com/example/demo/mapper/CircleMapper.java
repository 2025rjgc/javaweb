package com.example.demo.mapper;

import com.example.demo.entity.Circle;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CircleMapper {

    List<Circle> selectQuanziList(Circle circle);

    @Select("select * from circles where id = #{id}")
    Circle getInfo(Integer id);

    @Insert("INSERT INTO circles (title, owner, cover, members, posts) " +
            "VALUES (#{title}, #{owner}, #{cover}, #{members}, #{posts})")
    void createCircle(Circle circle);

    @Delete("delete from circles where id = #{id}")
    void deleteCircle(Integer id);
}
