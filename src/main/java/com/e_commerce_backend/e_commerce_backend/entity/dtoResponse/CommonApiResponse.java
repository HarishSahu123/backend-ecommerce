package com.e_commerce_backend.e_commerce_backend.entity.dtoResponse;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class CommonApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    public CommonApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
