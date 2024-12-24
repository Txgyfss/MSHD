package com.example.controller;

import com.example.common.Result;
import com.example.entity.*;
import com.example.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
*  描述：商品管理相关接口
*/
@RestController
@RequestMapping(value = "/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 描述：新增
     */
    @PostMapping
    public Result add(@RequestBody Goods goods) {

        goodsService.add(goods);
        return Result.success(goods);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        goodsService.delete(id);
        return Result.success();
    }

    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Goods goods) {

        goodsService.update(goods);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Goods goods = goodsService.findById(id);
        return Result.success(goods);
    }

    /**
     * 描述：根据分类ID查询
     */
    @GetMapping("/type/{id}")
    public Result type(@PathVariable Integer id) {
        List<Goods> list = goodsService.findByFlId(id);
        return Result.success(list);
    }

    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(goodsService.findAll());
    }
    /**
     * 描述：查询所有热门商品
     */
    @GetMapping("/hotgoods")
    public Result hotall() {
        List<Goods> collect = goodsService.findAll().stream().filter(item -> "是".equals(item.getIshot())).collect(Collectors.toList());
        return Result.success(collect);
    }
    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Goods search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(goodsService.findPage(search, pageNum, pageSize));
    }

    /**
     * 描述：分页查询所有热门商品
     */
    @PostMapping("/hot/page")
    public Result hotpage(@RequestBody Goods search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(goodsService.findHotPage(search, pageNum, pageSize));
    }

    /**
     * 描述：搜索
     */
    @GetMapping("/search/{name}")
    public Result search(@PathVariable String name) {
        return Result.success(goodsService.search(name));
    }
}
