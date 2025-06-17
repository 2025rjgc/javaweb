package com.example.demo.controller;
import com.example.demo.entity.Circle;
import com.example.demo.entity.Members;
import com.example.demo.entity.Result;
import com.example.demo.service.CircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        return Result.success(list);
    }
    // 获取指定圈子信息
    @GetMapping("/{id}")
    public Result getCircleById(@PathVariable Integer id){
        Circle quanzi= circleService.getInfo(id);
        return Result.success(quanzi);
    }

    //根据用户id查询圈子
    @GetMapping("/user/{userId}")
    public Result getCircleByUserId(@PathVariable Integer userId){
        List<Circle> list = circleService.selectCircleByUserId(userId);
        return Result.success(list);
    }

    // 创建圈子
    @PostMapping
    public Result createCircle(@RequestBody Circle quanzi)
    {
         circleService.createCircle(quanzi);
         return Result.success();
    }
    // 删除圈子
    @DeleteMapping("/{id}")
    public Result deleteCircle(@PathVariable Integer id){
         circleService.deleteCircle(id);
         return Result.success();
    }

    //查询圈子成员
    @GetMapping("/{id}/members")
    public Result getMembers(@PathVariable Integer id){
        List<Members> list = circleService.getMembers(id);
        return Result.success(list);
    }

    //查询非圈子且无圈子成员
    @GetMapping("/{id}/users")
    public Result getInvite(@PathVariable Integer id){
        List<Members> list = circleService.getInvite(id);
        return Result.success(list);
    }

    //邀请成员
    @PostMapping("/{id}/invite")
    public Result inviteMember(@PathVariable Integer id,@RequestBody Members member){
        circleService.inviteMember(id,member);
        return Result.success();
    }

    // 删除成员
    @DeleteMapping("/members/{userId}")
    public Result deleteMember(@PathVariable Integer userId){
        circleService.deleteMember(userId);
        return Result.success();
    }
}
