package com.zlr.seat.vo;

import com.zlr.seat.entity.enums.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description
 * @create 2022-09-15-下午5:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 867933019328199779L;
    private int code;
    private String message;
    private T data;

    /**
     *  成功时候的调用
     * */
    public static  <T> Result<T> success(T data){
        return new Result<T>(ResultStatus.SUCCESS.getCode(),ResultStatus.SUCCESS.getMessage(),data);
    }

    public static  <T> Result<T> success(){
        return new Result<T>(ResultStatus.SUCCESS);
    }


    protected Result(ResultStatus status){
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    protected Result(ResultStatus status, String message){
        this.code = status.getCode();
        this.message = message;
    }


    public static  <T> Result<T> error(ResultStatus status){
        return new Result<T>(status);
    }

    public static  <T> Result<T> error(ResultStatus status, String message){
        return new Result<T>(status, message);
    }
}
