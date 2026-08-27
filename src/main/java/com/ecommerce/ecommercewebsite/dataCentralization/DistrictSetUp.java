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
                    // Koshi Province
                    "Bhojpur",
                    "Dhankuta",
                    "Ilam",
                    "Jhapa",
                    "Khotang",
                    "Morang",
                    "Okhaldhunga",
                    "Panchthar",
                    "Sankhuwasabha",
                    "Solukhumbu",
                    "Sunsari",
                    "Taplejung",
                    "Terhathum",
                    "Udayapur",

                    // Madhesh Province
                    "Bara",
                    "Dhanusha",
                    "Mahottari",
                    "Parsa",
                    "Rautahat",
                    "Saptari",
                    "Sarlahi",
                    "Siraha",

                    // Bagmati Province
                    "Bhaktapur",
                    "Chitwan",
                    "Dhading",
                    "Dolakha",
                    "Kathmandu",
                    "Kavrepalanchok",
                    "Lalitpur",
                    "Makwanpur",
                    "Nuwakot",
                    "Ramechhap",
                    "Rasuwa",
                    "Sindhuli",
                    "Sindhupalchok",

                    // Gandaki Province
                    "Baglung",
                    "Gorkha",
                    "Kaski",
                    "Lamjung",
                    "Manang",
                    "Mustang",
                    "Myagdi",
                    "Nawalpur",
                    "Parbat",
                    "Syangja",
                    "Tanahun",

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
                    "Nawalparasi West",

                    // Karnali Province
                    "Dailekh",
                    "Dolpa",
                    "Humla",
                    "Jajarkot",
                    "Jumla",
                    "Kalikot",
                    "Mugu",
                    "Rukum West",
                    "Salyan",
                    "Surkhet",

                    // Sudurpashchim Province
                    "Achham",
                    "Baitadi",
                    "Bajhang",
                    "Bajura",
                    "Dadeldhura",
                    "Darchula",
                    "Doti",
                    "Kailali",
                    "Kanchanpur"
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