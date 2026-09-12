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

            String[] districts = {
                    // Lumbini Province
                    "Arghakhanchi",
                    "Banke",
                    "Bardiya",
                    "Dang",
                    "Gulmi",
                    "Kapilbastu",
                    "Palpa",
                    "Pyuthan",
                    "Rolpa",
                    "Rukum East",
                    "Rupandehi",
                    "Nawalparasi West"
            };

            for (String name : districts) {
                District district = new District();
                district.setDistrictName(name);
                districtRepository.save(district);
            }

            System.out.println("Districts successfully loaded!");
        }
    }
}