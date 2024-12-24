package com.example.controller;

import com.example.common.Result;
import com.example.service.SysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/areas")
public class SysAreaController {

    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 获取省列表
     */
    @GetMapping("/getProvinces")
    public Result getProvinces() {
        return Result.success(sysAreaService.getProvinces());
    }

    /**
     * 获取市列表
     * @param provinceCode 省编码
     */
    @GetMapping("/getCities")
    public Result getCities(@RequestParam String provinceCode) {
        return Result.success(sysAreaService.getCities(provinceCode));
    }

    /**
     * 获取县列表
     * @param cityCode 市编码
     */
    @GetMapping("/getCounties")
    public Result getCounties(@RequestParam String cityCode) {
        return Result.success(sysAreaService.getCounties(cityCode));
    }

    /**
     * 获取乡列表
     * @param areaCode 县编码
     */
    @GetMapping("/getTowns")
    public Result getTowns(@RequestParam String areaCode) {
        return Result.success(sysAreaService.getTowns(areaCode));
    }

    /**
     * 获取村列表
     * @param streetCode 乡编码
     */
    @GetMapping("/getVillages")
    public Result getVillages(@RequestParam String streetCode) {
        return Result.success(sysAreaService.getVillages(streetCode));
    }
}
