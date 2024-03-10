package com.choi.api.core.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApiResponseData<T> extends ApiResponse {
    private T data;

    public ApiResponseData(){
        super(ApiResponse.Status.success);
    }

    public ApiResponseData(T data){
        super(ApiResponse.Status.success, "성공적으로 처리되었습니다.");
        this.data = data;
    }

    public ApiResponseData(Status status, T data){
        super(ApiResponse.Status.success, "성공적으로 처리되었습니다.");
        this.data = data;
    }
}
