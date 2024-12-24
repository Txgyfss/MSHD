package com.example.controller;

import com.example.ApiResponse;
import com.example.entity.Disaster;
import com.example.service.DisasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController

@CrossOrigin(origins = "http://localhost:8081")
public class DisasterController {

    @Autowired
    private DisasterService disasterService;

    // 获取灾情分类列表 (模拟从数据库查询)
    @GetMapping("/disaster/types")
    public ApiResponse<List<String>> getDisasterTypes() {
        // 模拟数据，返回灾情类型列表
        List<String> disasterTypes = Arrays.asList("洪水", "地震", "火灾");
        return ApiResponse.success(disasterTypes);
    }

    // 获取灾情地点列表 (模拟从数据库查询)
    @GetMapping("/disaster/locations")
    public ApiResponse<List<String>> getLocations() {
        // 模拟数据，返回灾情地点列表
        List<String> locations = Arrays.asList("地区1", "地区2", "地区3");
        return ApiResponse.success(locations);
    }

    // 获取灾情来源列表 (模拟从数据库查询)
    @GetMapping("/disaster/sources")
    public ApiResponse<List<String>> getSources() {
        // 模拟数据，返回灾情来源列表
        List<String> sources = Arrays.asList("来源1", "来源2", "来源3");
        return ApiResponse.success(sources);
    }

    // 获取灾情载体类型列表 (模拟从数据库查询)
    @GetMapping("/disaster/carriers")
    public ApiResponse<List<String>> getCarriers() {
        // 模拟数据，返回灾情载体类型列表
        List<String> carriers = Arrays.asList("载体类型1", "载体类型2", "载体类型3");
        return ApiResponse.success(carriers);
    }

    // 获取灾情指标分类列表 (模拟从数据库查询)
    @GetMapping("/disaster/indicators")
    public ApiResponse<List<String>> getIndicators() {
        // 模拟数据，返回灾情指标分类列表
        List<String> indicators = Arrays.asList("指标类型1", "指标类型2", "指标类型3");
        return ApiResponse.success(indicators);
    }

    // 新增或更新灾情信息并进行编码和解码
    @PostMapping("/upload")
    public ApiResponse<String> uploadDisasterInfo(@RequestBody Disaster disaster) {
        try {
            // 解码灾情信息
            Disaster decodedDisaster = disasterService.decodeDisasterInfo(disaster.getDisasterCode());

            // 处理解码后的灾情信息，保存到数据库
            disasterService.saveDisaster(decodedDisaster);

            // 进行编码操作（如果需要返回编码后的灾情码等）
            String encodedDisasterCode = disasterService.encodeDisasterInfo(decodedDisaster);

            // 返回成功信息，包含编码后的灾情码
            return ApiResponse.success("灾情信息上传成功，编码后的灾情码: " + encodedDisasterCode);
        } catch (Exception e) {
            // 异常处理，返回失败的 API 响应
            return ApiResponse.failure(500, "灾情信息上传失败: " + e.getMessage());
        }
    }
}
