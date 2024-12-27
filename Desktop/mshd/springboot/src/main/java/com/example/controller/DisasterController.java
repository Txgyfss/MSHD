package com.example.controller;

import cn.hutool.json.JSONArray;
import com.example.ApiResponse;
import com.example.entity.Disaster;
import com.example.service.DisasterService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DisasterController {

    @Autowired
    private DisasterService disasterService;



    // 获取灾情地点列表
    @GetMapping("/disaster/locations")
    public ApiResponse<List<String>> getLocations() {
        List<String> locations = disasterService.getLocations();
        return ApiResponse.success(locations);
    }




    // 删除灾情信息
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteDisasterById(@PathVariable int id) {
        boolean success = disasterService.deleteDisasterById(id);
        if (success) {
            return ApiResponse.success("灾情信息删除成功");
        } else {
            return ApiResponse.error("灾情信息删除失败");
        }
    }

    // 查看所有灾情信息
    @GetMapping("/all")
    public ApiResponse<List<Map<String, Object>>> getAllDisasters() {
        return ApiResponse.success(disasterService.getAllDisasters());
    }

    // 筛选灾情信息
    @GetMapping("/filter")
    public ApiResponse<List<Map<String, Object>>> filterDisasters(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String carrierForm,
            @RequestParam(required = false) String sourceCategory,
            @RequestParam(required = false) String disasterMajorCategory,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        return ApiResponse.success(disasterService.filterDisasters(province, city, carrierForm,
                sourceCategory, disasterMajorCategory, startDate, endDate, sortOrder));
    }
    // 新增灾情信息
    @PostMapping("/add")
    public ApiResponse<String> addDisasterInfo(@RequestBody Disaster disaster) {
        // 输出接收到的 Disaster 对象
        System.out.println(disaster);
        String encodedDisasterCode;
        try {
            // 对灾情信息进行编码
            encodedDisasterCode = disasterService.encodeDisasterInfo(disaster);

            // 设置编码后的灾情码到灾情对象
            disaster.setDisastercode(encodedDisasterCode);

            // 保存编码后的灾情信息到数据库
            disasterService.saveDisaster(disaster);

        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            // 记录日志
            System.out.println("处理灾情信息时发生错误：" + e.getMessage());
            return ApiResponse.error("灾情信息上传失败：" + e.getMessage());
        }

        return ApiResponse.success("灾情信息上传成功，编码后的灾情码: " + encodedDisasterCode);
    }
    // 更新灾情信息
    @PostMapping("/update")
    public ApiResponse<String> updateDisasterInfo(@RequestBody Disaster disaster) {
        try {

            // 从数据库中获取当前灾情信息
            Disaster existingDisaster = disasterService.getDisasterById(Integer.parseInt(disaster.getId()));
            if (existingDisaster == null) {
                return ApiResponse.error("无法找到指定灾情信息，请检查灾情编码！");
            }
            existingDisaster.setId(disaster.getId());
            System.out.println("当前数据库中的灾情信息: " + existingDisaster);

            // 使用新上传的数据重新编码（新数据覆盖现有数据后生成编码）
            Disaster updatedDisaster = new Disaster();
            updatedDisaster.setId(existingDisaster.getId()); // 保留主键 ID 不变
            updatedDisaster.setProvince(disaster.getProvince() != null ? disaster.getProvince() : existingDisaster.getProvince());
            updatedDisaster.setCity(disaster.getCity() != null ? disaster.getCity() : existingDisaster.getCity());
            updatedDisaster.setCountry(disaster.getCountry() != null ? disaster.getCountry() : existingDisaster.getCountry());
            updatedDisaster.setTown(disaster.getTown() != null ? disaster.getTown() : existingDisaster.getTown());
            updatedDisaster.setVillage(disaster.getVillage() != null ? disaster.getVillage() : existingDisaster.getVillage());
            updatedDisaster.setDate(disaster.getDate() != null ? disaster.getDate() : existingDisaster.getDate());
            updatedDisaster.setLocation(disaster.getLocation() != null ? disaster.getLocation() : existingDisaster.getLocation());
            updatedDisaster.setDetail(disaster.getDetail() != null ? disaster.getDetail() : existingDisaster.getDetail());
            updatedDisaster.setReportingUnit(disaster.getReportingUnit() != null ? disaster.getReportingUnit() : existingDisaster.getReportingUnit());
            updatedDisaster.setSourceCategory(disaster.getSourceCategory() != null ? disaster.getSourceCategory() : existingDisaster.getSourceCategory());
            updatedDisaster.setSourceSubCategory(disaster.getSourceSubCategory() != null ? disaster.getSourceSubCategory() : existingDisaster.getSourceSubCategory());
            updatedDisaster.setCarrierForm(disaster.getCarrierForm() != null ? disaster.getCarrierForm() : existingDisaster.getCarrierForm());
            updatedDisaster.setLink(disaster.getLink() != null ? disaster.getLink() : existingDisaster.getLink());
            updatedDisaster.setDisasterMajorCategory(disaster.getDisasterMajorCategory() != null ? disaster.getDisasterMajorCategory() : existingDisaster.getDisasterMajorCategory());
            updatedDisaster.setDisasterSubCategory(disaster.getDisasterSubCategory() != null ? disaster.getDisasterSubCategory() : existingDisaster.getDisasterSubCategory());
            updatedDisaster.setDisasterIndicator(disaster.getDisasterIndicator() != null ? disaster.getDisasterIndicator() : existingDisaster.getDisasterIndicator());
            System.out.println("到这里没问题 ");
            // 根据新信息重新生成编码
            String updatedEncodedDisasterCode = disasterService.encodeDisasterInfo(updatedDisaster);
            System.out.println("到这里没问题1 ");
            updatedDisaster.setDisastercode(updatedEncodedDisasterCode);
            System.out.println("更新后的信息： "+updatedDisaster );
            // 更新到数据库（使用 UPDATE 而非 INSERT）
            System.out.println("准备更新到数据库 " );
            boolean isUpdated = disasterService.updateDisasterInDatabase(updatedDisaster);

            if (isUpdated) {
                return ApiResponse.success("灾情信息更新成功，编码后的灾情码: " + updatedEncodedDisasterCode);
            } else {
                return ApiResponse.error("灾情信息更新失败，数据库更新未成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常并返回错误信息
            return ApiResponse.error("灾情信息更新失败：" + e.getMessage());
        }
    }
    /**
     * 传入灾情码，解码生成灾情信息对象并存储到数据库
     */
    @PostMapping("/decode")
    public ApiResponse<Map<String, Object>> decodeAndSaveDisaster(@RequestBody Map<String, String> request) {
        try {
            // 从请求体中获取灾情编码
            String disasterCode = request.get("disasterCode");
            System.out.println("传入的编码: " + disasterCode);

            // 调用解码方法生成灾情对象
            Disaster disaster = disasterService.decodeDisasterInfo(disasterCode);
            disaster.setDisastercode(disasterCode);
            System.out.println("解码得到的对象: " + disaster);

            // 将灾情对象存储到数据库
            disasterService.saveDisasterCodeToDatabase(disaster);

            // 转换 Disaster 对象为 Map<String, Object>
            Map<String, Object> disasterData = new HashMap<>();
            disasterData.put("id", disaster.getId());
            disasterData.put("disastercode", disaster.getDisastercode());
            disasterData.put("province", disaster.getProvince());
            disasterData.put("city", disaster.getCity());
            disasterData.put("country", disaster.getCountry());
            disasterData.put("town", disaster.getTown());
            disasterData.put("village", disaster.getVillage());
            disasterData.put("date", disaster.getDate());
            disasterData.put("location", disaster.getLocation());
            disasterData.put("detail", disaster.getDetail());
            disasterData.put("reportingUnit", disaster.getReportingUnit());
            disasterData.put("sourceCategory", disaster.getSourceCategory());
            disasterData.put("sourceSubCategory", disaster.getSourceSubCategory());
            disasterData.put("carrierForm", disaster.getCarrierForm());
            disasterData.put("link", disaster.getLink());
            disasterData.put("disasterMajorCategory", disaster.getDisasterMajorCategory());
            disasterData.put("disasterSubCategory", disaster.getDisasterSubCategory());
            disasterData.put("disasterIndicator", disaster.getDisasterIndicator());

            // 返回成功响应，包含解码后的灾情信息
            return ApiResponse.success(disasterData);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("解码或保存灾情信息失败：" + e.getMessage());
        }
    }
    @PostMapping("/decodeFromFile")
    public ApiResponse<String> decodeFromFile(@RequestBody Map<String, String> request) {
        try {
            // 获取灾情编码和文件路径
            String disasterCode = request.get("disasterCode");
            String fileLink = request.get("fileLink");

            if (disasterCode == null || disasterCode.isEmpty()) {
                return ApiResponse.error("灾情编码为空，无法解码！");
            }

            if (fileLink == null || fileLink.isEmpty()) {
                return ApiResponse.error("文件路径为空，无法解码！");
            }

            // 根据灾情编码生成灾情对象
            Disaster disaster = disasterService.decodeDisasterInfo(disasterCode);
            disaster.setDisastercode(disasterCode);

            // 设置文件路径到灾情对象
            disaster.setLink(fileLink);

            // 保存灾情信息到数据库
            disasterService.saveDisasterCodeToDatabase(disaster);

            return ApiResponse.success("灾情信息解码并保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("解码或保存灾情信息失败：" + e.getMessage());
        }
    }

    // 获取灾情类型列表
    @GetMapping("/disaster/types")
    public ApiResponse<List<Object>> getDisasterTypes() {
        String disasterTypeListStr = "[" +
                "{\"code\": \"1\", \"name\": \"震情\"}," +
                "{\"code\": \"2\", \"name\": \"人员伤亡及失踪\"}," +
                "{\"code\": \"3\", \"name\": \"房屋破坏\"}," +
                "{\"code\": \"4\", \"name\": \"生命线工程灾情\"}," +
                "{\"code\": \"5\", \"name\": \"次生灾害\"}" +
                "]";

        JSONArray disasterTypeList = new JSONArray(disasterTypeListStr);

        return ApiResponse.success(disasterTypeList);
    }

    // 获取子灾情类型列表
    @GetMapping("/disaster/subTypes")
    public ApiResponse<List<Object>> getSubDisasterTypes(@RequestParam String disasterCode) {
        String subDisasterTypeListStr = "[" +
                "{\"code\": \"01\", \"name\": \"震情信息\", \"disasterCode\": \"1\"}," +
                "{\"code\": \"01\", \"name\": \"死亡\", \"disasterCode\": \"2\"}," +
                "{\"code\": \"02\", \"name\": \"受伤\", \"disasterCode\": \"2\"}," +
                "{\"code\": \"03\", \"name\": \"失踪\", \"disasterCode\": \"2\"}," +
                "{\"code\": \"01\", \"name\": \"土木\", \"disasterCode\": \"3\"}," +
                "{\"code\": \"02\", \"name\": \"砖木\", \"disasterCode\": \"3\"}," +
                "{\"code\": \"03\", \"name\": \"砖混\", \"disasterCode\": \"3\"}," +
                "{\"code\": \"04\", \"name\": \"框架\", \"disasterCode\": \"3\"}," +
                "{\"code\": \"05\", \"name\": \"其他\", \"disasterCode\": \"3\"}," +
                "{\"code\": \"01\", \"name\": \"交通\", \"disasterCode\": \"4\"}," +
                "{\"code\": \"02\", \"name\": \"供水\", \"disasterCode\": \"4\"}," +
                "{\"code\": \"03\", \"name\": \"输油\", \"disasterCode\": \"4\"}," +
                "{\"code\": \"04\", \"name\": \"燃气\", \"disasterCode\": \"4\"}," +
                "{\"code\": \"05\", \"name\": \"电力\", \"disasterCode\": \"4\"}," +
                "{\"code\": \"06\", \"name\": \"通信\", \"disasterCode\": \"4\"}," +
                "{\"code\": \"07\", \"name\": \"水利\", \"disasterCode\": \"4\"}," +
                "{\"code\": \"01\", \"name\": \"崩塌\", \"disasterCode\": \"5\"}," +
                "{\"code\": \"02\", \"name\": \"滑坡\", \"disasterCode\": \"5\"}," +
                "{\"code\": \"03\", \"name\": \"泥石流\", \"disasterCode\": \"5\"}," +
                "{\"code\": \"04\", \"name\": \"岩溶塌陷\", \"disasterCode\": \"5\"}," +
                "{\"code\": \"05\", \"name\": \"地裂缝\", \"disasterCode\": \"5\"}," +
                "{\"code\": \"06\", \"name\": \"地面沉降\", \"disasterCode\": \"5\"}," +
                "{\"code\": \"07\", \"name\": \"其他\", \"disasterCode\": \"5\"}" +
                "]";

        JSONArray allSubDisasterTypeList = new JSONArray(subDisasterTypeListStr);

        // 根据 disasterCode 来过滤子灾情类型
        List<Object> subDisasterTypeList = allSubDisasterTypeList.stream()
                .filter(item -> item.toString().split("disasterCode\":\"")[1].split("\"")[0].equals(disasterCode))
                .collect(Collectors.toList());

        return ApiResponse.success(subDisasterTypeList);
    }

    // 获取来源列表
    @GetMapping("/disaster/sources")
    public ApiResponse<List<Object>> getSources() {
        String sourceListStr = "[" +
                "{\"code\": \"1\", \"name\": \"业务报送数据\"}," +
                "{\"code\": \"2\", \"name\": \"泛在感知数据\"}," +
                "{\"code\": \"3\", \"name\": \"其他数据\"}" +
                "]";

        JSONArray sourceList = new JSONArray(sourceListStr);

        return ApiResponse.success(sourceList);
    }

    @GetMapping("/disaster/sourcesName")
    public ApiResponse<Object> getSources(@RequestParam(required = false) String code) {
        // 模拟的源数据列表
        String sourceListStr = "[" +
                "{\"code\": \"1\", \"name\": \"业务报送数据\"}," +
                "{\"code\": \"2\", \"name\": \"泛在感知数据\"}," +
                "{\"code\": \"3\", \"name\": \"其他数据\"}" +
                "]";

        try {
            // 使用 Jackson 的 ObjectMapper 来解析 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode sourceList = objectMapper.readTree(sourceListStr);

            if (code != null) {
                // 如果传递了编码，则查找对应的 name
                for (JsonNode source : sourceList) {
                    if (source.get("code").asText().equals(code)) {
                        // 只返回找到的 name
                        return ApiResponse.success(source.get("name").asText());
                    }
                }
                return ApiResponse.error("编码为空");
            }

            // 如果没有传递编码，返回整个列表
            return ApiResponse.success(sourceList);
        } catch (Exception e) {
            return ApiResponse.error("解析 JSON 数据失败"+e.getMessage());
        }
    }



    // 获取子来源列表
    @GetMapping("/disaster/subSources")
    public ApiResponse<List<Object>> getSubSources(@RequestParam String sourceCode) {
        String subSourceListStr = "[" +
                "{\"code\": \"00\", \"name\": \"前方地震应急指挥部\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"01\", \"name\": \"后方地震应急指挥部\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"20\", \"name\": \"应急指挥技术系统\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"21\", \"name\": \"社会服务工程应急救援系统\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"40\", \"name\": \"危险区预评估工作组\" , \"sourceCode\": \"1\"}," +
                "{\"code\": \"41\", \"name\": \"地震应急指挥技术协调组\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"42\", \"name\": \"震后政府信息支持工作项目组\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"80\", \"name\": \"灾情快速上报接收处理系统\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"81\", \"name\": \"地方地震局应急信息服务相关技术系统\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"99\", \"name\": \"其他\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"00\", \"name\": \"互联网感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"01\", \"name\": \"通信网感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"02\", \"name\": \"舆情网感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"03\", \"name\": \"电力系统感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"04\", \"name\": \"交通系统感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"05\", \"name\": \"其他\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"03\", \"name\": \"  \", \"sourceCode\": \"3\"}" +
                "]";

        JSONArray allSubSourceList = new JSONArray(subSourceListStr);

        // 根据 sourceCode 来过滤子来源
        List<Object> subSourceList = allSubSourceList.stream()
                .filter(item -> item.toString().split("sourceCode\":\"")[1].split("\"")[0].equals(sourceCode))
                .collect(Collectors.toList());

        return ApiResponse.success(subSourceList);
    }
    @GetMapping("/disaster/subSourcesName")
    public ApiResponse<Object> getSubSourcesName(@RequestParam(required = false) String code) {
        // 模拟的源数据列表
        String subSourceListStr = "[" +
                "{\"code\": \"00\", \"name\": \"前方地震应急指挥部\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"01\", \"name\": \"后方地震应急指挥部\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"20\", \"name\": \"应急指挥技术系统\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"21\", \"name\": \"社会服务工程应急救援系统\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"40\", \"name\": \"危险区预评估工作组\" , \"sourceCode\": \"1\"}," +
                "{\"code\": \"41\", \"name\": \"地震应急指挥技术协调组\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"42\", \"name\": \"震后政府信息支持工作项目组\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"80\", \"name\": \"灾情快速上报接收处理系统\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"81\", \"name\": \"地方地震局应急信息服务相关技术系统\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"99\", \"name\": \"其他\", \"sourceCode\": \"1\"}," +
                "{\"code\": \"00\", \"name\": \"互联网感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"01\", \"name\": \"通信网感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"02\", \"name\": \"舆情网感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"03\", \"name\": \"电力系统感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"04\", \"name\": \"交通系统感知\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"05\", \"name\": \"其他\", \"sourceCode\": \"2\"}," +
                "{\"code\": \"03\", \"name\": \"  \", \"sourceCode\": \"3\"}" +
                "]";

        try {
            // 使用 Jackson 的 ObjectMapper 来解析 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode subsourceList = objectMapper.readTree(subSourceListStr);

            if (code != null) {
                // 如果传递了编码，则查找对应的 name
                for (JsonNode source : subsourceList) {
                    if (source.get("code").asText().equals(code)) {
                        // 只返回找到的 name
                        return ApiResponse.success(source.get("name").asText());
                    }
                }
                return ApiResponse.error("未找到对应的来源类型");
            }

            // 如果没有传递编码，返回整个列表
            return ApiResponse.success(subsourceList);
        } catch (Exception e) {
            return ApiResponse.error("解析 JSON 数据失败"+e.getMessage());
        }
    }

    // 获取灾情载体类型列表 (模拟从数据库查询)
    @GetMapping("/disaster/carriers")
    public ApiResponse<List<String>> getCarriers() {
        // 模拟数据，返回灾情载体类型列表
        List<String> carriers = Arrays.asList("文字", "图像", "音频","视频","其他");
        return ApiResponse.success(carriers);
    }

    @GetMapping("/disaster/indicators")
    public ApiResponse<List<String>> getIndicators(@RequestParam String sourceCode) {
        List<String> indicators = Collections.emptyList();

        switch (sourceCode) {
            case "1":
                indicators = Arrays.asList("地理位置", "时间", "震级", "深度", "烈度");
                break;
            case "2":
                indicators = Arrays.asList("受灾人数", "受灾程度");
                break;
            case "3":
                indicators = Arrays.asList( "一般损坏面积", "严重损坏面积", "受灾程度");
                break;
            case "4":
                indicators = Arrays.asList( "受灾设施数", "受灾范围", "受灾程度");
                break;
            case "5":
                indicators = Arrays.asList("灾害损失", "灾害范围", "受灾程度");
                break;
            default:
                //return ApiResponse.error("无效的 sourceCode");
        }

        return ApiResponse.success(indicators);
    }




}
