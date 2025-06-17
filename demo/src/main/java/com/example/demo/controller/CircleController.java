package com.example.demo.controller;
import com.example.demo.entity.Circle;
import com.example.demo.entity.Members;
import com.example.demo.entity.Result;
import com.example.demo.service.CircleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/circles")
public class CircleController {
   @Autowired
   private CircleService circleService;

    // 获取所有圈子信息
    @GetMapping
    public  Result getAllCircles(Circle quanzi){
        List<Circle> list = circleService.selectCircleList(quanzi);
        log.info("获取圈子列表：{}",list);
        return Result.success(list);
    }
    // 获取指定圈子信息
    @GetMapping("/{id}")
    public Result getCircleById(@PathVariable Integer id){
        Circle quanzi= circleService.getInfo(id);
        return Result.success(quanzi);
    }
    //  创建圈子
    @PostMapping
    public Result createCircle(@RequestBody Circle quanzi)
    {
         log.info("创建圈子：{}",quanzi);
         circleService.createCircle(quanzi);
         return Result.success();
    }
    //删除圈子
    @DeleteMapping("/{id}")
    public Result deleteCircle(@PathVariable Integer id){
         circleService.deleteCircle(id);
         return Result.success();
    }

    //查询圈子成员
    @GetMapping("/{id}/members")
    public Result getMembers(@PathVariable Integer id){
        log.info("查询圈子成员：{}",id);
        List<Members> list = circleService.getMembers(id);
        log.info("获取成员列表：{}",list);
        return Result.success(list);
    }

    //查询非圈子且无圈子成员
    @GetMapping("/{id}/users")
    public Result getInvite(@PathVariable Integer id){
        List<Members> list = circleService.getInvite(id);
        log.info("获取邀请列表：{}",list);
        return Result.success(list);
    }

    //邀请成员
    @PostMapping("/{id}/invite")
    public Result inviteMember(@PathVariable Integer id,@RequestBody Members member){
        log.info("邀请成员：{},{}",id,member);
        circleService.inviteMember(id,member);
        return Result.success();
    }
}
