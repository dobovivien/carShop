package dev.carshop.carshop.converter;

import dev.carshop.carshop.dto.NewAdDto;
import dev.carshop.carshop.model.AdvertisementModel;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementConverter {

    public NewAdDto modelToNewAdDto(AdvertisementModel adModel) {
        return NewAdDto.builder()
                .brand(adModel.getBrand())
                .type(adModel.getType())
                .description(adModel.getDescription())
                .price(String.valueOf(adModel.getPrice()))
                .build();
    }
}
