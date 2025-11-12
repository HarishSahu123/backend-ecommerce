package com.e_commerce_backend.e_commerce_backend.entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long cart_id;
    private Double total_Price;
    private List<ProductDTO> product= new ArrayList<>();


    public Long getCart_id() {
        return cart_id;
    }

    public void setCart_id(Long cart_id) {
        this.cart_id = cart_id;
    }

    public Double getTotal_Price() {
        return total_Price;
    }

    public void setTotal_Price(Double total_Price) {
        this.total_Price = total_Price;
    }

    public List<ProductDTO> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDTO> product) {
        this.product = product;
    }
}
