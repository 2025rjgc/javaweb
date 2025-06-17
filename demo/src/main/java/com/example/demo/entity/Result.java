package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result{
    private String code;
    private String message;
    private Object data;

    public static Result success(){
        Result result = new Result();
        result.code = "1";
        result.message = "success";
        return result;
    }

    public static Result success(Object data){
        Result result = success();
        result.data = data;
        return result;
    }

    public static Result error(String message){
        Result result = new Result();
        result.code = "0";
        result.message = message;
        return result;
    }
}
