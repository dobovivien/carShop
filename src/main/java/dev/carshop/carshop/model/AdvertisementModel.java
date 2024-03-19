package dev.carshop.carshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementModel {

    private String id;

    private String brand;

    private String type;

    private String description;

    private String price;

    private String userEmail;
}
