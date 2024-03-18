package dev.carshop.carshop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewAdDto {

    String brand;

    String type;

    String description;

    String price;
}
