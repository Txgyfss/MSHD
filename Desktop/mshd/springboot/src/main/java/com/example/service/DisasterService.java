package com.example.service;

import com.example.entity.Disaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void saveDisaster(Disaster disaster) throws Exception {
        // 假设此处将灾情信息存储到数据库
        // 保存到数据库
        saveDisasterCodeToDatabase(disaster);
        System.out.println("保存灾情信息: " + disaster);
    }
    public void saveDisasterCodeToDatabase(Disaster disaster) throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/mshd?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // 替换为你的数据库URL
        String user = "root"; // 替换为你的数据库用户名
        String password = "0978543"; // 替换为你的数据库密码

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 连接到数据库
            connection = DriverManager.getConnection(url, user, password);
// 输出接收到的 Disaster 对象
            System.out.println(disaster);
            System.out.println("正在插入到数据库 " );
            // SQL插入语句
            String sql = "INSERT INTO disaster (id, province, city, country, town, village, date, location, detail, reportingUnit, sourceCategory, sourceSubCategory, carrierForm, link, disasterMajorCategory, disasterSubCategory, disasterIndicator,disastercode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数
            preparedStatement.setString(1, disaster.getId());
            preparedStatement.setString(2, disaster.getProvince());
            preparedStatement.setString(3, disaster.getCity());
            preparedStatement.setString(4, disaster.getCountry());
            preparedStatement.setString(5, disaster.getTown());
            preparedStatement.setString(6, disaster.getVillage());
            preparedStatement.setTimestamp(7, new Timestamp(disaster.getDate().getTime()));
            preparedStatement.setString(8, disaster.getLocation());
            preparedStatement.setString(9, disaster.getDetail());
            preparedStatement.setString(10, disaster.getReportingUnit());
            preparedStatement.setString(11, disaster.getSourceCategory());
            preparedStatement.setString(12, disaster.getSourceSubCategory());
            preparedStatement.setString(13, disaster.getCarrierForm());
            preparedStatement.setString(14, disaster.getLocation());
            preparedStatement.setString(15, disaster.getDisasterMajorCategory());
            preparedStatement.setString(16, disaster.getDisasterSubCategory());
            preparedStatement.setString(17, disaster.getDisasterIndicator());
            preparedStatement.setString(18, disaster.getDisastercode());

            // 执行插入
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("数据库插入失败：" + e.getMessage(), e);
        } finally {
            // 关闭资源
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }

    // 编码操作（这里的实现你可以根据需求稍后补充）
    public String encodeDisasterInfo(Disaster disaster) throws Exception {
        // 初始化编码字符串
        StringBuilder encodedId = new StringBuilder();
// 输出接收到的 Disaster 对象
        System.out.println(disaster);
        // 1. 地区码 (12位)
        String villageName = disaster.getVillage(); // 获取村庄名称
        String locationCode = getLocationCode(villageName); // 数据库中查找地区码
        if (locationCode.length() != 12) {
            throw new Exception("地区码格式错误：" + locationCode);
        }
        encodedId.append(locationCode);

        // 2. 日期和时间 (14位)
        Date date = disaster.getDate(); // 获取日期时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = dateFormat.format(date); // 格式化为YYYYMMDDHHMMSS
        if (dateTime.length() != 14) {
            throw new Exception("日期时间格式错误：" + dateTime);
        }
        encodedId.append(dateTime);

        // 3. 来源分类 (3位)
        String sourceCategory = disaster.getSourceCategory(); // 来源大类
        String sourceSubCategory = disaster.getSourceSubCategory(); // 来源子类
        String sourceCategoryCode = getSourceCategoryCode(sourceCategory, sourceSubCategory); // 获取来源编码
        if (sourceCategoryCode.length() != 3) {
            throw new Exception("来源分类格式错误：" + sourceCategoryCode);
        }
        encodedId.append(sourceCategoryCode);

        // 4. 载体编码 (1位)
        String carrierForm = disaster.getCarrierForm(); // 获取载体形式
        String carrierCode = getCarrierCode(carrierForm); // 获取载体编码
        if (carrierCode.length() != 1) {
            throw new Exception("载体编码格式错误：" + carrierCode);
        }
        encodedId.append(carrierCode);

        // 5. 灾情编码 (6位)
        String disasterMajorCategory = disaster.getDisasterMajorCategory(); // 获取灾情大类
        String disasterSubCategory = disaster.getDisasterSubCategory(); // 获取灾情子类
        String disasterIndicator = disaster.getDisasterIndicator(); // 获取灾情指标
        String disasterCode = getDisasterCode(disasterMajorCategory, disasterSubCategory, disasterIndicator); // 获取灾情编码
        if (disasterCode.length() != 6) {
            throw new Exception("灾情编码格式错误：" + disasterCode);
        }
        encodedId.append(disasterCode);
        String thedisastercode=encodedId.toString();
        System.out.println("灾情编码: " + thedisastercode);
        // 保存到 Disaster 对象
        disaster.setDisastercode(thedisastercode);


        // 返回完整编码
        return thedisastercode;
    }

    // 获取地区码 (根据village名称查询数据库)
    private String getLocationCode(String villageName) throws Exception {
        // 模拟数据库查询
        String query = "SELECT Code FROM village WHERE name = '" + villageName + "';";
        String code = executeDatabaseQuery(query); // 执行查询语句
        return code; // 返回地区码
    }

    // 获取来源分类编码 (1位大类 + 2位子类)
    private String getSourceCategoryCode(String category, String subCategory) throws Exception {
        // 模拟来源分类编码逻辑
        if ("业务报送数据".equals(category)) {
            if ("前方地震应急指挥部".equals(subCategory)) return "100";
            if ("后方地震应急指挥部".equals(subCategory)) return "101";
            if ("应急指挥技术系统".equals(subCategory)) return "120";
            if ("社会服务工程应急救援系统".equals(subCategory)) return "121";
            if ("危险区预评估工作组".equals(subCategory)) return "140";
            if ("地震应急指挥技术协调组".equals(subCategory)) return "141";
            if ("震后政府信息支持工作项目组".equals(subCategory)) return "142";
            if ("灾情快速上报接收处理系统".equals(subCategory)) return "180";
            if ("地方地震局应急信息服务相关技术系统".equals(subCategory)) return "181";
            if ("其他".equals(subCategory)) return "199";
        } else if ("泛在感知数据".equals(category)) {
            if ("互联网感知".equals(subCategory)) return "200";
            if ("通信网感知".equals(subCategory)) return "201";
            if ("舆情网感知".equals(subCategory)) return "202";
            if ("电力系统感知".equals(subCategory)) return "203";
            if ("交通系统感知".equals(subCategory)) return "204";
            if ("其他".equals(subCategory)) return "205";
        } else if ("其他数据".equals(category)) {
            if (Objects.equals(subCategory, null)) return "300";
        }
        throw new Exception("无效的来源分类：" + category + " - " + subCategory);
    }


    // 获取载体编码 (1位)
    private String getCarrierCode(String carrierForm) throws Exception {
        switch (carrierForm) {
            case "文字": return "1";
            case "图像": return "2";
            case "音频": return "3";
            case "视频": return "4";
            case "其他": return "5";
            default: throw new Exception("无效的载体形式：" + carrierForm);
        }
    }

    // 获取灾情编码 (1位大类 + 2位子类 + 3位指标)
    private String getDisasterCode(String majorCategory, String subCategory, String indicator) throws Exception {
        StringBuilder code = new StringBuilder();

        // 灾情大类
        if ("震情".equals(majorCategory)) code.append("1");
        else if ("人员伤亡及失踪".equals(majorCategory)) code.append("2");
        else if ("房屋破坏".equals(majorCategory)) code.append("3");
        else if ("生命线工程灾情".equals(majorCategory)) code.append("4");
        else if ("次生灾害".equals(majorCategory)) code.append("5");
        else throw new Exception("无效的灾情大类：" + majorCategory);

        // 灾情子类
        switch (subCategory) {
            case "震情信息": code.append("01"); break;
            case "死亡": code.append("01"); break;
            case "受伤": code.append("02"); break;
            case "失踪": code.append("03"); break;
            case "土木": code.append("01"); break;
            case "砖木": code.append("02"); break;
            case "砖混": code.append("03"); break;
            case "框架": code.append("04"); break;
            case "其他": code.append("05"); break;
            case "交通": code.append("01"); break;
            case "供水": code.append("02"); break;
            case "输油": code.append("03"); break;
            case "燃气": code.append("04"); break;
            case "电力": code.append("05"); break;
            case "通信": code.append("06"); break;
            case "水利": code.append("07"); break;
            case "崩塌": code.append("01"); break;
            case "滑坡": code.append("02"); break;
            case "泥石流": code.append("03"); break;
            case "岩溶塌陷": code.append("04"); break;
            case "地裂缝": code.append("05"); break;
            case "地面沉降": code.append("06"); break;
            default: throw new Exception("无效的灾情子类：" + subCategory);
        }

        // 灾情指标
        switch (indicator) {
            case "地理位置": code.append("001"); break;
            case "时间": code.append("002"); break;
            case "震级": code.append("003"); break;
            case "深度": code.append("004"); break;
            case "烈度": code.append("005"); break;
            case "受灾人数": code.append("001"); break;
            case "受灾程度": code.append("002"); break;
            case "一般损坏面积": code.append("001"); break;
            case "严重损坏面积": code.append("002"); break;
            case "受灾范围": code.append("002"); break;
            case "灾害损失": code.append("001"); break;
            default: throw new Exception("无效的灾情指标：" + indicator);
        }

        String disasterCode = code.toString();


        return disasterCode;
    }


    // 模拟执行数据库查询
    private String executeDatabaseQuery(String query) throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/mshd?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // 替换为你的数据库URL
        String user = "root"; // 替换为你的数据库用户名
        String password = "0978543"; // 替换为你的数据库密码

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 连接到数据库
            connection = DriverManager.getConnection(url, user, password);

            // 创建PreparedStatement以防止SQL注入
            preparedStatement = connection.prepareStatement(query);

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            // 处理查询结果
            if (resultSet.next()) {
                return resultSet.getString(1); // 假定查询返回的结果在第一列
            } else {
                throw new Exception("未找到符合条件的记录：" + query);
            }
        } catch (Exception e) {
            throw new Exception("数据库查询失败：" + e.getMessage(), e);
        } finally {
            // 关闭资源
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }
    /**
     * 根据ID获取灾情信息
     */
    public Disaster getDisasterById(int id) {
        String sql = "SELECT id, disastercode, province, city, country, town, village, date, location, detail, " +
                "reportingUnit, sourceCategory, sourceSubCategory, carrierForm, link, disasterMajorCategory, " +
                "disasterSubCategory, disasterIndicator FROM disaster WHERE id = ?";

        try (Connection connection = dataSource.getConnection(); // 从数据源获取连接
             PreparedStatement statement = connection.prepareStatement(sql)) { // 准备查询语句

            statement.setInt(1, id); // 设置参数
            try (ResultSet resultSet = statement.executeQuery()) { // 执行查询
                if (resultSet.next()) {
                    Disaster disaster = new Disaster();
                    disaster.setId(String.valueOf(resultSet.getInt("id")));
                    disaster.setDisastercode(resultSet.getString("disastercode"));
                    disaster.setProvince(resultSet.getString("province"));
                    disaster.setCity(resultSet.getString("city"));
                    disaster.setCountry(resultSet.getString("country"));
                    disaster.setTown(resultSet.getString("town"));
                    disaster.setVillage(resultSet.getString("village"));
                    disaster.setDate(resultSet.getTimestamp("date"));
                    disaster.setLocation(resultSet.getString("location"));
                    disaster.setDetail(resultSet.getString("detail"));
                    disaster.setReportingUnit(resultSet.getString("reportingUnit"));
                    disaster.setSourceCategory(resultSet.getString("sourceCategory"));
                    disaster.setSourceSubCategory(resultSet.getString("sourceSubCategory"));
                    disaster.setCarrierForm(resultSet.getString("carrierForm"));
                    disaster.setLink(resultSet.getString("link"));
                    disaster.setDisasterMajorCategory(resultSet.getString("disasterMajorCategory"));
                    disaster.setDisasterSubCategory(resultSet.getString("disasterSubCategory"));
                    disaster.setDisasterIndicator(resultSet.getString("disasterIndicator"));
                    return disaster; // 返回灾情对象
                } else {
                    throw new Exception("未找到对应的灾情信息，ID: " + id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取灾情信息时发生错误：" + e.getMessage());
            return null;
        }
    }

    // 解码操作（这里的实现你可以根据需求稍后补充）
    public Disaster decodeDisasterInfo(String disasterCode) throws Exception {
        if (disasterCode == null || disasterCode.length() != 36) {
            throw new Exception("无效的灾情编码长度！");
        }

        Disaster disaster = new Disaster();

        // 解码地区码（前12位）
        String locationCode = disasterCode.substring(0, 12);
        Map<String, String> locationMap = queryLocationByCode(locationCode);
        disaster.setProvince(locationMap.get("province"));
        disaster.setCity(locationMap.get("city"));
        disaster.setCountry(locationMap.get("country"));
        disaster.setTown(locationMap.get("town"));
        disaster.setVillage(locationMap.get("village"));
        disaster.setLocation(String.join(" ", locationMap.values())); // 将完整地址存储到 location

        // 解码时间（中间14位）
        String dateTimeCode = disasterCode.substring(12, 26);
        Date date = parseDateTimeCode(dateTimeCode);
        disaster.setDate(date);

        // 解码来源分类（第27-29位）
        String sourceCategoryCode = disasterCode.substring(26, 29);
        String[] sourceCategory = decodeSourceCategory(sourceCategoryCode);
        disaster.setSourceCategory(sourceCategory[0]); // 大类
        disaster.setSourceSubCategory(sourceCategory[1]); // 子类

        // 解码载体形式（第30位）
        String carrierCode = disasterCode.substring(29, 30);
        String carrier = decodeCarrier(carrierCode);
        disaster.setCarrierForm(carrier);

        // 解码灾情分类（最后6位）
        String disasterCategoryCode = disasterCode.substring(30);
        String[] disasterCategory = decodeDisasterCategory(disasterCategoryCode);
        disaster.setDisasterMajorCategory(disasterCategory[0]); // 大类
        disaster.setDisasterSubCategory(disasterCategory[1]); // 子类
        disaster.setDisasterIndicator(disasterCategory[2]); // 指标
        System.out.println("1解码得到的对象: " + disaster);

        return disaster;
    }

    private Map<String, String> queryLocationByCode(String locationCode) throws Exception {
        Map<String, String> locationMap = new HashMap<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mshd?useSSL=false&serverTimezone=UTC", "root", "0978543")) {
            // 查询省
            String provinceCode = locationCode.substring(0, 2); // 前2位
            String provinceQuery = "SELECT name FROM province WHERE code = ?";
            try (PreparedStatement provinceStmt = connection.prepareStatement(provinceQuery)) {
                provinceStmt.setString(1, provinceCode);
                ResultSet resultSet = provinceStmt.executeQuery();
                if (resultSet.next()) {
                    locationMap.put("province", resultSet.getString("name"));
                } else {
                    throw new Exception("未找到对应的省：" + provinceCode);
                }
            }

            // 查询市
            String cityCode = locationCode.substring(0, 4); // 前4位
            String cityQuery = "SELECT name FROM city WHERE code = ?";
            try (PreparedStatement cityStmt = connection.prepareStatement(cityQuery)) {
                cityStmt.setString(1, cityCode);
                ResultSet resultSet = cityStmt.executeQuery();
                if (resultSet.next()) {
                    locationMap.put("city", resultSet.getString("name"));
                } else {
                    throw new Exception("未找到对应的市：" + cityCode);
                }
            }

            // 查询区/县
            String areaCode = locationCode.substring(0, 6); // 前6位
            String areaQuery = "SELECT name FROM area WHERE code = ?";
            try (PreparedStatement areaStmt = connection.prepareStatement(areaQuery)) {
                areaStmt.setString(1, areaCode);
                ResultSet resultSet = areaStmt.executeQuery();
                if (resultSet.next()) {
                    locationMap.put("country", resultSet.getString("name"));
                } else {
                    throw new Exception("未找到对应的区/县：" + areaCode);
                }
            }

            // 查询乡/镇
            String streetCode = locationCode.substring(0, 9); // 前9位
            String streetQuery = "SELECT name FROM street WHERE code = ?";
            try (PreparedStatement streetStmt = connection.prepareStatement(streetQuery)) {
                streetStmt.setString(1, streetCode);
                ResultSet resultSet = streetStmt.executeQuery();
                if (resultSet.next()) {
                    locationMap.put("town", resultSet.getString("name"));
                } else {
                    throw new Exception("未找到对应的乡/镇：" + streetCode);
                }
            }

            // 查询村
            String villageCode = locationCode.substring(0, 12); // 前12位
            String villageQuery = "SELECT name FROM village WHERE code = ?";
            try (PreparedStatement villageStmt = connection.prepareStatement(villageQuery)) {
                villageStmt.setString(1, villageCode);
                ResultSet resultSet = villageStmt.executeQuery();
                if (resultSet.next()) {
                    locationMap.put("village", resultSet.getString("name"));
                } else {
                    throw new Exception("未找到对应的村：" + villageCode);
                }
            }
        }

        return locationMap;
    }



    private Date parseDateTimeCode(String dateTimeCode) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.parse(dateTimeCode);
    }


    private String[] decodeSourceCategory(String sourceCategoryCode) throws Exception {
        String category;
        String subCategory;

        // 根据来源分类编码解析
        switch (sourceCategoryCode.substring(0, 1)) {
            case "1":
                category = "业务报送数据";
                switch (sourceCategoryCode.substring(1, 3)) {
                    case "00": subCategory = "前方地震应急指挥部"; break;
                    case "01": subCategory = "后方地震应急指挥部"; break;
                    case "20": subCategory = "应急指挥技术系统"; break;
                    case "21": subCategory = "社会服务工程应急救援系统"; break;
                    case "40": subCategory = "危险区预评估工作组"; break;
                    case "41": subCategory = "地震应急指挥技术协调组"; break;
                    case "42": subCategory = "震后政府信息支持工作项目组"; break;
                    case "80": subCategory = "灾情快速上报接收处理系统"; break;
                    case "81": subCategory = "地方地震局应急信息服务相关技术系统"; break;
                    case "99": subCategory = "其他"; break;
                    default: throw new Exception("无效的来源子类编码：" + sourceCategoryCode);
                }
                break;
            case "2":
                category = "泛在感知数据";
                switch (sourceCategoryCode.substring(1, 3)) {
                    case "00": subCategory = "互联网感知"; break;
                    case "01": subCategory = "通信网感知"; break;
                    case "02": subCategory = "舆情网感知"; break;
                    case "03": subCategory = "电力系统感知"; break;
                    case "04": subCategory = "交通系统感知"; break;
                    case "05": subCategory = "其他"; break;
                    default: throw new Exception("无效的来源子类编码：" + sourceCategoryCode);
                }
                break;
            case "3":
                category = "其他数据";
                switch (sourceCategoryCode.substring(1, 3)) {
                    case "00": subCategory = "其他"; break;
                    default: throw new Exception("无效的来源子类编码：" + sourceCategoryCode);
                }
                break;
            default:
                throw new Exception("无效的来源分类编码：" + sourceCategoryCode);
        }

        return new String[]{category, subCategory};
    }

    private String decodeCarrier(String carrierCode) throws Exception {
        switch (carrierCode) {
            case "1": return "文字";
            case "2": return "图像";
            case "3": return "音频";
            case "4": return "视频";
            case "5": return "其他";
            default: throw new Exception("无效的载体编码：" + carrierCode);
        }
    }

    private String[] decodeDisasterCategory(String disasterCategoryCode) throws Exception {
        String majorCategory;
        String subCategory;
        String indicator;


        switch (disasterCategoryCode.substring(0, 1)) {
            case "1":
                majorCategory = "震情";
                switch (disasterCategoryCode.substring(1, 3)) {
                    case "01": subCategory = "震情信息"; break;
                    default: throw new Exception("无效的震情子类编码：" + disasterCategoryCode.substring(1, 3));
                }
                switch (disasterCategoryCode.substring(3, 6)) {
                    case "001": indicator = "地理位置"; break;
                    case "002": indicator = "时间"; break;
                    case "003": indicator = "震级"; break;
                    case "004": indicator = "深度"; break;
                    case "005": indicator = "烈度"; break;
                    default: throw new Exception("无效的震情指标编码：" + disasterCategoryCode.substring(3, 6));
                }
                break;

            case "2":
                majorCategory = "人员伤亡及失踪";
                switch (disasterCategoryCode.substring(1, 3)) {
                    case "01": subCategory = "死亡"; break;
                    case "02": subCategory = "受伤"; break;
                    case "03": subCategory = "失踪"; break;
                    default: throw new Exception("无效的人员伤亡及失踪子类编码：" + disasterCategoryCode.substring(1, 3));
                }
                switch (disasterCategoryCode.substring(3, 6)) {
                    case "001": indicator = "受灾人数"; break;
                    case "002": indicator = "受灾程度"; break;
                    default: throw new Exception("无效的人员伤亡及失踪指标编码：" + disasterCategoryCode.substring(3, 6));
                }
                break;

            case "3":
                majorCategory = "房屋破坏";
                switch (disasterCategoryCode.substring(1, 3)) {
                    case "01": subCategory = "土木"; break;
                    case "02": subCategory = "砌木"; break;
                    case "03": subCategory = "砌混"; break;
                    case "04": subCategory = "框架"; break;
                    case "05": subCategory = "其他"; break;
                    default: throw new Exception("无效的房屋破坏子类编码：" + disasterCategoryCode.substring(1, 3));
                }
                switch (disasterCategoryCode.substring(3, 6)) {
                    case "001": indicator = "一般损坏面积"; break;
                    case "002": indicator = "严重损坏面积"; break;
                    case "003": indicator = "受灾程度"; break;
                    default: throw new Exception("无效的房屋破坏指标编码：" + disasterCategoryCode.substring(3, 6));
                }
                break;

            case "4":
                majorCategory = "生命线工程灾情";
                switch (disasterCategoryCode.substring(1, 3)) {
                    case "01": subCategory = "交通"; break;
                    case "02": subCategory = "供水"; break;
                    case "03": subCategory = "输油"; break;
                    case "04": subCategory = "燃气"; break;
                    case "05": subCategory = "电力"; break;
                    case "06": subCategory = "通信"; break;
                    case "07": subCategory = "水利"; break;
                    default: throw new Exception("无效的生命线工程灾情子类编码：" + disasterCategoryCode.substring(1, 3));
                }
                switch (disasterCategoryCode.substring(3, 6)) {
                    case "001": indicator = "受灾设施数"; break;
                    case "002": indicator = "受灾范围"; break;
                    case "003": indicator = "受灾程度"; break;
                    default: throw new Exception("无效的生命线工程灾情指标编码：" + disasterCategoryCode.substring(3, 6));
                }
                break;

            case "5":
                majorCategory = "次生灾害";
                switch (disasterCategoryCode.substring(1, 3)) {
                    case "01": subCategory = "崩塌"; break;
                    case "02": subCategory = "滑坡"; break;
                    case "03": subCategory = "泥石流"; break;
                    case "04": subCategory = "岩溶塌陷"; break;
                    case "05": subCategory = "地裂缝"; break;
                    case "06": subCategory = "地面沉降"; break;
                    case "07": subCategory = "其他"; break;
                    default: throw new Exception("无效的次生灾害子类编码：" + disasterCategoryCode.substring(1, 3));
                }
                switch (disasterCategoryCode.substring(3, 6)) {
                    case "001": indicator = "灾害损失"; break;
                    case "002": indicator = "灾害范围"; break;
                    case "003": indicator = "受灾程度"; break;
                    default: throw new Exception("无效的次生灾害指标编码：" + disasterCategoryCode.substring(3, 6));
                }
                break;

            default:
                throw new Exception("无效的灾情大类编码：" + disasterCategoryCode.substring(0, 1));
        }

// 最终返回解码结果
        System.out.println("大类：" + majorCategory + "，子类：" + subCategory + "，指标：" + indicator);

        return new String[]{majorCategory, subCategory, indicator};
    }

    public void saveDisasterToDatabase(Disaster disaster) throws Exception {
        String sql = "INSERT INTO disaster (id, province, city, country, town, village, date, location, detail, reportingUnit, sourceCategory, sourceSubCategory, carrierForm, link, disasterMajorCategory, disasterSubCategory, disasterIndicator) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mshd?useSSL=false&serverTimezone=UTC", "root", "0978543");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, disaster.getId());
            preparedStatement.setString(2, disaster.getProvince());
            preparedStatement.setString(3, disaster.getCity());
            preparedStatement.setString(4, disaster.getCountry());
            preparedStatement.setString(5, disaster.getTown());
            preparedStatement.setString(6, disaster.getVillage());
            preparedStatement.setTimestamp(7, new Timestamp(disaster.getDate().getTime()));
            preparedStatement.setString(8, disaster.getLocation());
            preparedStatement.setString(9, disaster.getDetail());
            preparedStatement.setString(10, disaster.getReportingUnit());
            preparedStatement.setString(11, disaster.getSourceCategory());
            preparedStatement.setString(12, disaster.getSourceSubCategory());
            preparedStatement.setString(13, disaster.getCarrierForm());
            preparedStatement.setString(14, disaster.getLocation());
            preparedStatement.setString(15, disaster.getDisasterMajorCategory());
            preparedStatement.setString(16, disaster.getDisasterSubCategory());
            preparedStatement.setString(17, disaster.getDisasterIndicator());
            preparedStatement.executeUpdate();
        }
    }
    @Autowired
    private DataSource dataSource;

    /**
     * 删除灾情信息
     */
    public boolean deleteDisasterById(int id) {
        String sql = "DELETE FROM disaster WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除灾情信息时发生错误：" + e.getMessage());
            return false;
        }
    }

    /**
     * 查看所有灾情信息
     */
    public List<Map<String, Object>> getAllDisasters() {
        // SQL 查询语句
        String sql = "SELECT id,disastercode, province, city, country, town, village, date, location, detail, " +
                "reportingUnit, sourceCategory, sourceSubCategory, carrierForm, link, disasterMajorCategory, " +
                "disasterSubCategory, disasterIndicator FROM disaster";

        // 用于存储查询结果的列表
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection connection = dataSource.getConnection(); // 从数据源获取连接
             PreparedStatement statement = connection.prepareStatement(sql); // 准备查询语句
             ResultSet resultSet = statement.executeQuery()) { // 执行查询

            // 遍历结果集
            while (resultSet.next()) {
                // 使用 HashMap 构建每一条记录
                Map<String, Object> disaster = new HashMap<>();
                disaster.put("id", resultSet.getString("id"));
                disaster.put("disastercode", resultSet.getString("disastercode"));
                disaster.put("province", resultSet.getString("province"));
                disaster.put("city", resultSet.getString("city"));
                disaster.put("country", resultSet.getString("country"));
                disaster.put("town", resultSet.getString("town"));
                disaster.put("village", resultSet.getString("village"));
                disaster.put("date", resultSet.getTimestamp("date"));
                disaster.put("location", resultSet.getString("location"));
                disaster.put("detail", resultSet.getString("detail"));
                disaster.put("reportingUnit", resultSet.getString("reportingUnit"));
                disaster.put("sourceCategory", resultSet.getString("sourceCategory"));
                disaster.put("sourceSubCategory", resultSet.getString("sourceSubCategory"));
                disaster.put("carrierForm", resultSet.getString("carrierForm"));
                disaster.put("link", resultSet.getString("link"));
                disaster.put("disasterMajorCategory", resultSet.getString("disasterMajorCategory"));
                disaster.put("disasterSubCategory", resultSet.getString("disasterSubCategory"));
                disaster.put("disasterIndicator", resultSet.getString("disasterIndicator"));

                // 添加到结果列表中
                results.add(disaster);
            }

        } catch (Exception e) {
            // 捕获异常并打印堆栈信息
            e.printStackTrace();
            System.out.println("查询所有灾情信息时发生错误：" + e.getMessage());
        }

        // 返回结果列表
        return results;
    }

    /**
     * 筛选灾情信息
     */
    public List<Map<String, Object>> filterDisasters(String province, String city, String carrierForm,
                                                     String sourceCategory, String disasterMajorCategory,
                                                     Date startDate, Date endDate, String sortOrder) {
        StringBuilder sql = new StringBuilder(
                "SELECT disastercode, province, city, country, town, village, date, location, detail, " +
                        "reportingUnit, sourceCategory, sourceSubCategory, carrierForm, link, disasterMajorCategory, " +
                        "disasterSubCategory, disasterIndicator FROM disaster WHERE 1=1");

        List<Object> params = new ArrayList<>();

        // 动态构建筛选条件
        if (province != null && !province.isEmpty()) {
            sql.append(" AND province = ?");
            params.add(province);
        }
        if (city != null && !city.isEmpty()) {
            sql.append(" AND city = ?");
            params.add(city);
        }
        if (carrierForm != null && !carrierForm.isEmpty()) {
            sql.append(" AND carrierForm = ?");
            params.add(carrierForm);
        }
        if (sourceCategory != null && !sourceCategory.isEmpty()) {
            sql.append(" AND sourceCategory = ?");
            params.add(sourceCategory);
        }
        if (disasterMajorCategory != null && !disasterMajorCategory.isEmpty()) {
            sql.append(" AND disasterMajorCategory = ?");
            params.add(disasterMajorCategory);
        }
        if (startDate != null) {
            sql.append(" AND date >= ?");
            params.add(new java.sql.Date(startDate.getTime())); // 转换为 SQL Date
        }
        if (endDate != null) {
            sql.append(" AND date <= ?");
            params.add(new java.sql.Date(endDate.getTime())); // 转换为 SQL Date
        }

        // 动态添加排序
        if ("asc".equalsIgnoreCase(sortOrder)) {
            sql.append(" ORDER BY date ASC");
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            sql.append(" ORDER BY date DESC");
        }

        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); // 数据源获取连接
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            // 动态绑定参数
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i)); // 参数从 1 开始
            }

            // 执行查询
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> disaster = new HashMap<>();
                    disaster.put("disastercode", resultSet.getString("disastercode"));
                    disaster.put("province", resultSet.getString("province"));
                    disaster.put("city", resultSet.getString("city"));
                    disaster.put("country", resultSet.getString("country"));
                    disaster.put("town", resultSet.getString("town"));
                    disaster.put("village", resultSet.getString("village"));
                    disaster.put("date", resultSet.getDate("date"));
                    disaster.put("location", resultSet.getString("location"));
                    disaster.put("detail", resultSet.getString("detail"));
                    disaster.put("reportingUnit", resultSet.getString("reportingUnit"));
                    disaster.put("sourceCategory", resultSet.getString("sourceCategory"));
                    disaster.put("sourceSubCategory", resultSet.getString("sourceSubCategory"));
                    disaster.put("carrierForm", resultSet.getString("carrierForm"));
                    disaster.put("link", resultSet.getString("link"));
                    disaster.put("disasterMajorCategory", resultSet.getString("disasterMajorCategory"));
                    disaster.put("disasterSubCategory", resultSet.getString("disasterSubCategory"));
                    disaster.put("disasterIndicator", resultSet.getString("disasterIndicator"));

                    // 添加到结果列表
                    results.add(disaster);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("筛选灾情信息时发生错误：" + e.getMessage());
        }

        return results;
    }
    public boolean updateDisasterInDatabase(Disaster updatedDisaster) {
        System.out.println("正在更新到数据库 " );
        String sql = "UPDATE disaster SET province = ?, city = ?, country = ?, town = ?, village = ?, date = ?, " +
                "location = ?, detail = ?, reportingUnit = ?, sourceCategory = ?, sourceSubCategory = ?, " +
                "carrierForm = ?, link = ?, disasterMajorCategory = ?, disasterSubCategory = ?, " +
                "disasterIndicator = ?, disastercode = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 设置参数
            statement.setString(1, updatedDisaster.getProvince());
            statement.setString(2, updatedDisaster.getCity());
            statement.setString(3, updatedDisaster.getCountry());
            statement.setString(4, updatedDisaster.getTown());
            statement.setString(5, updatedDisaster.getVillage());
            statement.setDate(6, updatedDisaster.getDate() != null ? new java.sql.Date(updatedDisaster.getDate().getTime()) : null);
            statement.setString(7, updatedDisaster.getLocation());
            statement.setString(8, updatedDisaster.getDetail());
            statement.setString(9, updatedDisaster.getReportingUnit());
            statement.setString(10, updatedDisaster.getSourceCategory());
            statement.setString(11, updatedDisaster.getSourceSubCategory());
            statement.setString(12, updatedDisaster.getCarrierForm());
            statement.setString(13, updatedDisaster.getLink());
            statement.setString(14, updatedDisaster.getDisasterMajorCategory());
            statement.setString(15, updatedDisaster.getDisasterSubCategory());
            statement.setString(16, updatedDisaster.getDisasterIndicator());
            statement.setString(17, updatedDisaster.getDisastercode());
            statement.setInt(18, Integer.parseInt(updatedDisaster.getId()));

            // 执行更新操作
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("更新灾情信息时发生错误：" + e.getMessage());
            return false;
        }
    }

}

