package com.example.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DisasterService {

    // 假设这些数据来自数据库或者其他外部数据源
    public List<String> getDisasterTypes() {
        return Arrays.asList("自然灾害", "人为灾害", "公共卫生事件");
    }

    public List<String> getLocations() {
        return Arrays.asList("北京", "上海", "广州", "深圳", "武汉");
    }

    public List<String> getSources() {
        return Arrays.asList("政府", "媒体", "民间");
    }

    public List<String> getCarriers() {
        return Arrays.asList("卫星", "气象站", "互联网");
    }

    public List<String> getIndicators() {
        return Arrays.asList("温度", "降水量", "风速", "辐射强度");
    }
}
