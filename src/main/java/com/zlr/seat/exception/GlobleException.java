package com.zlr.seat.exception;

import com.zlr.seat.entity.enums.ResultStatus;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.exception
 * @Description
 * @create 2022-09-16-下午1:04
 */
public class GlobleException extends RuntimeException {


    private ResultStatus status;

    public GlobleException(ResultStatus status) {
        super();
        this.status = status;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}