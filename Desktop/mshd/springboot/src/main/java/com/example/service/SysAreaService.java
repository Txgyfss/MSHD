package com.example.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysAreaService {

    // 数据库连接配置
    private static final String URL = "jdbc:mysql://localhost:3306/mshd?useSSL=false&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // 数据库用户名
    private static final String PASSWORD = "0978543"; // 数据库密码

    /**
     * 获取省列表
     */
    public List<Map<String, Object>> getProvinces() {
        String sql = "SELECT code, name FROM province";
        return executeQuery(sql, null);
    }

    /**
     * 获取市列表
     * @param provinceCode 省编码
     */
    public List<Map<String, Object>> getCities(String provinceCode) {
        String sql = "SELECT code, name FROM city WHERE provincecode = ?";
        return executeQuery(sql, new Object[]{provinceCode});
    }

    /**
     * 获取县列表
     * @param cityCode 市编码
     */
    public List<Map<String, Object>> getCounties(String cityCode) {
        String sql = "SELECT code, name FROM area WHERE cityCode = ?";
        return executeQuery(sql, new Object[]{cityCode});
    }

    /**
     * 获取乡列表
     * @param areaCode 县编码
     */
    public List<Map<String, Object>> getTowns(String areaCode) {
        String sql = "SELECT code, name FROM street WHERE areacode = ?";
        return executeQuery(sql, new Object[]{areaCode});
    }

    /**
     * 获取村列表
     * @param streetCode 乡编码
     */
    public List<Map<String, Object>> getVillages(String streetCode) {
        String sql = "SELECT code, name FROM village WHERE streetcode = ?";
        return executeQuery(sql, new Object[]{streetCode});
    }

    /**
     * 通用的 JDBC 查询方法
     * @param sql SQL 语句
     * @param params SQL 参数
     * @return 查询结果
     */
    private List<Map<String, Object>> executeQuery(String sql, Object[] params) {
        List<Map<String, Object>> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 获取数据库连接
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // 创建 PreparedStatement
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            // 处理结果集
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
