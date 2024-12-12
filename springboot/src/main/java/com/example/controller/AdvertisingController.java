package com.example.controller;

import com.example.common.Result;
import com.example.entity.Advertising;
import com.example.service.AdvertisingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
*  描述：广告位展示管理相关接口
*/
@RestController
@RequestMapping(value = "/advertising")
public class AdvertisingController {

    @Resource
    private AdvertisingService advertisingService;

    /**
     * 描述：新增
     */
    @PostMapping
    public Result add(@RequestBody Advertising advertising) {

        advertisingService.add(advertising);
        return Result.success(advertising);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        advertisingService.delete(id);
        return Result.success();
    }

    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Advertising advertising) {

        advertisingService.update(advertising);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Advertising advertising = advertisingService.findById(id);
        return Result.success(advertising);
    }


    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(advertisingService.findAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Advertising search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(advertisingService.findPage(search, pageNum, pageSize));
    }


}
