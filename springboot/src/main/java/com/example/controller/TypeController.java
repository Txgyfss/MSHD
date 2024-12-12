package com.example.controller;

import com.example.common.Result;
import com.example.entity.*;
import com.example.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*  描述：商品分类相关接口
*/
@RestController
@RequestMapping(value = "/type")
public class TypeController {

    @Resource
    private TypeService typeService;

    /**
     * 描述：新增
     */
    @PostMapping
    public Result add(@RequestBody Type type) {

        typeService.add(type);
        return Result.success(type);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        typeService.delete(id);
        return Result.success();
    }

    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Type type) {

        typeService.update(type);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Type type = typeService.findById(id);
        return Result.success(type);
    }

    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(typeService.findAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Type search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(typeService.findPage(search, pageNum, pageSize));
    }




}
