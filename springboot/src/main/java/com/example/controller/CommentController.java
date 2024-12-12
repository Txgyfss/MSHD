package com.example.controller;

import com.example.common.Result;
import com.example.entity.Comment;
import com.example.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*  描述：评论相关接口
*/
@RestController
@RequestMapping(value = "/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 描述：新增
     */
    @PostMapping
    public Result add(@RequestBody Comment comment) {
        commentService.add(comment);
        return Result.success(comment);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        commentService.delete(id);
        return Result.success();
    }

    /**
     * 描述：查询所有
     */
    @GetMapping("/{goodsId}")
    public Result all(@PathVariable Integer goodsId) {
        return Result.success(commentService.findAll(goodsId));
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Comment search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(commentService.findPage(search, pageNum, pageSize));
    }
}
