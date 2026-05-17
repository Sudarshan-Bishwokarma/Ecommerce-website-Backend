package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.model.District;
import com.ecommerce.ecommercewebsite.repositories.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    DistrictRepository districtRepository;

    @Override
    public List<District> getAllDistricts() {
        List<District> allDistricts = districtRepository.findAll();
        return allDistricts;
    }
}
