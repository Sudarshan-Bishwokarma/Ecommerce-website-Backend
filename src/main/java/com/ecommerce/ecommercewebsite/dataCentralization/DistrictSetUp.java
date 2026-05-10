package com.ecommerce.ecommercewebsite.dataCentralization;

import com.ecommerce.ecommercewebsite.model.District;
import com.ecommerce.ecommercewebsite.repositories.DistrictRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistrictSetUp {
    @Autowired
    DistrictRepository districtRepository;

    @PostConstruct
    public void init() {
        if (districtRepository.count() == 0) {
            String[] districts = {"Arghakhanchi", "Rupendehi", "Kapilbastu", "Palpa", "Banke", "Bardiya", "Dang", "East Rukum", "Gulmi", "Nawalparasi West", "Pyuthan", "Rolpa"
            };
            for (String name : districts) {
                District district = new District();
                district.setDistrictName(name);
                districtRepository.save(district);
            }
            System.out.println("District successfully loaded!");
        }

    }
}
