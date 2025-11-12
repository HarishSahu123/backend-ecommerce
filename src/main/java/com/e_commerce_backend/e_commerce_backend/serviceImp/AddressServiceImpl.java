package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.Exception.ResourceNotFoundException;
import com.e_commerce_backend.e_commerce_backend.entity.Address;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.AddressDTO;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.repository.AddressRepository;
import com.e_commerce_backend.e_commerce_backend.repository.UserRepository;
import com.e_commerce_backend.e_commerce_backend.services.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }


    @Override
    public AddressDTO addUserAddress(AddressDTO addressDTO, Long userId) {
        Address address = modelMapper.map(addressDTO, Address.class);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User" ,"userId" ,+ userId));
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

}
