package com.example.service;

import com.example.entity.Disaster;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
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
        return Arrays.asList("文字", "图像", "音频","视频","其他");
    }

    public List<String> getIndicators() {
        return Arrays.asList("温度", "降水量", "风速", "辐射强度");
    }

    // 模拟数据库保存灾情信息（实际项目中可以通过JPA或MyBatis等进行数据库操作）
    public void saveDisaster(Disaster disaster) {
        // 假设此处将灾情信息存储到数据库
        System.out.println("保存灾情信息: " + disaster);
    }

    // 编码操作（这里的实现你可以根据需求稍后补充）
    public String encodeDisasterInfo(Disaster disaster) {
        // 编码逻辑
        // 返回编码后的灾情信息（暂时返回一个示例）
        return disaster.getDisasterCode();
    }

    // 解码操作（这里的实现你可以根据需求稍后补充）
    public Disaster decodeDisasterInfo(String disasterCode) {
        // 解码逻辑
        // 返回解码后的灾情信息对象（暂时返回一个示例）
        Disaster disaster = new Disaster();
        disaster.setDisasterCode(disasterCode);
        disaster.setLocation("示例地点");
        disaster.setTime(new Date());
        disaster.setSource("示例来源");
        disaster.setCarrier("示例载体");
        disaster.setDisasterType("示例类型");
        disaster.setIndicator("示例指标");
        disaster.setDescription("示例描述");
        return disaster;
    }
}
