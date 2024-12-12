package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.*;
import com.example.service.GoodsService;
import com.example.service.OrderService;
import com.example.service.TypeService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*  描述：订单相关接口
*/
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private TypeService typeService;

    /**
     * 描述：购买
     */
    @PostMapping("/buy")
    public Result add(@RequestBody OrderDto order) {
        orderService.add(order);
        return Result.success();
    }

    /**
     * 描述：购买一个
     */
    @PostMapping("/buy/one")
    public Result add(@RequestBody Order order) {
        orderService.addOne(order);
        return Result.success();
    }
    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        orderService.delete(id);
        return Result.success();
    }

    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Order order) {

        orderService.update(order);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Order order = orderService.findById(id);
        return Result.success(order);
    }

    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(orderService.findAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Order search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(orderService.findPage(search, pageNum, pageSize));
    }

}
