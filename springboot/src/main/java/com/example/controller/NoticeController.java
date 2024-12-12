package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*  描述：系统公告相关接口
*/
@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 描述：新增
     */
    @PostMapping
    public Result add(@RequestBody Notice notice) {
        noticeService.add(notice);
        return Result.success(notice);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        noticeService.delete(id);
        return Result.success();
    }

    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Notice notice) {

        noticeService.update(notice);
        return Result.success();
    }

    /**
     * 描述：查询最新三条
     */
    @GetMapping
    public Result all() {
        return Result.success(noticeService.findAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Notice search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(noticeService.findPage(search, pageNum, pageSize));
    }

}
