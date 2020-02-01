package com.mmall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json对象时，如果是null的对象，key也会消失
public class ServletResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    public ServletResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ServletResponse(int status) {
        this.status = status;
    }

    public ServletResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ServletResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }


    //作用：在实体类向前台返回数据时用来忽略不想传递给前台的属性或接口
    @JsonIgnore
    //使之不在json序列化的对象当中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode() ;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServletResponse<T> createBySuccess(){

        return new ServletResponse<T>(ResponseCode.SUCCESS.getCode());
    }


    public static <T> ServletResponse<T> createBySuccessMessage(String msg){
        return new ServletResponse<T>(ResponseCode.SUCCESS.getCode(),msg);

    }

    public static <T> ServletResponse<T> createBySuccess(T data){

        return new ServletResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }


    public static <T> ServletResponse<T> createBySuccess(String msq,T data){

        return new ServletResponse<T>(ResponseCode.SUCCESS.getCode(),msq,data);
    }


    public static <T> ServletResponse<T> createByError(){

        return new ServletResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServletResponse<T> createByErrorMessage(String errorMessage){

        return new ServletResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }


    public static <T> ServletResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){

        return new ServletResponse<T>(errorCode,errorMessage);
    }

















}
