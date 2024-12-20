package com.example.controller;

import com.example.common.Result;
import com.example.entity.*;
import com.example.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*  描述：管理员相关接口
*/
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 描述：新增
     */
    @PostMapping
    public Result add(@RequestBody Admin admin) {

        adminService.add(admin);
        return Result.success(admin);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        adminService.delete(id);
        return Result.success();
    }

    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Admin admin) {

        adminService.update(admin);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Admin admin = adminService.findById(id);
        return Result.success(admin);
    }

    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(adminService.findAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Admin search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(adminService.findPage(search, pageNum, pageSize));
    }




}
