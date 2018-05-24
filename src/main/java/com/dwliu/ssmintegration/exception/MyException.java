package com.dwliu.ssmintegration.exception;

public class MyException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private int code;

    private int DEFAULT_CODE = Integer.MAX_VALUE;

    // 用于国际化的key值
    private String key;

    // 国际化占位符的值
    private String[] arguments;

    public MyException(int code, String message) {
        super(message);
        this.code = code;
    }

    public MyException(String message) {
        super(message);
        this.code = DEFAULT_CODE;
    }

    public MyException(int code, String message, String key, String[] arguments) {
        super(message);
        this.code = code;
        this.key = key;
        this.arguments = arguments;
    }

    public MyException(String message, String key, String[] arguments) {
        super(message);
        this.code = DEFAULT_CODE;
        this.key = key;
        this.arguments = arguments;
    }

    public MyException(int code, String message, String key) {
        super(message);
        this.code = code;
        this.key = key;
    }

    public MyException(String message, String key) {
        super(message);
        this.code = DEFAULT_CODE;
        this.key = key;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
