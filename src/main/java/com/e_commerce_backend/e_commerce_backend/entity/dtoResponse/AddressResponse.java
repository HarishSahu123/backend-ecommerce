package com.e_commerce_backend.e_commerce_backend.entity.dtoResponse;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressResponse {
    List<AddressDTO> content;

    public List<AddressDTO> getContent() {
        return content;
    }

    public void setContent(List<AddressDTO> content) {
        this.content = content;
    }
}
