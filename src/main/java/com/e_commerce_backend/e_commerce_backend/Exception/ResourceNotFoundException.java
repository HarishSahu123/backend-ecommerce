package com.e_commerce_backend.e_commerce_backend.Exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

public class ResourceNotFoundException extends RuntimeException{

    String reSourceName;
    String field;
    String fieldName;

    Long fieldId;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String reSourceName, String field, String fieldName) {
        super(String.format("%s is not found with %s: %s" ,reSourceName ,field,fieldName));
        this.reSourceName = reSourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String reSourceName, String field, Long fieldId) {
        super(String.format("%s is not found with %s :%d" ,reSourceName ,field,fieldId));
        this.reSourceName = reSourceName;
        this.field = field;
        this.fieldId = fieldId;
    }



}
