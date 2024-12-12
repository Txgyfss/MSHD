package com.example.controller;

import com.example.common.Result;
import com.example.entity.Banner;
import com.example.service.BannerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*  描述：广告位展示管理相关接口
*/
@RestController
@RequestMapping(value = "/banner")
public class BannerController {

    @Resource
    private BannerService bannerService;

    /**
     * 描述：新增
     */
    @PostMapping
    public Result add(@RequestBody Banner banner) {

        bannerService.add(banner);
        return Result.success(banner);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        bannerService.delete(id);
        return Result.success();
    }

    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Banner banner) {

        bannerService.update(banner);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Banner banner = bannerService.findById(id);
        return Result.success(banner);
    }


    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(bannerService.findAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Banner search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(bannerService.findPage(search, pageNum, pageSize));
    }


}
