package com.choi.api.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ApiResponse {

    public enum Status {
        success, fail, error
    }

    private Status status;
    private String msg;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date date = new Date();

    public ApiResponse(Status status){
        this.status = status;
        this.date = new Date();
    }

    public ApiResponse(Status status, String msg){
        this.status = status;
        this.msg = msg;
        this.date = new Date();
    }

    public ApiResponse(Status status, String msg, Date date) {
        this.status = status;
        this.msg = msg;
        this.date = date;
    }
}
