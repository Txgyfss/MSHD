package com.example.controller;

import com.example.common.Result;
import com.example.entity.Cart;
import com.example.service.CartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*  描述：购物车相关接口
*/
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Resource
    private CartService cartService;

    /**
     * 描述：新增
     */
    @PostMapping
    public Result add(@RequestBody Cart cart) {
        cartService.add(cart);
        return Result.success(cart);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        cartService.delete(id);
        return Result.success();
    }

    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Cart cart) {

        cartService.update(cart);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Cart cart = cartService.findById(id);
        return Result.success(cart);
    }

    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(cartService.findAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Cart search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(cartService.findPage(search, pageNum, pageSize));
    }




}
