package com.example.controller;

import com.example.ApiResponse;
import com.example.entity.Disaster;
import com.example.service.DisasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RestController
public class DisasterController {

    @Autowired
    private DisasterService disasterService;

    // 获取灾情分类列表
    @GetMapping("/disaster/types")
    public ApiResponse<List<String>> getDisasterTypes() {
        List<String> disasterTypes = disasterService.getDisasterTypes();
        return ApiResponse.success(disasterTypes);
    }

    // 获取灾情地点列表
    @GetMapping("/disaster/locations")
    public ApiResponse<List<String>> getLocations() {
        List<String> locations = disasterService.getLocations();
        return ApiResponse.success(locations);
    }

    // 获取灾情来源列表
    @GetMapping("/disaster/sources")
    public ApiResponse<List<String>> getSources() {
        List<String> sources = disasterService.getSources();
        return ApiResponse.success(sources);
    }

    // 获取灾情载体类型列表
    @GetMapping("/disaster/carriers")
    public ApiResponse<List<String>> getCarriers() {
        List<String> carriers = disasterService.getCarriers();
        return ApiResponse.success(carriers);
    }

    // 获取灾情指标分类列表
    @GetMapping("/disaster/indicators")
    public ApiResponse<List<String>> getIndicators() {
        List<String> indicators = disasterService.getIndicators();
        return ApiResponse.success(indicators);
    }

    // 新增或更新灾情信息
    @PostMapping("/upload")
    public ApiResponse<String> uploadDisasterInfo(@RequestBody Disaster disaster) {
        // 解码灾情信息
        Disaster decodedDisaster = disasterService.decodeDisasterInfo(disaster.getDisasterCode());

        // 处理解码后的灾情信息（比如保存到数据库）
        disasterService.saveDisaster(decodedDisaster);

        // 进行编码操作（如果需要返回编码后的灾情码等）
        String encodedDisasterCode = disasterService.encodeDisasterInfo(decodedDisaster);

        return ApiResponse.success("灾情信息上传成功，编码后的灾情码: " + encodedDisasterCode);
    }

}
