package com.choi.api.core.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApiResponseErr extends ApiResponse{

    private String error;

    public ApiResponseErr(){
        super(Status.error);
    }

    public ApiResponseErr(String error){
        super(Status.error);
        this.error = error;
    }

    public ApiResponseErr(String error, String msg) {
        super(ApiResponse.Status.error, msg);
        this.error = error;
    }
}
