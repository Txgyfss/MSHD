package com.example;

public class ApiResponse<T> {

    private String code;
    private String msg;
    private T data;

    private ApiResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("0", "成功", data);
    }

    // 失败响应
    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>("1", msg, null);
    }

    // Getters 和 Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
