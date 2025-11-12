package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.AddressDTO;

public interface AddressService {
    AddressDTO addUserAddress(AddressDTO addressDTO, Long userId);
}
