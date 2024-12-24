package com.example.controller;

import com.example.ApiResponse;
import com.example.entity.Disaster;
import com.example.service.DisasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
            // 验证灾情编码是否存在
            if (disaster.getDisastercode() == null || disaster.getDisastercode().isEmpty()) {
                return ApiResponse.error("灾情编码不能为空！");
            }

            // 从数据库中获取当前灾情信息
            Disaster existingDisaster = disasterService.decodeDisasterInfo(disaster.getDisastercode());
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
    public ApiResponse<String> decodeAndSaveDisaster(@RequestBody Map<String, String> request) {
        try {
            String disasterCode = request.get("disasterCode");
            // 调用解码方法生成灾情对象
            System.out.println("传入的编码: " + disasterCode);
            Disaster disaster = disasterService.decodeDisasterInfo(disasterCode);
            disaster.setDisastercode(disasterCode);
            System.out.println("解码得到的对象: " + disaster);
            // 将灾情对象存储到数据库
            disasterService.saveDisasterCodeToDatabase(disaster);

            return ApiResponse.success("解码成功并保存灾情信息，灾情编码：" + disaster.getDisastercode());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("解码或保存灾情信息失败：" + e.getMessage());
        }
    }

}
