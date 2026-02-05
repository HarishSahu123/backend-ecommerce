package com.e_commerce_backend.e_commerce_backend.serviceImp;

import jakarta.persistence.Embedded;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface tempAnnotation {
    String value() default "";

}
